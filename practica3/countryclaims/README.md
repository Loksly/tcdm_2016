# Country Claims

## Objetivo

Programa *Python* que obtiene el número medio de reivindicaciones ("claims") de las patentes por países, a partir del fichero _apat63_99.txt_

**Mapper**

* Lee las líneas del fichero y escribe el país (campo 4) y el nº de claims (campo 8).

**Reducer**

* Lee la salida de los mappers (que las ordena por países).
* Para cada país acumula los claims para obtener la media.
* Debe detectar el cambio de país en los datos de entrada.

## Ejecución

### Descarga de los ficheros en el equipo desde el que se lanza el _map-reduce_

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica3/countryclaims/python/CountryClaimsMapper.py
$ wget -q https://github.com/Loksly/tcdm_2016/raw/master/practica3/countryclaims/python/CountryClaimsReducer.py
```


### Ejecución

#### Comprobar que python está en el $PATH.

```bash
$ python --version
```

En realidad lo apropiado sería comprobar que está instalado en todos los nodos y
que en todas las versiones de los nodos worker está instalada la misma versión.

#### Ejecución

```bash
$ JAR_HADOOP_STREAMING_UTILITY=`find ~ -name "hadoop-streaming.jar" `
$ hadoop jar \
	$JAR_HADOOP_STREAMING_UTILITY \
	-input datos/patentes-mini/apat63_99.txt -output salida/CC \
	-mapper 'python CountryClaimsMapper.py' \
	-reducer 'python CountryClaimsReducer.py' \
	-file CountryClaimsMapper.py \
	-file CountryClaimsReducer.py \
	-numReduceTasks 1
```

Nota: Como el programa en sí no es más que un juguete, he supuesto que si el valor leído era vacío en ese campo el valor a tomar en cuenta es 0, y por tanto hace media con 0.

### Test sencillo

A modo de ejemplo, la salida del mapper para España puede calcularse así:
```bash
$ python CountryClaimsMapper.py  < /tcdm_2016/practica3/datos/patentes-mini/apat63_99.txt  |grep ES
ES      0
ES      9
ES      3
ES      6
ES      9
ES      0
ES      17
ES      14
ES      6
ES      3
ES      6
ES      18
ES      5
ES      0
ES      30
ES      3
ES      19
ES      4
```

Que haciendo cálculos:

```txt
(0+ 9+ 3+ 6+ 9+ 0+ 17+ 14+ 6+ 3+ 6+ 18+ 5+ 0+ 30+ 3+ 19+ 4) / 18 = 8.44
```
La salida que muestra el programa es 8.


