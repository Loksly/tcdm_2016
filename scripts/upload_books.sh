#!/bin/bash
#tutorial obtained from: http://azure.microsoft.com/es-es/documentation/articles/hdinsight-upload-data/


azure storage account list
storageaccount=

azure storage account keys list $storageaccount

key=###################


azure storage container list -a $storageaccount -k $key

insight=clustertest

files=`ls libros`
cd libros
for file in $files
do
	azure storage blob upload -a $storageaccount -k $key $file $insight user/loksly/libros/$file
done



1. Create hadoop cluster:
