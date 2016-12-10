package cursohadoop.creasequencefile;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MRPatentDataToSequence extends Configured implements Tool {
	// Tamano en bytes de la primera linea del fichero apat, para saltarnosla
	private static long bytes_primera_linea = 225;

	@Override
	public int run(String[] arg0) throws Exception {
		Job job = creaJob(this, getConf(), arg0);
		if (job == null) {
			return -1;
		}
		job.setJobName("Crea fichero sequence");

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(MRPatentDataToSequenceMapper.class);
		job.setNumReduceTasks(0);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new MRPatentDataToSequence(), args);
		System.exit(exitCode);
	}

	public static class MRPatentDataToSequenceMapper extends
			Mapper<LongWritable, Text, Text, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			if (key.get() > bytes_primera_linea) {
				//bastaria con poner mayor que 0, la entrada al map va línea por línea
				String[] fields = value.toString().split(",");
				Text pais = new Text(fields[4].replace("\"", ""));
				Text patenteanho = new Text(fields[0] + "," + fields[1]);
				context.write(pais, patenteanho);
			}
		}
	}

	private Job creaJob(Tool tool, Configuration conf, String[] args)
			throws IOException {
		if (args.length != 2) {
			System.err
					.printf("Usar: %s [opciones genéricas] <directorio_entrada> <directorio_salida>%n",
							getClass().getSimpleName());
			System.err
					.printf("Recuerda que el directorio de salida no puede existir");
			ToolRunner.printGenericCommandUsage(System.err);
			return null;
		}

		Job job = Job.getInstance(conf);
		job.setJarByClass(tool.getClass());

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job;
	}
}
