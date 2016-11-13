#!/bin/bash

wget https://github.com/Loksly/tcdm_2016/raw/master/hadoop/xmls/core-site.xml -O $HADOOP_PREFIX/etc/hadoop/core-site.xml
wget https://github.com/Loksly/tcdm_2016/raw/master/hadoop/xmls/hdfs-site.xml -O $HADOOP_PREFIX/etc/hadoop/hdfs-site.xml
wget https://github.com/Loksly/tcdm_2016/raw/master/hadoop/xmls/mapred-site.xml -O $HADOOP_PREFIX/etc/hadoop/mapred-site.xml
wget https://github.com/Loksly/tcdm_2016/raw/master/hadoop/xmls/yarn-site.xml -O $HADOOP_PREFIX/etc/hadoop/yarn-site.xml
