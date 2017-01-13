
## [Versión PIG](./SimpleJoin.pig)

**Objetivo**: Realiza en pig un Join idéntico al que hicimos en MapReduce (programa "_simplereducesidejoin_").
Para simplificar, utilizad como entrada los ficheros de texto cite77_99.txt y apat63_99.txt, no el fichero .seq) 

El script se debe ejecutar de la siguiente forma:

```bash
$ pig -param citas=path-cite75_99.txt -param info=path-apat63_99.txt -param output=dir_salida SimpleJoin.pig
```

Debeis probarlo en el cluster y con varios reducers

**Pasos**:

* Hacer lo mismo que en "CitationNumberByPatent.pig" para generar las patentes y el número de citas (cites), pero sin hacer el _STORE_.
* Cargar el fichero apat63_99.txt:
    * ``` info = LOAD '$info' USING PigStorage(',') AS (patent:int, year:int, int, int, country:chararray); ```
* Usar un _FOREACH_ para quedarnos con la patente, el país y el año (yearcountry)
* Usar el comando _JOIN_ para unir cites y yearcountry por la patente.
    * ``` joininfo = JOIN cites BY patent, yearcountry BY patent; ```

* Para que la salida quede mejor, usar un _FOREACH_ para eliminar una de las ocurrencias de patent y ordenar la salida por año:
    * ``` patentinfo = FOREACH joininfo GENERATE cites::patent,country,year,ncites; ```
    * ``` orderedinfo = ORDER patentinfo BY year ASC; ```
* Guardar la salida separando por coma:
    * ``` STORE patentinfo INTO '$output' USING PigStorage(','); ```



## Ejecución

### Previo:

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica3/datos.tgz
$ tar xvfz datos.tgz
$ hdfs dfs -put datos
$ hdfs dfs -ls datos/patentes-mini/cite75_99.txt
$ hdfs dfs -ls datos/patentes-mini/apat63_99.txt
```

### Ejecución:

Supuesto que pig está en el _PATH_:

```bash
$ pig -param citas=datos/patentes-mini/cite75_99.txt -param info=datos/patentes-mini/apat63_99.txt -param output=dir_salida SimpleJoin.pig
```

### Test:

```bash
$ hdfs dfs -cat dir_salida/part-r-00000

```


## [Versión HIVE](./SimpleJoin.hive)

**Objetivo**: Realiza en Hive el mismo Join que en Pig

**Pasos**:

* Busca en la documentación de Hive como crear tablas.
* Crea tablas para los datos de los ficheros cite75_99.txt y apat63_99.txt.
* Carga los ficheros en las tablas.
* Realiza en Join y muestra la salida ordenada por pais y año.


```text```
Nota, he escrito el código pero no consigo ejecutarlo por segunda vez por un problema de configuración
que ha dejado incosistente la instalación de hive y ahora no termina de arrancar.
En principio el código debería funcionar pero no tengo forma de comprobarlo.
```
