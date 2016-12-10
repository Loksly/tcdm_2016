package cursohadoop.simplereducesidejoin;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CNBPTaggedMapper extends Mapper<LongWritable, Text, LongWritable, TaggedText> {
	/**
	 * map - para cada línea de la salida de citingpatents, obtiene la patente y el número de citas con la etiqueta "cite"
	 * 
	 * @param key  n de patente
	 * @parm value string con las citas de la patente separadas por coma
	 * 
	 * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN,
	 * org.apache.hadoop.mapreduce.Mapper.Context)
	 */	

	@Override
	public void map(LongWritable key, Text value, Context context)
		throws IOException, InterruptedException {

		if (key.get() == 0){
			return;
		}
		try {
			// TODO: Escribimos el número de patente como un long para que nos los ordene numéricamente
			LongWritable npatente = new LongWritable(Long.parseLong(key.toString()));
			
			// TODO: Dividimos el valor usando "," como separador. La longitud de este array nos indica el nº de citas.
			cites = value.toString().split(",");
			int ncites = cites.length + 1;
		
			// TODO: Etiquetamos el número de citas con el texto "cite" para hacer el join
			nCitesTagged.set(new Text("cite"), new Text(ncites + "") );
			
			// Escribimos en el contexto el número de patente y el número de citas etiquetado
			context.write(npatente, nCitesTagged);
		} catch (NumberFormatException e) {
			System.err.println("Error procesando patente en CNBPTaggedMapper "+key.toString());
			context.setStatus("Error procesando patente en CNBPTaggedMapper "+key.toString());
		}
	}
	private TaggedText nCitesTagged = new TaggedText();
	private String[] cites;
}
