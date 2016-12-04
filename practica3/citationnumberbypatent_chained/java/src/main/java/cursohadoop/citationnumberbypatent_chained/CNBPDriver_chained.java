package cursohadoop.citationnumberbypatent_chained;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;

import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;

import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.util.Tool;

import java.io.IOException;

public class CNBPDriver_chained extends Configured implements Tool {

	/* CharacterCounter credits belong to: http://stackoverflow.com/a/9548397/1082061 */
	public static class CharacterCounter{
		public static int countOccurrences(String find, String string){
			int count = 0;
			int indexOf = 0;
			while (indexOf > -1){
				indexOf = string.indexOf(find, indexOf + 1);
				if (indexOf > -1){
					count++;
				}
			}
			return count;
		}
	}

	public static class SplitMapper extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
				if (key.get() == 0){ return; }
				String[] split = value.toString().split(",");
				context.write(new Text(split[1]), new Text(split[0]));
			}
	}

	public static class CountMapper extends Mapper<Text, Text, Text, IntWritable> {
		@Override
		public void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {
				//the easiest is just to count the number of commas
				int commas = CharacterCounter.countOccurrences(",", value.toString() ) + 1;
				context.write(key, new IntWritable(commas));
			}
	}

	public static class CCReducer extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
				StringBuilder sb = new StringBuilder();
				for (Text value : values) {
					if (sb.length() > 0 ){
						sb.append(",");
					}
					sb.append(value);
				}
				context.write(key, new Text(sb.toString()));
			}
	}

	@Override
	public int run(String[] arg0) throws Exception {
		if (arg0.length != 2) {
			System.err.printf("Usar: %s [opciones genéricas] <directorio_entrada> <directorio_salida>%n",
					getClass().getSimpleName());
			System.err.printf("Recuerda que el directorio de salida no debe existir");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		Configuration conf = getConf();		
		//conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",");
		
		Job job = Job.getInstance(conf);
		job.setJobName("Trabajo encadenado");

		job.setJarByClass(getClass());
	
		FileInputFormat.addInputPath(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));

		Configuration configuration_concatpapers = new Configuration(false);
		Configuration configuration_countpapers = new Configuration(false);
		Configuration configuration_reduce = new Configuration(false);
	
		ChainMapper.addMapper(job, SplitMapper.class, LongWritable.class, Text.class, Text.class, Text.class, configuration_concatpapers);
		ChainReducer.setReducer(job, CCReducer.class,  Text.class, Text.class, Text.class, Text.class, configuration_reduce );
		ChainReducer.addMapper(job, CountMapper.class, Text.class, Text.class, Text.class, IntWritable.class, configuration_countpapers);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Configuration(), new CNBPDriver_chained(), args);
		System.exit(exitCode);
	}
}
