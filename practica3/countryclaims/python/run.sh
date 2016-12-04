hdfs dfs -rm -r salida
JAR_HADOOP_STREAMING_UTILITY=`find ~ -name "hadoop-streaming.jar" `
export JAR_HADOOP_STREAMING_UTILITY
hadoop jar $JAR_HADOOP_STREAMING_UTILITY -input datos/patentes-mini/apat63_99.txt -output salida -mapper 'python CountryClaimsMapper.py' -reducer 'python CountryClaimsReducer.py' -file CountryClaimsMapper.py -file CountryClaimsReducer.py -numReduceTasks 1