
## CitationNumberByPatent.pig

**Objetivo**: obtener el número de citas que recibió cada patente (a partir del fichero cite75_99.txt)

**Pasos**:

* Cargar el fichero de la siguiente forma: 
    * ```LOAD 'fichero' USING PigStorage(',') AS (citing:int, cited:int);```

* Agrupar por el campo cited

* Para cada grupo, ver el número de elementos que tiene.

* Para probarlo, ejecutarlo en el cluster en modo interactivo.

* Ejecutar el comando *pig*

* En el prompt de *grunt* ir ejecutando una a una las instrucciones del script

* Después de una asignación se puede usar el comando _ILLUSTRATE_ para ver una muestra del resultado, por ejemplo:

```
grunt> ILLUSTRATE citas
```

* Usar un STORE para guardar la salida en un fichero.
    * El comando STORE lanza la ejecución del MapReduce.



## Ejecución

### Previo:

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica3/datos.tgz
$ tar xvfz datos.tgz
$ hdfs dfs -put datos
$ hdfs dfs -ls datos/patentes-mini/cite75_99.txt
```

### Ejecución:

Supuesto que pig está en el _PATH_:

```bash
$ pig
```

Copiar una a una las líneas del fichero [CitationNumberByPatent.pig](CitationNumberByPatent.pig),
que copio aquí por su brevedad:

```piglatin
citings = LOAD 'datos/patentes-mini/cite75_99.txt' USING PigStorage(',') AS (citing:int, cited:int);
references = GROUP citings BY cited; 
citas = FOREACH references GENERATE group, COUNT(citings);
STORE citas INTO 'pig_output' USING PigStorage(',');
```

Nota, se recomienda leer el fichero para leer los comentarios al mismo.


### Test:

```bash
$ hdfs dfs -cat pig_output/part-r-00000

```

Se puede comparar con el del ejercicio de la práctica 3
[citationnumberbypatent_chained](../../practica3/citationnumberbypatent_chained),
mediante el comando _diff_ si se realiza un _sort_ previo sobre la salida de éste.

