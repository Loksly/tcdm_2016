package cursohadoop.citingpatents;

/**
 * Ejemplos basados en los del libro Hadoop in Action, C. Lam, 2011
 * 
 * Ficheros de datos: 
 * 
 * cite75_99.txt -> citas realizadas por patentes a otras patentes
 *                  1 columna: nº de patente que cita
 *                  2 columna: nº de patente citada
 *                  
 * apat63_99.txt -> información sobre las patentes. Algunos campos:
 *                  1 columna: nº de patente
 *                  2 columna: año
 *                  5 columna: país
 *                  9 columna: nº de reivindicaciones de la patente
 * 
 * 
 *  CitingPatents - cites by number: Obtiene la lista de citas de una patente (fichero cite75_99.txt).
 *  	mapper -> CPMapper
 *              Para cada línea, invierte las columnas (patente citada, patente que cita)
 *      reducer -> CPReducer
 *    	 	Para cada línea, agrupa en un string las patentes que citan
 *      
 */

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
import org.apache.hadoop.util.ToolRunner;


public class CPDriver extends Configured implements Tool {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.hadoop.util.Tool#run(java.lang.String[])
	 */
	public int run(String[] arg0) throws Exception {

		/*
		 * Comprueba los parámetros de entrada  
		 */
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
		
		
		/* Obtiene un job a partir de la configuración actual */
		Job job = Job.getInstance(conf);
		job.setJobName("Lista de citas");
		
		/* Fija el jar del trabajo a partir de la clase del objeto actual */
		job.setJarByClass(getClass());
		
		FileInputFormat.addInputPath(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));

		
		// TODO: Fijamos la compresión
		FileOutputFormat.setCompressOutput(job, true);
		FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
		
		// TODO:
		// Fija el formato de los ficheros de entrada y salida 
		//   KeyValueTextInputFormat - Cada línea del fichero es un registro. El primer separador de la línea (por defecto \t)
		//                             separa la línea en clave y valor. El separador puede especificarse en la propiedad
		//                             mapreduce.input.keyvaluelinerecordreader.key.value.separator, por ejemplo, usando
		//                             conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",") antes 
		//                             de crear el job
		//                             Clave - Text; Valor - Text 
		//   TextOutputFormat - Escribe cada registro como una línea de texto. Claves y valores se escriben como strings separadas
		//                      por \t (separador especificable mediante mapred.textoutputformat.separator)
		//


		// TODO: Especifica el tipo de la clave y el valor de salida del mapper
		// No es necesario si los tipos son iguales a los tipos de la salida 
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		// TODO: Especifica el tipo de la clave y el valor de salida final
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		// TODO: Especifica el número de reducers
		job.setNumReduceTasks(1);

		// Especifica el mapper, el combiner y el reducer
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
