package cursohadoop.simplereducesidejoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
/**
 * Driver para probar un Reduce Side Join simple
 * 
 * Utiliza la salida de CitingPatents - cites by number (Obtiene la lista de citas de una patente) 
 * y el fichero apat63_99.txt (información sobre las patentes)
 * 
 * Usa dos mappers:
 * 		CNBPTaggedMapperTest -> similar a CNBPMapper pero devuelve un valor etiquetado
 *      PBCMapper -> devuelve el pais de cada patente (etiquetado)
 *      
 * El reducer hace un join de la salida de los dos mappers (ordenada por patente) 
 * y devuelve:
 * 
 * 		n_patente, n_citas, pais_de_la_patente
 * 
 * Si no tiene información sobre el pais de la patente, devuelve
 * 
 * 		n_patente, n_citas, No disponible
 * 
 * @author tomas
 *
 */
public class SRSJDriver extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		/*
		 * Comprueba los parámetros de entrada  
		 */
		if (args.length != 3) {
			System.err.printf("Usar: %s [opciones genéricas] <entrada_citas> <entrada_info> <directorio_salida>%n",
					getClass().getSimpleName());
			System.err.printf("Recuerda que el directorio de salida no puede existir");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		/* Obtiene la configuración por defecto y modifica algún parámetro */
		Configuration conf = getConf();
		
		/* Obtiene un job a partir de la configuración actual */
		Job job = Job.getInstance(conf);
		job.setJobName("Lista de citas");
		/* Fija el jar del trabajo a partir de la clase del objeto actual */
		job.setJarByClass(getClass());

		/* Paths de entrada y salida */
		Path entradaCitas = new Path(args[0]);
		Path entradaInfo = new Path(args[1]);
		Path salida = new Path(args[2]);
		
		// TODO: Como tenemos dos entradas diferentes (con dos mappers diferentes)
		// usamos MultipleInputs, indicando en path de entrada, el formato
		// de la entrada y el mapper a usar para cada una 
		MultipleInputs.addInputPath(job, entradaCitas, TextInputFormat.class, CNBPTaggedMapper.class);
		MultipleInputs.addInputPath(job, entradaInfo, SequenceFileInputFormat.class, PBCMapper.class);
		
		/* Añadimos el path de salida */
		FileOutputFormat.setOutputPath(job, salida);

		// TODO: Especifica el tipo de la clave y el valor de salida de los mappers.
		// No es necesario si los tipos son iguales a los tipos de la salida final.
		// Usamos LongWritable para el número de patente para que las ordene 
		// numéricamente 


		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(TaggedText.class);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		
		job.setNumReduceTasks(4);
		job.setReducerClass(SRSJReducer.class);

		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	/**
	 * Usar yarn jar SimpleReduceSideJoin.jar patentes_citas patentes_info dir_salida
	 * 
	 * @param args
	 *            patentes_citas patentes_info dir_salida
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new SRSJDriver(), args);
		System.exit(exitCode);
	}

}
