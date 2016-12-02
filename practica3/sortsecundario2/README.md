## Objetivo

Partiendo de los ficheros sequence creados en la práctica 2, obtener un fichero de texto con la siguiente información, en el formato indicado:
```txt
  * país   año -> no de patentes ese año; año -> no de patentes ese año; ....
```

Es decir, deberemos tener una línea para cada país, con todos los años a continuación. Por ejemplo: 

```txt
   ES      1963->26;1964->19;1965->49;...
```

Se debe mantener el orden por países, y para cada país, por año (sort secundario).

* Usamos la misma una clave compuesta [país, año], pero agrupamos por país.
* Se ordena por la clave compuesta [país, año].
* Se particiona por país (garantiza que los mismos países van al mismo reducer).
* Se agrupa por país (el reduce obtiene una lista con todos los valores asociados a un país).


## Trabajo adicional 1

- [ ] Usando el fichero country_codes.txt, modificar el programa anterior para para que muestre el nombre de los países en vez del código.
- [ ] Distribuir el fichero country_codes.txt usando la opción -files en el momento de ejecutar el código.
- [ ] En el Reducer, sobrescribir el método setup para que cargue el fichero y cree un mapa con los códigos y el nombre de los países
- [ ] Al fichero se accede con: File('country_codes.txt')

## Trabajo adicional 2

Modificar el programa para incluir contadores que nos indiquen el número de países que tienen patentes en menos de 5 años y en 5 o más
Definir un enum como este:

```java
   enum PaisesConPatentes {
      MENOS_DE_5,
      MAS_DE_5
   }
```

En el Reducer, contar el número de años en los que el país tiene patentes e incrementar el contador correspondiente:

```java
   if(counter < 5) {
      context.getCounter(PaisesConPatentes.MENOS_DE_5).increment(1);
   } 
   else {
      context.getCounter(PaisesConPatentes.MAS_DE_5).increment(1);
   }
```

## Trabajo adicional 3

- [ ] Crear test unitarios usando MRUnit para las classes Map y Reduce del programa.