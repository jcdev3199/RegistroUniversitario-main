# RegistroUniversitario-main
Crud_estudiante
Gestión de Estudiantes

Esta API REST, construida con Spring Boot, permite realizar operaciones CRUD enfocadas en los métodos PUT, POST y DELETE. Usa Java 17 y funciona en el puerto 8081.

Características
Actualización de Estudiantes (PUT)
 Actualiza la información de un estudiante existente en la base de datos.

Implementación:

Controlador: Se creó un método con @PutMapping para manejar solicitudes PUT, recibiendo el ID en la URL y los datos en JSON.

Servicio: Busca al estudiante por ID, actualiza los atributos, y lo guarda en el repositorio.

Pruebas: Solicitud HTTP PUT en Postman para verificar la operación exitosa.

URL: http://localhost:8081/api/estudiantes/{id}

Creación de Estudiantes (POST)
 Permite registrar nuevos estudiantes con datos proporcionados en formato JSON.

Implementación:

Controlador: Método @PostMapping para manejar solicitudes POST, mapeando automáticamente el cuerpo de la solicitud a un DTO.

Servicio: Convierte el DTO en una entidad y la guarda en el repositorio.

Pruebas: Solicitud HTTP POST en Postman con datos JSON para verificar la creación.

URL: http://localhost:8081/api/estudiantes

Eliminación de Estudiantes (DELETE)
 Elimina un estudiante existente del sistema utilizando su ID como parámetro.

Implementación:

Controlador: Método @DeleteMapping para manejar solicitudes DELETE, recibiendo el ID en la URL.

Servicio: Busca al estudiante por ID, lanza una excepción si no existe, y lo elimina del repositorio.

Pruebas: Solicitudes HTTP DELETE en Postman con ID válido y no válido para comprobar respuestas correctas (204 o 404).

URL: http://localhost:8081/api/estudiantes/{id}

