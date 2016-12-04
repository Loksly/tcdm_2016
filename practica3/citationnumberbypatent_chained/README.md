# Citation Number By Patent - Chained

## Objetivo

Obtener el número de citas de una patente, combinando el programa 01-citingpatents con un mapper adicional que, a partir de la salida del reducer del citingpatents, para cada patente cuente el número de patentes que la citan. La salida debe de estar en texto plano con dos columnas (patente, nº de citas) por línea, separadas por una coma.


## Versión Java

### Compilación

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/archive/master.zip
$ unzip master.zip
$ cd tcdm_2016/practica3/citationnumberbypatent_chained/java/
$ mvn package
```

### Ejecución

#### Paso previo opcional, si no se ha realizado la compilación
```bash
$ wget -q -O citationnumberbypatent_chained.jar https://github.com/Loksly/tcdm_2016/raw/master/practica3/citationnumberbypatent_chained/target/citationnumberbypatent_chained-0.0.1-SNAPSHOT.jar
```

#### Si se ha realizado la compilación es necesario acceder al fichero jar generado
```bash
$ mv target/citationnumberbypatent_chained-0.0.1-SNAPSHOT.jar citationnumberbypatent_chained.jar
```

#### Ejecución

Supuesto que ya se han subido los ficheros de datos de prueba y que están en el HDFS en el directorio _datos_.
De no ser así le invito a seguir los pasos descritos en el 
[apartado](https://github.com/Loksly/tcdm_2016/tree/master/practica3#carga-de-datos-de-prueba)
relativo a la carga de datos.

```bash
$ hdfs dfs -rm -r citingpatents2 # sólo si es ejecutado por segunda vez
$ yarn jar citationnumberbypatent_chained.jar datos/patentes-mini/cite75_99.txt citingpatents2
$ hdfs dfs -cat citingpatents2/part-r-00000
```


#### Test

Primeras líneas de salida:

```txt
1000089 10
1000505 1
1015464 1
1019223 1
1022360 1
1025180 1
1034533 1
1036148 1
1036585 1
1038011 1
```

Que coincide con lo esperado tras la ejecución de este comando, en local:

```bash
$ grep 1000089 /tcdm_2016/practica3/datos/patentes-mini/cite75_99.txt
5395228,1000089
5544405,1000089
5503546,1000089
5505610,1000089
5277853,1000089
5571464,1000089
5540869,1000089
5505607,1000089
5505611,1000089
5807591,1000089
```
