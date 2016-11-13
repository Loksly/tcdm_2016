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
Para el tipo de máquina virtual vamos a elegir _Standard_D1_v2_ pero es posible ver más alternativas en la web de [azure](https://azure.microsoft.com/es-es/documentation/articles/virtual-machines-windows-sizes/)

```bash
nombreusuario=el_que_se_quiera
pass=passwordseguro
tipovm=Standard_D1_v2
azure vm quick-create -g BaseInst -n HadoopBase -y Linux -Q Debian -u $nombreusuario -p $pass  -z Standard_D1_v2 -l westeurope
```

Al terminar se muestra un informe que incluye la IP de la nueva máquina virtual, aunque también es posible acceder al mismo desde [el panel de control de azure](https://portal.azure.com).
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


## Copyright information

Copyright disclaimer: There are parts of this code that belongs to it's original author.
http://persoal.citius.usc.es/tf.pena/TCDM/P1/instalacin_manual_de_un_cluster.html
