package cursohadoop.citingpatents;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/*
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
*/

import org.apache.hadoop.util.ToolRunner;


public class CPDriver extends Configured implements Tool {
	public int run(String[] arg0) throws Exception {

		if (arg0.length != 2) {
			System.err.printf("Usar: %s [opciones genéricas] <directorio_entrada> <directorio_salida>%n",
					getClass().getSimpleName());
			System.err.printf("Recuerda que el directorio de salida no puede existir");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		/* Obtiene la configuración por defecto y modifica algún parámetro */
		Configuration conf = getConf();
		conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",");
		conf.set("mapreduce.textoutputformat.separator", "\t");
		
		Job job = Job.getInstance(conf);
		job.setJobName("Lista de citas");
		job.setJarByClass(getClass());
		
		//KeyValueTextInputFormat.addInputPath(job, new Path(arg0[0]));
		FileInputFormat.addInputPath(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));

		FileOutputFormat.setCompressOutput(job, true);
		FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setNumReduceTasks(1);

		job.setMapperClass(CPMapper.class);
		job.setCombinerClass(CPReducer.class);
		job.setReducerClass(CPReducer.class);
		
		return job.waitForCompletion(true) ? 0 : 1;
	}

	/**
	 * Usar yarn jar CitationNumberByPatent.jar dir_entrada dir_salida
	 * 
	 * @param args
	 *            dir_entrada dir_salida
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new CPDriver(), args);
		System.exit(exitCode);
	}
}
