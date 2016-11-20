#!/bin/bash
wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica2/split/jar/FileSystemSplit.jar
wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica2/split/lib/argparse4j-0.7.0.jar
classpath=`hadoop classpath`:/opt/yarn/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.3.jar
java -cp $classpath:FileSystemSplit.jar:argparse4j-0.7.0.jar es.loksly.FileSystemSplit -f hdfs:///tmp/filesystemcattest -t hdfs:///tmp/half -p 50 -s 50 -p
