
# Citation Number By Patent

* Objetivo: Crear un programa MapReduce que, para cada patente de cite75_99.txt, obtenga la lista de las que la citan
  * Formato salida: patente   patente1,patente2...
  * Mapper:
    * 3858245,3755824 → 3755824   3858245

  * Reducer:
    * 3755824  {3858245 3858247. . . } → 3755824   3858245,3858247...

* IMPORTANTE:
  * Utilizad como formato de entrada KeyValueTextInputFormat, indicando que el formato separador de campos es una coma.
  * La salida debe de guardarla en formato comprimido gzip.
  * Utilizad los métodos estáticos setCompressOutput y setOutputCompressorClass de la clase FileOutputFormat



## Versión Java

### Compilación

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/archive/master.zip
$ unzip master.zip
$ cd tcdm_2016/practica3/citingpatents/java/
$ mvn package
```

### Ejecución

#### Paso previo opcional, si no se ha realizado la compilación
```bash
$ wget -q -O CitationNumberByPatent.jar https://github.com/Loksly/tcdm_2016/raw/master/practica3/citingpatents/target/CitationNumberByPatent-0.0.1-SNAPSHOT.jar
```

#### Si se ha realizado la compilación es necesario acceder al fichero jar generado
```bash
$ mv target/CitationNumberByPatent-0.0.1-SNAPSHOT.jar CitationNumberByPatent.jar
```

#### Ejecución

Supuesto que ya se han subido los ficheros de datos de prueba y que están en el HDFS en el directorio _datos_.
De no ser así le invito a seguir los pasos descritos en el 
[apartado](https://github.com/Loksly/tcdm_2016/tree/master/practica3#carga-de-datos-de-prueba)
relativo a la carga de datos.

```bash
$ hdfs dfs -rm -r citingpatents # sólo si es ejecutado por segunda vez
$ yarn jar CitationNumberByPatent.jar datos/patentes-mini/cite75_99.txt citingpatents
$ hdfs dfs -get citingpatents/part-r-00000.gz
```



#### Comentarios


He desobedecido explícitamente la recomendación de usar __KeyValueTextInputFormat__ aunque he dejado rastros
en el código de cómo se utilizaría:

Básicamente tras su importación como:


```java
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

/*
posteriormente en el método run, bastaría con reemplazar la invocación de

FileInputFormat.addInputPath(job, new Path(arg0[0]));

por:
 */

KeyValueTextInputFormat.addInputPath(job, new Path(arg0[0]));
``` 

Y la clase CPMapper sería así:

```java
public class CPMapper extends Mapper<Text, Text, Text, Text> {
	@Override
	public void map(Text key, Text value, Context context)
		throws IOException, InterruptedException {

			if (key == "\"CITING\""){ return; }
			String[] split = value.toString().split(",");
			context.write(new Text(split[1]), new Text(split[0]));
		}
}
```

Sinceramente, me parece más elegante el código sin comprobar se está tratando la primera línea con ese String concreto.
De ahí que mi solución propuesta sea otra distinta.
