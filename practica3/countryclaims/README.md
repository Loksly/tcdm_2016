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
	-input datos/patentes-mini/apat63_99.txt -output salidacc \
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


Otra opción para probar/depurar sin llegar a ejecutarlo mediante hadoop, sino con archivos locales:

```bash
$ python CountryClaimsMapper.py < /tcdm_2016/practica3/datos/patentes-mini/apat63_99.txt | sort | python CountryClaimsReducer.py
```


## Opcional Plus

### Versión en nodejs

Disponible en el directorio [nodejs](https://github.com/Loksly/tcdm_2016/tree/master/practica3/countryclaims/nodejs).

Los requisitos son similares a los de python. Debe estar instalado y accesible en el PATH

```bash
$ hadoop jar \
	$JAR_HADOOP_STREAMING_UTILITY \
	-input datos/patentes-mini/apat63_99.txt -output salidanodejs \
	-mapper 'node CountryClaimsMapper.js' \
	-reducer 'node CountryClaimsReducer.js' \
	-file CountryClaimsMapper.js \
	-file CountryClaimsReducer.js \
	-numReduceTasks 1
```


Se puede comprobar que ambas salidas son las mismas:

```bash
$ python CountryClaimsMapper.py < /tcdm_2016/practica3/datos/patentes-mini/apat63_99.txt | sort | python CountryClaimsReducer.py
AR      10
AT      7
AU      10
BE      10
BG      15
BR      10
BY      22
CA      11
CH      9
CL      10
CN      10
CO      0
CS      3
CU      4
CZ      11
DE      9
DK      9
DZ      0
ES      8
ET      0
FI      9
FR      9
GB      9
GR      14
HK      7
HR      18
HU      6
IE      12
IL      13
IN      7
IT      8
JP      10
KR      7
KW      34
LU      8
MA      0
MC      1
MX      9
MY      19
NL      9
NO      10
NZ      19
PE      9
PH      2
PL      5
RU      12
SA      16
SE      9
SG      11
SI      4
SM      0
SU      5
TW      4
US      11
VE      15
YU      12
ZA      8

$ node CountryClaimsMapper.js < /tcdm_2016/practica3/datos/patentes-mini/apat63_99.txt | sort | node CountryClaimsReducer.js
AR      10
AT      7
AU      10
BE      10
BG      15
BR      10
BY      22
CA      11
CH      9
CL      10
CN      10
CO      0
CS      3
CU      4
CZ      11
DE      9
DK      9
DZ      0
ES      8
ET      0
FI      9
FR      9
GB      9
GR      14
HK      7
HR      18
HU      6
IE      12
IL      13
IN      7
IT      8
JP      10
KR      7
KW      34
LU      8
MA      0
MC      1
MX      9
MY      19
NL      9
NO      10
NZ      19
PE      9
PH      2
PL      5
RU      12
SA      16
SE      9
SG      11
SI      4
SM      0
SU      5
TW      4
US      11
VE      15
YU      12
ZA      8

```
