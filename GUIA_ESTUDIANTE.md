# Guía del Estudiante

Este proyecto es una **clínica veterinaria digital** construida como un conjunto de servicios pequeños e independientes.

## 1. La clínica en el mundo real

- **API Gateway = El Recepcionista**
  - Es el único punto de entrada a la clínica.
  - Todos los clientes llegan primero al recepcionista, que escucha qué necesitan y los deriva al médico adecuado.
  - En el proyecto, el API Gateway recibe las solicitudes externas y las envía a los microservicios correctos.

- **Eureka = El Directorio**
  - Es como un libro o un tablero en la recepción que sabe en qué oficina está cada médico.
  - Si un médico se mueve, el directorio se actualiza.
  - En el proyecto, Eureka sabe qué servicios están activos y dónde encontrarlos.

- **Microservicios = Los Médicos Especialistas**
  - Cada médico hace una tarea distinta: uno atiende mascotas, otro saca facturas, otro gestiona clientes.
  - Por ejemplo, el médico de mascotas no atiende facturas.
  - En el proyecto, cada microservicio es una aplicación pequeña y especializada.

- **Bases de datos por servicio = Archivadores privados bajo llave**
  - Cada médico tiene su propio archivador con su información.
  - Está prohibido que un médico revise el archivador de otro.
  - En el proyecto, cada servicio usa su propia base de datos y no comparten datos directamente.

- **Docker = El edificio prefabricado**
  - La clínica está en un edificio que se puede mover fácilmente a otro terreno sin desarmar nada.
  - Así, el mismo sistema funciona en otra computadora o servidor.
  - En el proyecto, Docker empaqueta cada servicio y la base de datos para que todo se inicie junto.

- **Swagger = El catálogo o menú**
  - Es como un folleto o menú que muestra todos los servicios que ofrece la clínica.
  - El evaluador puede verlo de forma interactiva y probarlo.
  - En el proyecto, Swagger documenta todas las rutas y operaciones disponibles.

## 2. ¿Cómo funciona todo junto?

1. El cliente llega y llama al recepcionista (API Gateway).
2. El recepcionista pregunta qué necesita y consulta al directorio (Eureka) para localizar al médico correcto.
3. El paciente se envía al médico especialista adecuado (microservicio).
4. El médico revisa solo su archivador privado (base de datos) y responde.
5. Si el evaluador quiere ver qué hace la clínica, abre el catálogo interactivo (Swagger).

## 3. ¿Por qué es bueno este diseño?

- Permite que cada médico trabaje por separado sin estorbar a los demás.
- Si hay un problema en un servicio, no caen todos los demás.
- La clínica puede moverse de un servidor a otro sin tener que rehacer la instalación.
- El recepcionista y el directorio garantizan que las solicitudes siempre lleguen al lugar correcto.

## 4. Qué presentar en la defensa

- Explica la analogía de la clínica veterinaria para cada componente.
- Muestra cómo Docker hace el sistema portátil.
- Enumera que cada servicio tiene su propio archivador privado.
- Señala que Eureka es el listado de servicios vivos.
- Comenta que Swagger es la guía visual de la API.

> Con esta analogía, el proyecto deja claro que no es una sola aplicación grande, sino una clínica organizada con roles definidos y equipos independientes.
