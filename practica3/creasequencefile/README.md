# creasequencefile

* Hacer un programa MapReduce MapOnly (sin reducers) que lea el fichero apat63_99.txt, separe los campos y lo guarde como un fichero Sequence con:
  * clave: el país (Text, sin comillas)
  * valor: una cadena con la patente y el año separados por coma (Text)

* IMPORTANTE:
  * El fichero de entrada no se puede modificar, por lo que nuestro código tiene que ser capaz de manejar la primera línea del fichero que es diferente a las demás.
  * Fijar el número de reducers a 0 (MapOnly job)
* La salida serán varios ficheros part-m-*. Se puede ver el contenido de estos ficheros ejecutando: hsfs dfs -text part-m-*


## Ejecución

Supuesto que ya se han subido los ficheros de datos de prueba y que están en el HDFS en el directorio _datos_.
De no ser así le invito a seguir los pasos descritos en el ejemplo [citingpatents](http.

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica3/datos.tgz
$ yarn jar creasequencefile-0.0.1-SNAPSHOT.jar datos/patentes-mini/apat63_99.txt sequence
```