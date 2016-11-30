package cursohadoop.citingpatents;

/**
 * Reducer para CitingPatents - cites by number: Obtiene el número de citas de una patente
 * Para cada línea, obtiene la clave (patente) y une en un string el número de patentes que la citan
 */
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;
import java.io.IOException;

public class CPReducer extends Reducer<Text, Text, Text, Text> {
	/**
	 * Método reduce
	 * @param key Patente citada
	 * @param values Lista con las patentes que la citan
	 * @param context Contexto MapReduce
	 * @throws IOException
	 * @throws InterruptedException
	 */

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
