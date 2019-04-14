#!/usr/bin/bash

$HADOOP_HOME/bin/hdfs dfs -rm -r -f /home/cs455/msd-analysis
gradle build
$HADOOP_HOME/bin/hadoop jar build/libs/HW3.jar cs455.hadoop.HW3.SongAnalysisJob /data/metadata /data/analysis /home/cs455/msd-analysis
$HADOOP_HOME/bin/hdfs dfs -ls /home/cs455/msd-analysis

