#!/bin/bash
wget https://github.com/Loksly/tcdm_2016/raw/master/practica2/split/jar/FileSystemSplit.jar
wget https://github.com/Loksly/tcdm_2016/raw/master/practica2/split/lib/FileSystemSplit.jar
classpath=`hadoop classpath`:/opt/yarn/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.3.jar
java -cp $classpath:FileSystemCat.jar:argparse4j-0.7.0.jar es.loksly.FileSystemSplit -f hdfs://localhost/filesystemcattest -t hdfs://localhost/half -p 50 -s 50 -p
