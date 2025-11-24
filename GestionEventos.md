Os te propongo una tarea simple pero completa que puede realizarse en aproximadamente una hora. Se estructura en tres niveles de complejidad, de acuerdo con las instrucciones que diste. Los alumnos deberán desarrollar una API REST sencilla utilizando **Spring Boot 3.x**, en la que se enfoca en un modelo básico de datos con un máximo de 3 entidades, sus respectivos controladores, repositorios, y una query extendida en los repositorios.

### Proyecto: **Gestión de Eventos y Participantes**

### Descripción:
La aplicación será un sistema básico de gestión de eventos y participantes. Los usuarios podrán crear eventos, añadir participantes a dichos eventos y consultarlos. Los estudiantes deben generar un diagrama E/R a partir del cual desarrollarán las entidades en el proyecto.

---

### Nivel 1: **Tres entidades (10 puntos)**

1. **Entidades**:
   - **Evento**: Representa un evento al cual se pueden inscribir participantes.
   - **Participante**: Representa una persona que asiste a un evento.
   - **Organizador**: Representa a la persona que organiza el evento.

2. **Diagrama E/R**:
   Los estudiantes deberán crear un diagrama E/R simple que relacione las tres entidades:
   - Un **Evento** tiene un **Organizador**.
   - Un **Evento** tiene varios **Participantes** (Relación de uno a muchos).
   - Un **Organizador** puede estar asociado a varios **Eventos** (Relación de uno a muchos).

3. **Tareas**:
   - Crear las 3 entidades mencionadas (Evento, Participante, Organizador) con relaciones adecuadas.
   - Crear los controladores REST para cada entidad.
   - Crear los repositorios extendiendo **JpaRepository** para las 3 entidades.
     - Añadir una consulta adicional en cada repositorio. Ejemplos:
       - Buscar eventos por título.
       - Buscar participantes por email.
       - Buscar organizadores por nombre.

4. **Pruebas**:
   - Probar los endpoints con un cliente REST (Postman, Insomnia, etc.) realizando peticiones CRUD (Crear, Leer, Actualizar, Eliminar).
   - Pruebas adicionales con las queries personalizadas.

---

### Nivel 2: **Dos entidades (8 puntos)**

1. **Entidades**:
   - **Evento**
   - **Participante**

2. **Diagrama E/R**:
   - Un **Evento** tiene varios **Participantes** (Relación de uno a muchos).

3. **Tareas**:
   - Crear las 2 entidades mencionadas (Evento, Participante) con relaciones adecuadas.
   - Crear los controladores REST para las 2 entidades.
   - Crear los repositorios extendiendo **JpaRepository** para ambas entidades.
     - Añadir una consulta adicional. Ejemplos:
       - Buscar eventos por título.
       - Buscar participantes por email.

4. **Pruebas**:
   - Probar los endpoints con un cliente REST realizando peticiones CRUD.
   - Probar las queries adicionales.

---

### Nivel 3: **Una entidad (6 puntos)**

1. **Entidad**:
   - **Evento**

2. **Diagrama E/R**:
   - No es necesario. Solo una entidad simple.

3. **Tareas**:
   - Crear la entidad **Evento**.
   - Crear el controlador REST para la entidad.
   - Crear el repositorio extendiendo **JpaRepository** para **Evento**.
     - Añadir una consulta adicional (buscar eventos por título).

4. **Pruebas**:
   - Probar los endpoints con un cliente REST realizando peticiones CRUD.
   - Probar la query adicional.

---



### Fichero de pistas: GestionEventosPistas.md
"El código ofrecido en este documento se proporciona como pistas o guía para la implementación de la API REST. Los estudiantes que consulten directamente este código en lugar de intentar resolver los desafíos por sí mismos verán una reducción mínima en su calificación, ya que se evalúa la capacidad de aplicar los conceptos aprendidos."
Está en una rama diferente.


## RA y CE
La tarea propuesta de crear una API REST sencilla con **Spring Boot** tocaría los siguientes **Resultados de Aprendizaje (RA)** y sus correspondientes **criterios de evaluación**:

Los más relevantes y que más se tratan en esta tarea son el **RA5** y el **RA6**

### 1. **RA 1: Selecciona las arquitecturas y tecnologías de programación Web en entorno servidor, analizando sus capacidades y características propias.**
   - **Criterio e)**: Se han identificado y caracterizado los principales lenguajes y tecnologías relacionados con la programación Web en entorno servidor.
   - **Criterio g)**: Se han reconocido y evaluado las herramientas de programación en entorno servidor.

### 2. **RA 2: Escribe sentencias ejecutables por un servidor Web reconociendo y aplicando procedimientos de integración del código en lenguajes de marcas.**
   - **Criterio b)**: Se han identificado las principales tecnologías asociadas.
   - **Criterio e)**: Se han escrito sentencias simples y se han comprobado sus efectos en el documento resultante.

### 3. **RA 5: Desarrolla aplicaciones Web identificando y aplicando mecanismos para separar el código de presentación de la lógica de negocio.**
   - **Criterio a)**: Se han identificado las ventajas de separar la lógica de negocio de los aspectos de presentación de la aplicación.
   - **Criterio f)**: Se han escrito aplicaciones Web con mantenimiento de estado y separación de la lógica de negocio.
   - **Criterio g)**: Se han aplicado los principios de la programación orientada a objetos.

### 4. **RA 6: Desarrolla aplicaciones de acceso a almacenes de datos, aplicando medidas para mantener la seguridad y la integridad de la información.**
   - **Criterio a)**: Se han analizado las tecnologías que permiten el acceso mediante programación a la información disponible en almacenes de datos.
   - **Criterio b)**: Se han creado aplicaciones que establezcan conexiones con bases de datos.
   - **Criterio c)**: Se ha recuperado información almacenada en bases de datos.
   - **Criterio d)**: Se ha publicado en aplicaciones Web la información recuperada.
   - **Criterio f)**: Se han creado aplicaciones Web que permitan la actualización y la eliminación de información disponible en una base de datos.

### 5. **RA 7: Desarrolla servicios Web analizando su funcionamiento e implantando la estructura de sus componentes.**
   - **Criterio d)**: Se ha programado un servicio Web.
   - **Criterio f)**: Se ha verificado el funcionamiento del servicio Web.

### 6. **RA 8: Genera páginas Web dinámicas analizando y utilizando tecnologías del servidor Web que añadan código al lenguaje de marcas.**
   - **Criterio a)**: Se han identificado las diferencias entre la ejecución de código en el servidor y en el cliente Web.
   - **Criterio g)**: Se han aplicado estas tecnologías en la programación de aplicaciones Web.

Estos RA y criterios se ajustan al enfoque de la tarea de desarrollo de una API REST con Spring Boot, ya que los estudiantes deberán trabajar tanto con la arquitectura del servidor, como con bases de datos y la separación de lógica de negocio en el backend.
