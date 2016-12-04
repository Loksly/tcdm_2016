# SimpleReduceSideJoin


## Objetivo

Unir datos de dos entradas

   (a) Fichero/s de salida de 02-citingpatents
   (b) Ficheros de salida de 04-creasequencefile

## Salida

  * patente, país, n_citas

La salida debe quedar ordenada por patente (numéricamente)

## Mappers

Deberemos utilizar un mapper diferente para cada entrada

    (a) Mapper-a (CNBPTaggedMapper) Obtiene el número de citas por patente y etiqueta cada salida con el string "ncitas"
```txt
            3755824    3858245,3858247. . . → 3755824    ''ncitas'', 9
```

    (b) Mapper-b (PBCMapper) Para cada patente, obtiene el país y etiqueta cada salida con el string "pais" 
```txt
            US   3755824,1973 → 3755824   ''pais'', US
```

''ncitas'' y ''pais'' son simples etiquetas que nos permitirán diferenciar en el reducer si el valor es un número de citas o un pais. La salida de los mappers debe ser, por lo tanto, un valor Writable compuesto similar al usado en la práctica anterior. 

## Reducer

Haremos un join de los dos mappers utilizando como clave el número de patente

```txt
    3755824    ''ncitas'', 9
                                                  →   3755824   US, 9
    3755824    ''pais'', US
```


## Ejecución

### Descarga del JAR

```bash
wget -q -O simplereducesidejoin.jar simplereducesidejoin-0.0.1-SNAPSHOT.jar
```

### Ejecución

#### Paso previo opcional, si no se ha realizado la compilación
```bash
$ wget -q -O simplereducesidejoin.jar https://github.com/Loksly/tcdm_2016/raw/master/practica3/simplereducesidejoin/target/simplereducesidejoin-0.0.1-SNAPSHOT.jar
```

#### Si se ha realizado la compilación es necesario acceder al fichero jar generado
```bash
$ mv target/simplereducesidejoin-0.0.1-SNAPSHOT.jar simplereducesidejoin.jar
```

#### Ejecución

Supuesto que ya se han ejecutado los programas
[citingpatents](https://github.com/Loksly/tcdm_2016/tree/master/practica3/sequence) y
[sequence](https://github.com/Loksly/tcdm_2016/tree/master/practica3/sequence)
con las salidas en los directorios mostrados en sus correspondientes apartados de _ejecución_.

```bash
$ hdfs dfs -rm -r join # sólo si es ejecutado por segunda vez
$ yarn jar simplereducesidejoin.jar citingpatents sequence join
$ hdfs dfs -text join/part-r-00000
```
