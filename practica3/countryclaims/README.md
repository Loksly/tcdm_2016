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
$ set JAR_HADOOP_STREAMING_UTILITY=`find -name "hadoop-streaming*.jar" `
$ hadoop jar \
	$JAR_HADOOP_STREAMING_UTILITY \
	-input datos/all/apat63_99.txt -output salida/CC \
	-mapper 'python CountryClaimsMapper.py' \
	-reducer 'python CountryClaimsReducer.py' \
	-file CountryClaimsMapper.py \
	-file CountryClaimsReducer.py \
	-numReduceTasks 1
```
