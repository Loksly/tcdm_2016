#!/bin/bash

$HADOOP_PREFIX/sbin/mr-jobhistory-daemon.sh stop historyserver
$HADOOP_PREFIX/sbin/hadoop-daemon.sh stop secondarynamenode