#!/bin/bash

$HADOOP_PREFIX/sbin/hadoop-daemon.sh start namenode
$HADOOP_PREFIX/sbin/yarn-daemon.sh start resourcemanager
$HADOOP_PREFIX/sbin/hadoop-daemons.sh start datanode
$HADOOP_PREFIX/sbin/yarn-daemons.sh start nodemanager