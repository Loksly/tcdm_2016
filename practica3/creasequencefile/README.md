# creasequencefile

* Hacer un programa MapReduce MapOnly (sin reducers) que lea el fichero apat63_99.txt, separe los campos y lo guarde como un fichero Sequence con:
  * clave: el país (Text, sin comillas)
  * valor: una cadena con la patente y el año separados por coma (Text)

* IMPORTANTE:
  * El fichero de entrada no se puede modificar, por lo que nuestro código tiene que ser capaz de manejar la primera línea del fichero que es diferente a las demás.
  * Fijar el número de reducers a 0 (MapOnly job)
* La salida serán varios ficheros part-m-*. Se puede ver el contenido de estos ficheros ejecutando: hsfs dfs -text part-m-*


## Versión Java

### Compilación

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/archive/master.zip
$ unzip master.zip
$ cd tcdm_2016/practica3/creasequencefile/java/
$ mvn package
```

### Ejecución

#### Paso previo opcional, si no se ha realizado la compilación
```bash
$ wget -q -O creasequencefile.jar https://github.com/Loksly/tcdm_2016/raw/master/practica3/creasequencefile/target/creasequencefile-0.0.1-SNAPSHOT.jar
```

#### Si se ha realizado la compilación es necesario acceder al fichero jar generado
```bash
$ mv target/creasequencefile-0.0.1-SNAPSHOT.jar creasequencefile.jar
```

#### Ejecución

Supuesto que ya se han subido los ficheros de datos de prueba y que están en el HDFS en el directorio _datos_.
De no ser así le invito a seguir los pasos descritos en el 
[apartado](https://github.com/Loksly/tcdm_2016/tree/master/practica3#carga-de-datos-de-prueba)
relativo a la carga de datos.

```bash
$ hdfs dfs -rm -r sequence # sólo si es ejecutado por segunda vez
$ yarn jar creasequencefile.jar datos/patentes-mini/apat63_99.txt sequence
$ hdfs dfs -text sequence/part-m-00000
```

#### Test

Ejemplo de salida:

```bash
$ hdfs dfs -text sequence/part-m-00000 | head
JP      4101646,1978
US      4186332,1980
JP      4920512,1990
ET      3512825,1970
US      3797992,1974
US      5394547,1995
JP      4299230,1981
US      4348596,1982
US      4708861,1987
NL      4694033,1987
```
