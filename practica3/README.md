# Práctica 3: Programación MapReduce
### El contenido de este documento está disponible en https://github.com/Loksly/tcdm_2016/tree/master/practica3

## Introducción

Este repositorio tiene por finalidad la de documentar el desarrollo de la tercera práctica de la asignatura de TCDM.

En esta práctica estudiaremos la programación MapReduce en Hadoop. Los ejemplos están inspirados en el libro
Hadoop in Action, C. Lam, 2011.


## Ficheros de datos:

Los ficheros de datos son:

 * cite75_99.txt -> citas realizadas por patentes a otras patentes
 	* 1 columna: nº de patente que cita
	* 2 columna: nº de patente citada
                 
 * apat63_99.txt -> información sobre las patentes. Algunos campos:
 	* 1 columna: nº de patente
 	* 2 columna: año
	* 5 columna: país
	* 9 columna: nº de reivindicaciones de la patente


### Carga de datos de prueba
```bash
$ wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica3/datos.tgz
$ tar xvfz datos.tgz
$ hdfs dfs -put datos
```


## Apartados obligatorios:

- [x] [citingpatents](https://github.com/Loksly/tcdm_2016/tree/master/practica3/citingpatents)
- [x] [citationnumberbypatent_chained](https://github.com/Loksly/tcdm_2016/tree/master/practica3/citationnumberbypatent_chained)
- [x] [creasequencefile](https://github.com/Loksly/tcdm_2016/tree/master/practica3/creasequencefile)
- [x] [sortsecundario](https://github.com/Loksly/tcdm_2016/tree/master/practica3/sortsecundario)
- [x] [simplereducesidejoin](https://github.com/Loksly/tcdm_2016/tree/master/practica3/simplereducesidejoin)


## Apartados opcionales:

- [x] [countryclaims](https://github.com/Loksly/tcdm_2016/tree/master/practica3/countryclaims)
- [ ] [sortsecundario2](https://github.com/Loksly/tcdm_2016/tree/master/practica3/sortsecundario2)
