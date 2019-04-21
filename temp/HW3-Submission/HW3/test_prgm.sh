#!/usr/bin/bash

$HADOOP_HOME/bin/hdfs dfs -rm -r -f /cs455/debug
gradle build
$HADOOP_HOME/bin/hadoop jar build/libs/HW3.jar cs455.hadoop.HW3.SongAnalysisJob /cs455/metadata /cs455/analysis /cs455/debug
$HADOOP_HOME/bin/hdfs dfs -ls /cs455/debug

