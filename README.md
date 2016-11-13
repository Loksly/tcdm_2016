# TCDM
### https://github.com/Loksly/tcdm_2016


## Introducción

Este repositorio tiene por finalidad la de documentar el desarrollo de la primera práctica de la asignatura de [TCDM](http://persoal.citius.usc.es/tf.pena/TCDM/P1/).

Pasos llevados a cabo:


## Parte obligatoria:

- [x] 1. Instalar manualmente un cluster Hadoop v2 con máquinas virtuales en Azure
- [x] 2. Ejecutar el programa WordCount en Java en el cluster
- [x] 3. Ejecutar benchmarks para HDFS y MapReduce en el cluster
- [x] 4. Añadir y retirar nodos del cluster Hadoop
- [ ] 5. Hacer que el cluster sea rack-aware


## Parte opcional:

- [ ] 1. Despliegue de un cluster usando Apache Ambari
- [x] 2. Ejecución del WordCount en un cluster Azure HDinsight



### 1. Instalar manualmente un cluster Hadoop v2 con máquinas virtuales en Azure:

Teniendo *nodejs* y *npm* instalado, instalar suite [azure](https://www.npmjs.com/package/azure):

```bash
npm install -g azure
azure login
```

Después se deben seguir los pasos para obtener la autenticación que permite vincular la suite con el usuario.

Una vez concluido ese proceso, a continuación, se deben indicar que se va a hacer uso de los servicios Compute, Storage y Network, mediante los comandos:


```bash
azure provider register Microsoft.Compute
azure provider register Microsoft.Storage
azure provider register Microsoft.Network
```

Crear un _Resource Group_ que se ejecutará en la zona del Oeste de Europa:


```bash
azure group create BaseInst -l westeurope
```

Ahora vamos a crear una plantilla de máquina virtual que luego replicaremos varias veces para el despliegue de todos los nodos necesarios, para ello es necesario
elegir un nombre de usuario y una contraseña segura (que tiene que tener al menos 8 caracteres, conteniendo al menos una minúscula, una mayúscula, un número y un signo de entre  !@#$%^&+=).
Para el tipo de máquina virtual vamos a elegir _Standard_D1_v2_ pero es posible ver más alternativas en la web de [Azure](https://azure.microsoft.com/es-es/documentation/articles/virtual-machines-windows-sizes/)

```bash
nombreusuario=el_que_se_quiera
pass=passwordseguro
tipovm=Standard_D1_v2
azure vm quick-create -g BaseInst -n HadoopBase -y Linux -Q Debian -u $nombreusuario -p $pass  -z Standard_D1_v2 -l westeurope
```

Al terminar se muestra un informe que incluye la IP de la nueva máquina virtual (campo _Network Profile/Network Interfaces/Network Interface #1/Location/Public IP Address_),
aunque también es posible acceder al mismo desde [el panel de control de Azure](https://portal.azure.com).
Lo siguiente será descargarse el script que realiza la instalación de *Hadoop*, para ello realiza un ssh a la máquina recién creada y utiliza la contraseña seleccionada.

```bash
wget https://github.com/Loksly/tcdm_2016/raw/master/scripts/hadoop_install.sh
sudo bash
bash hadoop_install.sh

su - hdmaster
wget https://github.com/Loksly/tcdm_2016/raw/master/scripts/hdmaster_config.sh
bash hdmaster_config.sh
hadoop version #debe mostrar la versión de Hadoop, si no es así repasar este último paso.
```

Ahora hay que configurar hadoop como usuario *hdmaster*:

```bash
wget https://github.com/Loksly/tcdm_2016/raw/master/scripts/hadoop_config.sh
bash hadoop_config.sh
```

Otros ficheros de configuración son los ficheros $HADOOP_PREFIX/etc/hadoop/*-env.sh. Hay que modificar los siguientes:

* hadoop-env.sh:
> JAVA_HOME: definidlo como /usr/lib/jvm/java-8-openjdk-amd64

> HADOOP_LOG_DIR: directorio donde se guardan los logs de hdfs. Definidlo como /var/log/hadoop/hdfs

* yarn-env.sh
> JAVA_HOME: definidlo como /usr/lib/jvm/java-8-openjdk-amd64

> YARN_LOG_DIR: directorio donde se guardan los logs de YARN. Definidlo como /var/log/hadoop/yarn

* mapred-env.sh
> JAVA_HOME: definidlo como /usr/lib/jvm/java-8-openjdk-and64

> HADOOP_MAPRED_LOG_DIR: directorio donde se guardan los logs de MapReduce. Definidlo como /var/log/hadoop/mapred 



### Crear las VMs

#### Obtener la imagen lista para la replicación:

Dentro de la máquina de Azure, ejecuta como administrador:
```bash
waagent -deprovision+user
```
Desconectate de la máquina de Azure, ejecutando logout (o CTRL-D) las veces que sean necesarias
```bash
Deallocate la máquina de Azure ejecutando:
azure vm deallocate -g BaseInst -n HadoopBase
```

Generaliza esta máquina para poder lanzar otras iguales a ella

```bash
azure vm generalize -g BaseInst -n HadoopBase
```

Captura la imagen y una plantilla local para lanzar las nuevas máquinas
```bash
azure vm capture -g BaseInst -n HadoopBase -p TCDM1617 -t imagenbase-template.json
```


## Arrancar las VMs en cualquier momento o pararlas:

En el ordenador local están disponibles dos scripts:
* Para arrancar: https://github.com/Loksly/tcdm_2016/raw/master/scripts/wakeup.sh
* Para parar: https://github.com/Loksly/tcdm_2016/raw/master/scripts/sleeps.sh

## Arrancar Yarn y los data nodes.

En el equipo NameNode, nos conectamos con un ```ssh usuario@namenode```, y después cambiamos de usuario con:
```bash
sudo su - hdmaster
cd
wget "https://github.com/Loksly/tcdm_2016/blob/master/scripts/namenode-run.sh"
wget "https://github.com/Loksly/tcdm_2016/blob/master/scripts/namenode-stop.sh"
bash namenode-run.sh
#para pararlo: bash namenode-stop.sh
```

Y ahora con el equipo checkpointnode, nos conectamos con un ```ssh usuario@checkpointnode```, y después cambiamos de usuario con:
```bash
sudo su - hdmaster
cd
wget "https://github.com/Loksly/tcdm_2016/blob/master/scripts/checkpointnode-run.sh"
wget "https://github.com/Loksly/tcdm_2016/blob/master/scripts/checkpointnode-stop.sh"
bash checkpointnode-run.sh
#para pararlo: bash checkpointnode-stop.sh
```


![Estado de las máquinas arrancadas](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/nodos_encendidos.png)

Las capturas de las interfaces web del HDFS, YARN, CheckPoint Node y JobHistory están disponibles en:
* [HDFS](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/hdfs.png)
* [YARN](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/yarn_QuasiMonteCarlo.pdf)
* [JobHistory](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/jobhistory.png)
* [CheckpointNode](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/checkpointnode.png)



Para ejecutar la aplicación, es necesario descargar el fichero de Mega, pero en vez de descargarlo en local y luego subirlo,
es más fácil instalar curl (mediante un sudo apt-get install curl -y)
y luego ejecutar el comando, como usuario normal:
```bash
wget -O mega.sh "http://pastebin.com/raw/JNZ0VUpi"
bash mega.sh 'https://mega.nz/#!T4lhjDqI!7qJXHdkffuZrrbZIgYfaeiwMnI53PEnlO-Wo_qgTbt4' libros.tar.gz
tar xvf AERqiL7XHKcX3EXBwMTmXgTmMPZX
hdfs dfs -put libros .
wget "https://github.com/Loksly/tcdm_2016/blob/master/wordcount-0.0.1-SNAPSHOT.jar"
yarn jar wordcount-0.0.1-SNAPSHOT.jar libros salidawc
```

Una vez terminado el fichero de salida estará disponible en el directorio salidawc de HDF, con nombre *part-r-00000*, podemos ver aquí un pequeño extracto del mismo:
```txt
a	45639
aa	1038
aachen	965
aaliyah	2063
aardvark	2056
aardvarks	992
aaron	1005
ab	1019
aba	8
abaci	1032
aback	1049
abacus	2031
abacuses	989
abad	70
abade	2
abadejo	4
abades	11
abadesa	33
abadesas	1
abadía	7
abaft	990
abaja	1
abajada	1
abajan	1
abajar	7
```

### Ejecutar los benchmarks

Para ejecutar los benchmarks tan sólo es necesario, como usuario hdmaster:
```bash
wget "https://github.com/Loksly/tcdm_2016/blob/master/scripts/run_benchmarks.bash" #descargar el script 
bash run_benchmarks.bash
```

Como prueba de la ejecución de los benchmark podemos utilizar esta captura de pantalla:
![JobHistory](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/jobhistory.png)




### Rack Awareness


Hay que crear un fichero _/opt/yarn/hadoop/etc/hadoop/topology.data_ con este contenido:
```bash
10.0.0.6     /rack1
10.0.0.7     /rack1
10.0.0.9     /rack2
10.0.0.10    /rack2
datanode1    /rack1
datanode2    /rack1
datanode4    /rack2
datanode5    /rack2
```

Descargar el fichero topology.script
```bash
cd /opt/yarn/hadoop/etc/hadoop/
wget "https://github.com/Loksly/tcdm_2016/blob/master/scripts/topology.script"
chmod +x topology.script
```

y modificar el fichero core.xml, para añadirle esta propiedad:
```xml
<property>
	<name>net.topology.script.file.name</name>
	<value>/opt/yarn/hadoop/etc/hadoop/topology.script</value>
	<final>true</final>
</property>
```

Al hacerlo reiniciamos los demonios con el script stop.sh y el script start.sh,
y podemos ver el resultado ejecutando:
```bash
hdfs dfsadmin -printTopology
```
![RackAwareness](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/rackawareness.png)




## Parte opcional

### Ejecución del WordCount en un cluster Azure HDinsight

Para ello es necesario hacer login en el [portal de Azure](https://portal.azure.com) y añadir un nuevo HDInsight y un nuevo almacenamiento.

#### Crear un nuevo espacio de almacenamiento:

Versión por asistente:

Para ello es necesario acceder a https://portal.azure.com/#create/Microsoft.StorageAccount-ARM
y cumplimentar con los siguientes datos:

* Nombre: lokslytest
* Tipo de cuenta: Uso general
* Rendimiento: Estándar
* Cifrado del lado del servidor: Deshabilitado
* Grupo de recursos: almacenamiento
* Ubicación oeste.




Se puede acceder directamente a https://portal.azure.com/#create/Microsoft.HDInsightCluster
o seguir los pasos de la siguiente imagen:
![Pasos Insight 1](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/insight1.png)


Después cumplimentar con los siguientes datos (son modificables/personalizables, pueden elegirse otros):

* Nombre del cluster: lokslytest
* Configuración del cluster:
	* Tipo de cluster: Hadoop
	* Sistema operativo: Linux
	* Versión: 2.7.1
	* Nivel de cluster: Estándar
* Credenciales:
	* Introducir contraseña de administrador  (_recordar este dato para conectarnos al servidor ambari_)
	* Introducir nombre de usuario SSH (_recordar este dato para enviar el trabajo_, en este ejemplo loksly)
	* Introducir contraseña SSH (_recordar este dato para enviar el trabajo_)
* Origen de datos:
	* Método de selección: Desde todas las suscripciones
	* Seleccionar existentes: lokslytest
	* Ubicación (la que ponga por defecto, que será la misma que la del almacenamiento).
* Precios:
	* Número de nodos de trabajador: 2
	* Tamaño del nodo Trabajador: D3 v2 (2 nodos, 8 núcleos)
	* Tamaño del nodo Encabezado: D3 v2 (2 nodos, 8 núcleos)
* Grupo de recursos: insighttest

El proceso dura unos cuantos minutos.

Mientras tanto podemos enviar los ficheros de prueba al almacenamiento.

Para ello debemos seguir los siguientes pasos, ejecutados en la máquina local:

0. El usuario ssh debe almacenarse en la variable $USERSSH,
```bash
USERSSH=loksly
```

1. obtener el listado de almacenamientos asociados a nuestro usuario en Azure.
```bash
azure storage account list
```

2. Seleccionamos la cuenta con la que queremos trabajar y almacenamos su valor en una variable:
```bash
storageaccount=CUENTA_DE_ENTRE_EL_LISTADO_PREVIO
```

3. Obtener la contraseña de acceso a ese almacenamiento
```bash
azure storage account keys list $storageaccount
```

4. Ahora almacenaremos la key en la variable con:
```bash
key=###################
```

5. Supuesto que estamos en el directorio previo al de los libros, procedemos a subir los libros con estos comandos, así:
```bash
files=`ls libros`
cd libros
for file in $files
do
	azure storage blob upload -a $storageaccount -k $key $file $insight user/$USERSSH/libros/$file
done

```


Una vez hemos enviado los ficheros al sistema de almacenamiento y hemos comprobado que ha terminado de crearse el cluster creado
podemos proceder a la ejecución del programa.

Primero podemos ver el ambari que se ha creado accediendo a la dirección https que nos muestra en el panel de control,
en mi caso https://clustertest.azurehdinsight.net, como usuario ponemos admin y como contraseña la del apartado anterior.
![Pasos Insight 1](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/insight_ambari.png)

Ahora vamos a ejecutar el programa, para ello hacemos ssh al servidor, en mi caso loksly@###############-ssh.azurehdinsight.net
y seguimos estos pasos:

```bash
	loksly@hn0-cluste:~$ wget https://github.com/Loksly/tcdm_2016/archive/master.zip
	--2016-11-12 13:36:16--  https://github.com/Loksly/tcdm_2016/archive/master.zip
	Resolving github.com (github.com)... 192.30.253.112, 192.30.253.113
	Connecting to github.com (github.com)|192.30.253.112|:443... connected.
	HTTP request sent, awaiting response... 302 Found
	Location: https://codeload.github.com/Loksly/tcdm_2016/zip/master [following]
	--2016-11-12 13:36:16--  https://codeload.github.com/Loksly/tcdm_2016/zip/master
	Resolving codeload.github.com (codeload.github.com)... 192.30.253.120, 192.30.253.121
	Connecting to codeload.github.com (codeload.github.com)|192.30.253.120|:443... connected.
	HTTP request sent, awaiting response... 200 OK
	Length: unspecified [application/zip]
	Saving to: ‘master.zip’

	    [ <=>                                                                                                                                                                                                ] 5,769       --.-K/s   in 0s

	2016-11-12 13:36:17 (68.6 MB/s) - ‘master.zip’ saved [5769]

	loksly@hn0-cluste:~$ unzip master.zip
	Archive:  master.zip
	023b9d83d9ba0d1f02a96b70d624328a8731a94e
	   creating: tcdm_2016-master/
	 extracting: tcdm_2016-master/README.md
	  inflating: tcdm_2016-master/wordcount-0.0.1-SNAPSHOT.jar
	loksly@hn0-cluste:~$ cd tcdm_2016-master/
	loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -ls libros
	Found 16 items
	-rwxrwxrwx   1     441804 2016-11-12 13:47 libros/pg14329.txt.gz
	-rwxrwxrwx   1     264123 2016-11-12 13:47 libros/pg1619.txt.gz
	-rwxrwxrwx   1     455129 2016-11-12 13:47 libros/pg16625.txt.gz
	-rwxrwxrwx   1     939502 2016-11-12 13:48 libros/pg17013.txt.gz
	-rwxrwxrwx   1     737367 2016-11-12 13:48 libros/pg17073.txt.gz
	-rwxrwxrwx   1     219304 2016-11-12 13:48 libros/pg18005.txt.gz
	-rwxrwxrwx   1     813698 2016-11-12 13:48 libros/pg2000.txt.gz
	-rwxrwxrwx   1     328494 2016-11-12 13:48 libros/pg24536.txt.gz
	-rwxrwxrwx   1     504188 2016-11-12 13:48 libros/pg25640.txt.gz
	-rwxrwxrwx   1      38194 2016-11-12 13:48 libros/pg25807.txt.gz
	-rwxrwxrwx   1     103986 2016-11-12 13:48 libros/pg32315.txt.gz
	-rwxrwxrwx   1     125693 2016-11-12 13:48 libros/pg5201.txt.gz
	-rwxrwxrwx   1      82099 2016-11-12 13:48 libros/pg7109.txt.gz
	-rwxrwxrwx   1      99685 2016-11-12 13:48 libros/pg8870.txt.gz
	-rwxrwxrwx   1      85187 2016-11-12 13:48 libros/pg9980.txt.gz
	-rwxrwxrwx   1  330326458 2016-11-12 14:04 libros/random_words.txt.bz2
	loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -ls libros
	loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -put salidawc.txt salidawc
	loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -put salidawcerr.txt salidawc
```
![Yarn Insight](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/insight_yarn.png)


Desde el panel de control de azure podemos acceder al resultado de la ejecución, tal y como muestra la imagen, dirigiéndonos al apartado relacionado con el almacenamiento.
![Salida Insight](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/salida_insight.png)

Una vez ha concluido la ejecución podemos proceder a la eliminación del cluster de Insight, de esta forma no consumirá recursos y el resultado
de su procesamiento ha quedado almacenado fuera del mismo. Para ello podemos irnos al apartado del cluster (en nuestro caso llamado clustertest),
y pulsar sobre el botón de *eliminar* situado en la parte superior derecha.

![Eliminar Insight](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/eliminar_insight.png)

Este proceso dura un rato, pero podemos ver que sigue procediendo a su eliminación por el mensaje de progreso situado en la parte superior derecha.
![Eliminar Insight](https://github.com/Loksly/tcdm_2016/blob/master/screen_captures/eliminando_insight.png)




## Copyright information

Copyright disclaimer: There are parts of this code that belongs to it's original author.
http://persoal.citius.usc.es/tf.pena/TCDM/P1/instalacin_manual_de_un_cluster.html
