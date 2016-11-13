#!/bin/bash

# Copyright disclaimer: There are parts of this code that belongs to it's original author.
# http://persoal.citius.usc.es/tf.pena/TCDM/P1/instalacin_manual_de_un_cluster.html

apt-get update
apt-get install openjdk-8-jre libssl-dev

#Descarga la última versión estable de Hadoop en /opt/yarn (en el momento de escribir este documento era la 2.7.3), crea un enlace simbólico y define la variable HADOOP_PREFIX.

mkdir /opt/yarn
cd /opt/yarn
wget http://mirrors.gigenet.com/apache/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz
tar xvzf hadoop-2.7.3.tar.gz
rm hadoop-2.7.3.tar.gz
ln -s hadoop-2.7.3 hadoop
export HADOOP_PREFIX=/opt/yarn/hadoop

#Crea un grupo hadoop y un usuario hdmaster para ejecutar los diferentes demonios (HDFS y YARN). Cambia el propietario del directorio de hadoop

groupadd -r hadoop
useradd -r -g hadoop -d /opt/yarn -s /bin/bash hdmaster
chown -R hdmaster:hadoop /opt/yarn  

# Crea directorios para los datos de HDFS (NameNode, DataNodes y Checkpoint node) y haz que sean propiedad del usuario hdmaster. En un sistema real, estos directorios deberían estar en particiones separadas con suficiente espacio libre.

mkdir -p /var/data/hadoop/hdfs/nn
mkdir -p /var/data/hadoop/hdfs/cpn
mkdir -p /var/data/hadoop/hdfs/dn
chown -R hdmaster:hadoop /var/data/hadoop/hdfs

# Crea directorios para los ficheros de log y haz que sean propiedad del usuario hdmaster.

mkdir -p /var/log/hadoop/yarn
mkdir -p /var/log/hadoop/hdfs
mkdir -p /var/log/hadoop/mapred
chown -R hdmaster:hadoop /var/log/hadoop

echo "10.0.0.4        namenode resourcemanager" >> /etc/hosts
echo "10.0.0.5        checkpointnode jobhistoryserver" >> /etc/hosts
echo "10.0.0.6        datanode1" >> /etc/hosts
echo "10.0.0.7        datanode2" >> /etc/hosts
echo "10.0.0.8        datanode3" >> /etc/hosts
echo "10.0.0.9        datanode4" >> /etc/hosts
echo "10.0.0.10       datanode5" >> /etc/hosts


#Ahora sólo queda modificar el fichero /etc/ssh/ssh_config y poner el parámetro StrictHostKeyChecking a no.

grep -v StrictHostKeyChecking /etc/ssh/ssh_config  > /tmp/ssh_config
mv /tmp/ssh_config /etc/ssh/ssh_config
echo "StrictHostKeyChecking no" >>/etc/ssh/ssh_config
