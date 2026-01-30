  # Carga ETL de Eventos Políticos (Scala + Doobie)

Este proyecto es una aplicación de **ETL (Extract, Transform, Load)** desarrollada en Scala utilizando el paradigma de Programación Funcional pura. 

El sistema lee un dataset de eventos políticos desde un archivo CSV y lo carga de manera transaccional en una base de datos MySQL, asegurando la integridad de los datos y el manejo seguro de recursos.

## Tecnologías Utilizadas

* **Lenguaje:** Scala
* **Efectos:** Cats Effect 3 (IO Monad)
* **Base de Datos:** Doobie (JDBC funcional)
* **Motor de BD:** MySQL
* **Herramienta de Construcción:** sbt

## Características Principales

1.  **Enfoque Puramente Funcional:** Todo el manejo de entrada/salida (lectura de archivo, conexión a BD) está encapsulado en el tipo `IO` para controlar los efectos secundarios.
2.  **Inicialización Automática:** La aplicación detecta si la base de datos `elecciones_db` o la tabla `eventos_politicos` no existen y las crea automáticamente al arrancar.
3.  **Transacciones Atómicas:** La inserción de datos se realiza bajo una transacción segura; si falla un registro, se mantiene la consistencia.
4.  **Parsing Robusto:** Conversión segura de tipos de datos (Strings a Int/Boolean) manejando posibles errores en el CSV original.

## Requisitos Previos

* JDK 11 o superior.
* sbt (Scala Build Tool).
* MySQL Server corriendo en `localhost:3306`.

## Configuración y Ejecución

### 1. Clonar el repositorio
```bash
git clone [https://github.com/Zanty11/ExamenParcial2Bimestre2.git](https://github.com/Zanty11/ExamenParcial2Bimestre2.git)
cd ExamenParcial2Bimestre2
```

2. Configurar Base de Datos
El proyecto está configurado para conectarse con las siguientes credenciales por defecto (ver ExamenFuncional.scala):

User: root

Password: password (Asegúrate de actualizar esto en el código si tu MySQL tiene otra clave).

3. Ejecutar la aplicación
Desde la terminal en la raíz del proyecto:

```
Bash
sbt run
```

---

### Prompt 



> estoy trabajando en un proyecto académico para realizar una migración de datos (ETL) usando Scala. El objetivo es procesar un archivo `politica.csv` que contiene registros electorales y persistirlos en una base de datos MySQL local
>
> Quiero asegurarme de seguir buenas prácticas de Programación Funcional. Mi idea es utilizar **Cats Effect** para manejar la concurrencia y los efectos de IO, y **Doobie** para la interacción con la base de datos, ya que quiero evitar el uso de JDBC tradicional imperativo
>
> ¿Podrías orientarme sobre la arquitectura correcta para este caso? Específicamente tengo estas dudas:
>
> ¿Cómo debería definir mi `case class` para mapear columnas que pueden ser texto, enteros o booleanos (como "True"/"False" en string)?
> He tenido problemas antes cuando la base de datos no existe. ¿Cómo configuro el `Transactor` de Doobie para que cree la base de datos automáticamente al iniciar la conexión?
> ¿Cuál es la forma más eficiente ("idiomática") en Doobie para leer la lista de objetos del CSV e insertarlos todos en una única transacción usando `.traverse`?
>
> Agradecería un ejemplo de estructura de código que integre estos puntos, mostrando cómo separar la lógica de creación de tablas de la lógica de inserción.
>
> <img width="1337" height="918" alt="image" src="https://github.com/user-attachments/assets/f95447e7-45f2-4acb-9549-a62b31c15033" />
