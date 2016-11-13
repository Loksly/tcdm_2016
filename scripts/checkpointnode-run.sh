#!/bin/bash

$HADOOP_PREFIX/sbin/hadoop-daemon.sh start secondarynamenode
$HADOOP_PREFIX/sbin/mr-jobhistory-daemon.sh start historyserver