package cursohadoop.simplereducesidejoin;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SRSJReducer extends Reducer<LongWritable, TaggedText, LongWritable, Text> {
	/**
	 * Método reduce
	 * @param key Patente
	 * @param values Lista que contiene la patente y el número de citas o la patente y el país
	 * @param context Contexto MapReduce
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void reduce(LongWritable key, Iterable<TaggedText> values, Context context) throws IOException, InterruptedException {
		String country = "";
		String ncites = "";
		for(TaggedText tt : values) {
			String tag = tt.getTag().toString();
			String value = tt.getValue().toString();

			if(tag.equalsIgnoreCase("country")) {
				country += value + ",";
			} else {
				ncites += value;
			}
		}

		if(ncites.isEmpty()) {
			ncites = "0";
		}

		if (country.isEmpty()) {
			country = "No disponible,";
		}
		
		context.write(key, new Text(country + ncites) );
	}
}
