#!/bin/bash

wget https://github.com/Loksly/tcdm_2016/raw/master/practica2/target/hdfsutils-0.0.1.jar
java -cp hdfsutils-0.0.1.jar es.loksly.FileSystemCat hdfs://localhost/filesystemcattest