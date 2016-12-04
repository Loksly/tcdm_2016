hdfs dfs -rm -r salidanodejs
JAR_HADOOP_STREAMING_UTILITY=`find ~ -name "hadoop-streaming.jar" `
export JAR_HADOOP_STREAMING_UTILITY
hadoop jar $JAR_HADOOP_STREAMING_UTILITY -input datos/patentes-mini/apat63_99.txt -output salidanodejs -mapper 'node CountryClaimsMapper.py' -reducer 'node CountryClaimsReducer.js' -file CountryClaimsMapper.js -file CountryClaimsReducer.js -numReduceTasks 1


###TEST:
#node CountryClaimsMapper.js < /tcdm_2016/practica3/datos/patentes-mini/apat63_99.txt | sort| node CountryClaimsReducer.js