/* 
 * CitationNumberByPatent.pig
 * Given data/cite75_99.txt as an input file,
 * this script computes the number of references of each patent.
 */


-- lazy load the CSV file, with two columns named citing and cited

citings = LOAD 'datos/patentes-mini/cite75_99.txt' USING PigStorage(',') AS (citing:int, cited:int);

-- group all lines that have the same value for cited column

references = GROUP citings BY cited; 

-- how to obtain a count, source: https://pig.apache.org/docs/r0.7.0/piglatin_ref2.html

citas = FOREACH references GENERATE group, COUNT(citings);

-- run the map-reduce jobs and store the result on pig_output directory

STORE citas INTO 'pig_output' USING PigStorage(',');
