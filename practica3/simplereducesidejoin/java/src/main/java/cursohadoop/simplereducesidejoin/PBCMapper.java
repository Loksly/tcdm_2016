package cursohadoop.simplereducesidejoin;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PBCMapper extends Mapper<Text, Text, LongWritable, TaggedText> {

	@Override
	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {

		fields = value.toString().split(",");
		/* formato real PAIS -> patente,anyo */
		try {
			LongWritable patente = new LongWritable(Long.parseLong(fields[0]));
			country = key.toString();
			TaggedText countryTagged = new TaggedText("country", country);		
			context.write(patente, countryTagged);
			
		} catch (NumberFormatException e) {
			System.err.println("Error procesando parente en PBCMapper " + fields[0]);
			context.setStatus("Error procesando parente en PBCMapper " + fields[0]);
		}	
	}
	private String[] fields;
	private String country;
}
