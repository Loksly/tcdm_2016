# Sort secundario


## Objetivo

Partiendo de los ficheros sequence creados en la práctica anterior, obtener un fichero de texto con la siguiente información

país     año -> no de patentes ese año

Tendremos una línea para cada país/año, por ejemplo: 
```txt
		ES   1963 -> 26
		ES   1964 -> 19
		ES   1965 -> 49
		...
```

* IMPORTANTE: La salida debe estar ordenada por países, y para cada país, por año (sort secundario)

Debemos usar una clave compuesta [país, año]:

### Clave compuesta

1. Se ordena por la clave compuesta [país, año] usando (job.setSortComparatorClass)
2. Se particiona por país (job.setPartitionerClass, garantiza que los mismos países van al mismo reducer)
3. Se agrupa por la clave compuesta [país, año] usando (job.setGroupingComparatorClass)


### Código ejemplo:

a. Clave compuesta

```java
public static class PaisAnho implements WritableComparable<PaisAnho> {
		public PaisAnho() {
			set(new Text(), new Text());
		}
		public PaisAnho(Text pais, Text anho) {
			set(pais, anho);
		}
		public void set(Text pais, Text anho) {
			this.pais = pais;
			this.anho = anho;
		}
		public Text getPais() {
			return pais;
		}
		public Text getAnho() {
			return anho;
		}
		@Override
		public void readFields(DataInput in) throws IOException {
			pais.readFields(in);
			anho.readFields(in);
		}
		@Override
		public void write(DataOutput out) throws IOException {
			pais.write(out);
			anho.write(out);
		}
		@Override
		public int compareTo(PaisAnho o) {
			int cmp = pais.compareTo(o.pais);
			if (cmp != 0) {
					return cmp;
			}
			return anho.compareTo(o.anho);
		}
		@Override
		public int hashCode() {
			return pais.hashCode() * 163 + anho.hashCode();
		}
		@Override
		public boolean equals(Object o) {
			if (o instanceof PaisAnho) {
					PaisAnho pa = (PaisAnho) o;
					return pais.equals(pa.pais) && anho.equals(pa.anho);
			}
			return false;
		}
		@Override
		public String toString() {
			return "["+pais+", "+anho+"]";
		}
		private Text pais;
		private Text anho;
}
```

b. Comparador

```java
public static class ClaveCompuestaComparator extends WritableComparator {
	protected ClaveCompuestaComparator() {
			super(PaisAnho.class, true);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		PaisAnho pa1 = (PaisAnho) w1;
		PaisAnho pa2 = (PaisAnho) w2;
		int cmp = pa1.getPais().compareTo(pa2.getPais());
		if (cmp == 0) {
			return pa1.getAnho().compareTo(pa2.getAnho());
		}
		return cmp;
	}
}
```

c. Particionador

```java
public static class PaisPartitioner extends Partitioner<PaisAnho, Text> {
	@Override
	public int getPartition(PaisAnho key, Text value, int numPartitions) {
			int hash = key.getPais().hashCode();
			int partition = hash % numPartitions;
			return partition;
	}
}
```


### Compilación:

```bash
$ wget -q https://github.com/Loksly/tcdm_2016/archive/master.zip
$ unzip master.zip
$ cd tcdm_2016/practica3/sortsecundario/java/
$ mvn package
```

#### Ejecución

#### Paso previo opcional, si no se ha realizado la compilación
```bash
$ wget -q -O sortsecundario.jar https://github.com/Loksly/tcdm_2016/raw/master/practica3/sortsecundario/target/sortsecundario-0.0.1-SNAPSHOT.jar
```

#### Si se ha realizado la compilación es necesario acceder al fichero jar generado
```bash
$ mv target/sortsecundario-0.0.1-SNAPSHOT.jar sortsecundario.jar
```

### Ejecución

Supuesto que ya se han subido los ficheros de datos de prueba y que están en el HDFS en el directorio _datos_.
De no ser así le invito a seguir los pasos descritos en el 
[apartado](https://github.com/Loksly/tcdm_2016/tree/master/practica3#carga-de-datos-de-prueba)
relativo a la carga de datos.

```bash
$ hdfs dfs -rm -r sortsecundario # sólo si es ejecutado por segunda vez
$ yarn jar sortsecundario.jar sequence sortsecundario
$ hdfs dfs -get sortsecundario/part-r-00000
```

