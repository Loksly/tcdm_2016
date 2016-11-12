#!/bin/bash
#after an ssh loksly@###############-ssh.azurehdinsight.net


loksly@hn0-cluste:~$ wget https://github.com/Loksly/tcdm_2016/archive/master.zip
--2016-11-12 13:36:16--  https://github.com/Loksly/tcdm_2016/archive/master.zip
Resolving github.com (github.com)... 192.30.253.112, 192.30.253.113
Connecting to github.com (github.com)|192.30.253.112|:443... connected.
HTTP request sent, awaiting response... 302 Found
Location: https://codeload.github.com/Loksly/tcdm_2016/zip/master [following]
--2016-11-12 13:36:16--  https://codeload.github.com/Loksly/tcdm_2016/zip/master
Resolving codeload.github.com (codeload.github.com)... 192.30.253.120, 192.30.253.121
Connecting to codeload.github.com (codeload.github.com)|192.30.253.120|:443... connected.
HTTP request sent, awaiting response... 200 OK
Length: unspecified [application/zip]
Saving to: ‘master.zip’

    [ <=>                                                                                                                                                                                                ] 5,769       --.-K/s   in 0s

2016-11-12 13:36:17 (68.6 MB/s) - ‘master.zip’ saved [5769]

loksly@hn0-cluste:~$ unzip master.zip
Archive:  master.zip
023b9d83d9ba0d1f02a96b70d624328a8731a94e
   creating: tcdm_2016-master/
 extracting: tcdm_2016-master/README.md
  inflating: tcdm_2016-master/wordcount-0.0.1-SNAPSHOT.jar
loksly@hn0-cluste:~$ cd tcdm_2016-master/
loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -ls libros
Found 16 items
-rwxrwxrwx   1     441804 2016-11-12 13:47 libros/pg14329.txt.gz
-rwxrwxrwx   1     264123 2016-11-12 13:47 libros/pg1619.txt.gz
-rwxrwxrwx   1     455129 2016-11-12 13:47 libros/pg16625.txt.gz
-rwxrwxrwx   1     939502 2016-11-12 13:48 libros/pg17013.txt.gz
-rwxrwxrwx   1     737367 2016-11-12 13:48 libros/pg17073.txt.gz
-rwxrwxrwx   1     219304 2016-11-12 13:48 libros/pg18005.txt.gz
-rwxrwxrwx   1     813698 2016-11-12 13:48 libros/pg2000.txt.gz
-rwxrwxrwx   1     328494 2016-11-12 13:48 libros/pg24536.txt.gz
-rwxrwxrwx   1     504188 2016-11-12 13:48 libros/pg25640.txt.gz
-rwxrwxrwx   1      38194 2016-11-12 13:48 libros/pg25807.txt.gz
-rwxrwxrwx   1     103986 2016-11-12 13:48 libros/pg32315.txt.gz
-rwxrwxrwx   1     125693 2016-11-12 13:48 libros/pg5201.txt.gz
-rwxrwxrwx   1      82099 2016-11-12 13:48 libros/pg7109.txt.gz
-rwxrwxrwx   1      99685 2016-11-12 13:48 libros/pg8870.txt.gz
-rwxrwxrwx   1      85187 2016-11-12 13:48 libros/pg9980.txt.gz
-rwxrwxrwx   1  330326458 2016-11-12 14:04 libros/random_words.txt.bz2
loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -ls libros
loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -put salidawc.txt salidawc
loksly@hn0-cluste:~/tcdm_2016-master$ hdfs dfs -put salidawcerr.txt salidawc
