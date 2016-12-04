package cursohadoop.sortsecundario;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Ejemplos basados en los del libro Hadoop in Action, C. Lam, 2011
 *  * 
 *  SortSecundario - Obtiene una lista ordenada (país   año -> no de patentes ese año)
 *  La salida debe estar ordenada por países, y para cada país, por año (sort secundario)
 *  
 *  Usa como entrada el fichero sequence creado por creasequencefile
 *      
 */

public class SortSecundario extends Configured implements Tool {

	@Override
	public int run(String[] arg0) throws Exception {
		// Crea el job
		Job job = creaJob(this, getConf(), arg0);
		if (job == null) {
			return -1;
		}
		job.setJobName("Ordenación secundaria");


		job.setInputFormatClass( SequenceFileInputFormat.class   );
		job.setOutputFormatClass( TextOutputFormat.class );

		// TODO: Tipo de clave y valor de salida del mapper
		job.setMapOutputKeyClass( PaisAnho.class  );
		job.setMapOutputValueClass( Text.class );
		
		// TODO: Tipo de clave y valor de salida del reducer
		job.setOutputKeyClass( PaisAnho.class );
		job.setOutputValueClass( Text.class );

		//Especifica el mapper
		job.setMapperClass(SortSecundarioMapper.class);

		//TODO: Especifica el particionador
		job.setPartitionerClass(PaisPartitioner.class);
		//TODO: Especifica el comparador
		job.setSortComparatorClass(ClaveCompuestaComparator.class);
		//TODO: Especifica el agrupador
		job.setGroupingComparatorClass(ClaveCompuestaComparator.class);

		//TODO: Especifica el reducer
		job.setReducerClass(SortSecundarioReducer.class);

		job.setNumReduceTasks(2);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new SortSecundario(), args);
		System.exit(exitCode);
	}

	// TODO: Completar el mapper
	static class SortSecundarioMapper extends
			Mapper<Text, Text, PaisAnho, Text> {
		// Input: clave = país; valor = patente,año
		// Output: clave: clave compuesta PaisAnho; valor: patente
		@Override
		public void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {

				String[] parts = value.toString().split(",");
				Text anyo = new Text(parts[1]);
				context.write(new PaisAnho(key, anyo), new Text(parts[0]) );
		}
	}

	// TODO: Completar el reducer
	static class SortSecundarioReducer extends
			Reducer<PaisAnho, Text, Text, Text> {
		// Input: clave: clave compuesta PaisAnho; valor: lista de patentes
		// Output: clave: país; valor: año -> n_patentes
		@Override
		public void reduce(PaisAnho key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException {

				int i = 0;
				for(Text t: values){
					i++;
				}

				context.write(key.getPais(), new Text( key.getAnho().toString() + " -> " + i) );
		}
	}

	public static class ClaveCompuestaComparator extends WritableComparator {
		protected ClaveCompuestaComparator() {
			super(PaisAnho.class, true);
		}
		@SuppressWarnings("rawtypes")
		@Override
		public int compare(WritableComparable w1, WritableComparable w2) {
			PaisAnho pa1 = (PaisAnho) w1;
			PaisAnho pa2 = (PaisAnho) w2;
			int cmp = pa1.getPais().compareTo(pa2.getPais());
			if (cmp == 0) {
				return pa1.getAnho().compareTo(pa2.getAnho());
			}
			return cmp;
		}
	}

	public static class PaisPartitioner extends Partitioner<PaisAnho, Text> {
		@Override
		public int getPartition(PaisAnho key, Text value, int numPartitions) {
			int hash = key.getPais().hashCode();
			int partition = hash % numPartitions;
			return partition;
		}
	}

	public static class PaisAnho implements WritableComparable<PaisAnho> {

		public PaisAnho() {
			set(new Text(), new Text());
		}

		public PaisAnho(Text pais, Text anho) {
			set(pais, anho);
		}

		public void set(Text pais, Text anho) {
			this.pais = pais;
			this.anho = anho;
		}

		public Text getPais() {
			return pais;
		}

		public Text getAnho() {
			return anho;
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			pais.readFields(in);
			anho.readFields(in);
		}

		@Override
		public void write(DataOutput out) throws IOException {
			pais.write(out);
			anho.write(out);
		}

		@Override
		public int compareTo(PaisAnho o) {
			int cmp = pais.compareTo(o.pais);
			if (cmp != 0) {
				return cmp;
			}
			return anho.compareTo(o.anho);
		}
		
		@Override
		public int hashCode() {
			return pais.hashCode() * 163 + anho.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof PaisAnho) {
				PaisAnho pa = (PaisAnho) o;
				return pais.equals(pa.pais) && anho.equals(pa.anho);
			}
			return false;
		}
		
		@Override
		public String toString() {
			return "["+pais+", "+anho+"]";
		}
		
		private Text pais;
		private Text anho;

	}

	private Job creaJob(Tool tool, Configuration conf, String[] args)
			throws IOException {
		/* Comprueba los parámetros de entrada */
		if (args.length != 2) {
			System.err
					.printf("Usar: %s [opciones genéricas] <directorio_entrada> <directorio_salida>%n",
							getClass().getSimpleName());
			System.err
					.printf("Recuerda que el directorio de salida no puede existir");
			ToolRunner.printGenericCommandUsage(System.err);
			return null;
		}

		/* Obtiene un job a partir de la configuración actual */
		Job job = Job.getInstance(conf);

		/* Fija el jar del trabajo a partir de la clase del objeto actual */
		job.setJarByClass(tool.getClass());

		/* Añade al job los paths de entrada y salida */
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job;
	}
}
