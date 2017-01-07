/* 
 * SimpleJoin.pig
 */


-- lazy load the CSV file, with two columns named citing and cited

citings = LOAD '$citas' USING PigStorage(',') AS (citing:int, cited:int);

-- group all lines that have the same value for cited column

references = GROUP citings BY cited; 

-- how to obtain a count, source: https://pig.apache.org/docs/r0.7.0/piglatin_ref2.html

cites = FOREACH references GENERATE group, COUNT(citings);

-- load another CSV file

info = LOAD '$info' USING PigStorage(',') AS (patent:int, year:int, int, int, country:chararray);

-- ignore the rest of the fields

yearcountry = FOREACH info GENERATE year, country

-- join both contents using patent fields

joininfo = JOIN cites BY patent, yearcountry BY patent;

-- format the output

patentinfo = FOREACH joininfo GENERATE cites::patent,country,year,ncites;

-- sort the output

orderedinfo = ORDER patentinfo BY year ASC;

-- run and store on $output file

STORE patentinfo INTO '$output' USING PigStorage(',');
