#!/bin/bash

$HADOOP_PREFIX/sbin/yarn-daemons.sh stop nodemanager
$HADOOP_PREFIX/sbin/hadoop-daemons.sh stop datanode
$HADOOP_PREFIX/sbin/yarn-daemon.sh stop resourcemanager
$HADOOP_PREFIX/sbin/hadoop-daemon.sh stop namenode
