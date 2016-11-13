export YARN_EXAMPLES=$HADOOP_PREFIX/share/hadoop/mapreduce
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-client-jobclient-*-tests.jar
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-examples-*.jar 