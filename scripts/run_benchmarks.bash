export YARN_EXAMPLES=$HADOOP_PREFIX/share/hadoop/mapreduce
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-client-jobclient-*-tests.jar
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-examples-*.jar 
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-client-jobclient-*-tests.jar TestDFSIO -write -nrFiles 10 -fileSize 100
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-client-jobclient-*-tests.jar TestDFSIO -read -nrFiles 10 -fileSize 100
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-client-jobclient-*-tests.jar TestDFSIO -clean #comentar para dejar la salida
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-examples-*.jar teragen 10000000 terasortin
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-examples-*.jar terasort terasortin terasortout
yarn jar $YARN_EXAMPLES/hadoop-mapreduce-examples-*.jar teravalidate terasortout teravalout