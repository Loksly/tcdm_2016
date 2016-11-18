#!/bin/bash

wget https://github.com/Loksly/tcdm_2016/raw/master/practica2/target/hdfs-0.0.1-SNAPSHOT.jar
java -cp hdfs-0.0.1-SNAPSHOT.jar es.loksly.FileSystemSplit -f filesystemcattest -t half -p 50 -s 50