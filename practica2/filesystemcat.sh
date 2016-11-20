#!/bin/bash
wget https://github.com/Loksly/tcdm_2016/raw/master/practica2/cat/jar/FileSystemCat.jar
classpath=`hadoop classpath`:/opt/yarn/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.3.jar
hdfs -cp $classpath:FileSystemCat.jar es.loksly.FileSystemCat hdfs://localhost/filesystemcattest