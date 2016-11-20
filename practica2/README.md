
# Práctica 2: Hadoop - Uso de HDFS
### El contenido de este documento está disponible en https://github.com/Loksly/tcdm_2016/practica2

## Introducción

Este repositorio tiene por finalidad la de documentar el desarrollo de la segunda práctica de la asignatura de TCDM.

En esta práctica estudiaremos el acceso programático a HDFS.

## Parte obligatoria

### Uso del código FileSystemCat.

- [x] Crea un fichero de texto en el namenode (que no sea demasiado pequeño) y cópialo al HDFS, con hdfs dfs -put (puedes usar, por ejemplo, un fichero de la salida del wordcount).

```bash
#Fichero de 14MB
curl -s http://loripsum.net/api/10000/verylong/plaintext | hdfs dfs -put - filesystemcattest

#Fichero de 2GB
curl -s http://loripsum.net/api/10000/verylong/plaintext | xargs yes | head -n 100000 | hdfs dfs -put - filesystemcattest
```

- [x] Utiliza FileSystemCat para ver el fichero.

```bash
o
bash filesystemcat.sh

```
- [x] Basándote en el código anterior, escribe un programa que copie la mitad inferior del fichero anterior en el HDFS a otro fichero en otro directorio del HDFS. Usa getFileStatus para obtener la longitud del fichero y seek para saltar a la mitad del mismo.

```bash
wget https://github.com/Loksly/tcdm_2016/raw/master/practica2/filesystemsplit.sh
bash filesystemsplit.sh

```

- [x] Probar hdfs dfsadmin y hdfs fsck
```bash
# https://hadoop.apache.org/docs/r1.2.1/commands_manual.html
hdfs dfsadmin -report
hdfs fsck .
hdfs fsck filesystemcattest/plaintext
```

El log de salida sería:

```txt
hdmaster@NameNode:~$ hdfs fsck .
Connecting to namenode via http://namenode:50070/fsck?ugi=hdmaster&path=%2Fuser%2Fhdmaster
FSCK started by hdmaster (auth:SIMPLE) from /10.0.0.4 for path /user/hdmaster at Fri Nov 18 17:07:51 UTC 2016
.........Status: HEALTHY
 Total size:    2014372924 B
 Total dirs:    4
 Total files:   9
 Total symlinks:                0
 Total blocks (validated):      33 (avg. block size 61041603 B)
 Minimally replicated blocks:   33 (100.0 %)
 Over-replicated blocks:        0 (0.0 %)
 Under-replicated blocks:       0 (0.0 %)
 Mis-replicated blocks:         0 (0.0 %)
 Default replication factor:    3
 Average block replication:     3.0
 Corrupt blocks:                0
 Missing replicas:              0 (0.0 %)
 Number of data-nodes:          4
 Number of racks:               2
FSCK ended at Fri Nov 18 17:07:51 UTC 2016 in 7 milliseconds


The filesystem under path '/user/hdmaster' is HEALTHY

hdmaster@NameNode:~$ hdfs fsck filesystemcattest
Connecting to namenode via http://namenode:50070/fsck?ugi=hdmaster&path=%2Fuser%2Fhdmaster%2Ffilesystemcattest
FSCK started by hdmaster (auth:SIMPLE) from /10.0.0.4 for path /user/hdmaster/filesystemcattest at Fri Nov 18 17:08:54 UTC 2016
.Status: HEALTHY
 Total size:    14372900 B
 Total dirs:    0
 Total files:   1
 Total symlinks:                0
 Total blocks (validated):      1 (avg. block size 14372900 B)
 Minimally replicated blocks:   1 (100.0 %)
 Over-replicated blocks:        0 (0.0 %)
 Under-replicated blocks:       0 (0.0 %)
 Mis-replicated blocks:         0 (0.0 %)
 Default replication factor:    3
 Average block replication:     3.0
 Corrupt blocks:                0
 Missing replicas:              0 (0.0 %)
 Number of data-nodes:          4
 Number of racks:               2
FSCK ended at Fri Nov 18 17:08:54 UTC 2016 in 1 milliseconds


The filesystem under path '/user/hdmaster/filesystemcattest' is HEALTHY
```


- [x] Crea un directorio en HDFS y ponle una cuota de solo 2 ficheros. Prueba a copiar más de dos ficheros a ese directorio. Haz un chequeo de todo el HDFS.
```bash
echo 'We are going to create a new directory called testdirectory'
hdfs dfs -mkdir testdirectory
hdfs dfsadmin -setQuota 2 testdirectory
echo 'About to copy first file'
echo 'About to copy first file' | hdfs dfs -put /dev/input testdirectory/1
echo 'About to copy second file'
echo 'About to copy second file' | hdfs dfs -put /dev/input testdirectory/2
echo 'Now you can see there are two files'
hdfs dfs -ls /testdirectory
echo 'About to copy third file. This should fail.'
echo 'Copy second file' | hdfs dfs -put /dev/input testdirectory/2
echo 'Now you can see there are two files'
hdfs dfs -ls /testdirectory
hdfs fsck
hdfs dfs -rmr testdirectory
echo 'Now you can see the directory has been deleted with all it's contents'
```

## Parte opcional (puntúa positivamente en la nota final)

- [x] Utiliza los ejemplos de http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/WebHDFS.html para, usando WebHDFS, desde tu PC crear un nuevo directorio en el directorio del usuario no provilegiado (no hdmaster= en HDFS y copiar un fichero de tu PC a ese directorio. El Host_Name es el NameNode y el Port el 50070.

```bash
curl http://loripsum.net/api/10000/verylong/plaintext
curl -i -X PUT "http://namenode:50070/webhdfs/v1/testdirectory2?op=MKDIRS" 
location=`curl -i -X PUT "http://namenode:50070/webhdfs/v1/tmp/testfile?op=CREATE&overwrite=true" 2>/dev/null |grep Location | sed s/'Location: '//`
curl -i -X PUT -T plaintext $location
#then test it has been uploaded
hdfs dfs -cat /tmp/testfile
```

=== Fuentes

http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/WebHDFS.html#Make_a_Directory
http://lecluster.delaurent.com/upload-a-file-with-webhdfs/



Pasos llevados a cabo:

## Parte obligatoria:

- [x] 1. Entregar los códigos desarrollados en los puntos 1 y 2.
Entregar los códigos desarrollados en los puntos 1 y 2. Se deben incluir tanto los ficheros fuente como los class, junto con un fichero README donde se indique cómo ejecutarlos.
Un documento que muestre el efecto de las cuotas y la salida del chequeo. Pueden usarse capturas de pantalla pero deben incluir una breve explicación de lo que aparece en las mismas.

## Parte opcional:

- [x] 1. Documento en el que se indiquen los comandos usados con el WebHDFS para crear directorios y ficheros y el resultado obtenido.


## Opcional plus

- [x] Compilación del ejemplo

```bash

sudo apt-get install git ant openjdk-8-jdk
wget https://github.com/Loksly/tcdm_2016/archive/master.zip
unzip master.zip
cd tcdm_2016/practica2
ant -buildfile ant.xml

mvn clean
mvn package

```
