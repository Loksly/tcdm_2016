
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

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica3/datos.tgz
$ tar xvfz datos.tgz
$ hdfs dfs -put datos hdfs:///tmp/citationtest
$ hdfs dfs -rm -r hdfs:///tmp/citingpatents # sólo si es ejecutado por segunda vez
$ yarn jar CitationNumberByPatent.jar hdfs:///tmp/citationtest/patentes-mini/cite75_99.txt hdfs:///tmp/citingpatents
$ hdfs dfs -get hdfs:///tmp/citingpatents/part-r-00000.gz
```
