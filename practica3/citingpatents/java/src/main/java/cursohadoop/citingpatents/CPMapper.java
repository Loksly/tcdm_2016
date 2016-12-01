package cursohadoop.citingpatents;

/**
 * Mapper para CitingPatents - cites by number: Obtiene el número de citas de una patente
 * Para cada línea, invierte las columnas (patente citada, patente que cita)
 */
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class CPMapper extends Mapper<LongWritable, Text, Text, Text> {
	@Override
	public void map(LongWritable key, Text value, Context context)
		throws IOException, InterruptedException {

			if (key.get() == 0){ return; }
			String[] split = value.toString().split(",");
			context.write(new Text(split[1]), new Text(split[0]));
		}
}
/*
public class CPMapper extends Mapper<Text, Text, Text, Text> {
	@Override
	public void map(Text key, Text value, Context context)
		throws IOException, InterruptedException {

			if (key == "CITED"){ return; }
			String[] split = value.toString().split(",");
			context.write(new Text(split[1]), new Text(split[0]));
		}
}
*/