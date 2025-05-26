# 🧩 Hexagonal User-API - Spring Boot :: Randstad


Aplicación de ejemplo con arquitectura hexagonal (puertos y adaptadores) utilizando Spring Boot 3.4.4, Java 21 y HSQLDB en memoria.   
Exposición de una API RESTful para la creación de usuarios con validaciones, autenticación JWT, documentación Swagger, pruebas unitarias e integración.

API REST que expone endpoints: 

```plaintext

| Método HTTP | Endpoint                                     | Descripción                   |
|-------------|----------------------------------------------|-------------------------------|
|  POST       |http://localhost:8082/api-randstad/users      | Crea un nuevo usuario.        | 
|  GET        |http://localhost:8082/api-randstad/users      | Obtiene todos los usuarios.   | 
|  GET        |http://localhost:8082/api-randstad/users/{id} | Obtiene un usuario por ID.    |  
|  PUT        |http://localhost:8082/api-randstad/users/{id} | Actualiza un usuario existente|  
|  DELETE     |http://localhost:8082/api-randstad/users/{id} | Elimina un usuario por ID.    |

```

## 🚀 Características Destacadas

- Arquitectura hexagonal con enfoque vertical slicing
- API RESTful para gestionar usuarios
- Validaciones con Bean Validation
- Seguridad basada en JWT (HS256)
- Almacenamiento en HSQLDB (modo memoria)
- Documentación de API con Swagger/OpenAPI
- Pruebas unitarias y de integración con JUnit 5 y Mockito
- registro de log por consola y por archivo en la ruta: logs/api-randstad.log


## 🛠️ Tecnologías

- Java 21
- Spring Boot 3.4.4
- Spring Web
- Spring Security
- Spring Validation
- Spring Data JPA
- HSQLDB
- JJWT
- Swagger / Springdoc OpenAPI
- JUnit 5 / Mockito
- Log4j2 con SLF4J


## ▶️ Cómo ejecutar la aplicación
### Prerrequisitos
- Java 21
- Maven 3.9+

### 🔧 Construcción

Abrir una terminal y realizar los siguiente pasos.
* crear carpeta (mkdir randstad)
* git clone https://github.com/alvarovergaracortes/api-randstad.git
* cd randstad


### Ejecución aplicacion local
```bash
mvn spring-boot:run
```
La aplicación se ejecutará en:

📍 http://localhost:8082/api-randstad/users
```

```JS
JSON de ejemplo(request):
   {
      "name": "Juan Rodriguez",
      "email": "juanperez@perez.com",
      "password": "hunter2",
      "phones": [
                  {"number": "1234567", "citycode": "1", "contrycode": "57"}
               ]
   }
```

```JS   
JSON de ejemplo(response. Token recortado):
{
    "id": "f19d0995-864c-4b44-87ea-f3a251d8d98c",
    "name": "Juan Rodriguez",
    "email": "juanperez@perez.com",
    "created": "2025-04-25T18:26:43.595000366",
    "modified": "2025-04-25T18:26:43.595000366",
    "lastLogin": "2025-04-25T18:26:43.595000366",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmMTlkMDk5NS04NjRjLTRiNDQtODdlYS1mM2EyN....",
    "active": true
}
```

##🌐 Pruebas Con Postman
En la carpeta **docs**, existe un export de la pruebas realizadas con postman (**api-randstad-test-users.json**)  
Todos tienes la estructura: http://localhost:8082/api-randstad/users  
Para PUT, GET, DELETE, antes de ejecutarse se debe agregar el token, por eso al ver el header mostrara algo asi:  

key  : Authorization  
value: Bearer TOKEN_ID  

Donde TOKEN_ID, se debe reemplazar por alguno generado.


##🧪 Pruebas unitarias con JUnit + Mcokito
Para ejecutar las pruebas:
  mvn test



##🗃️ Base de datos en memoria (HSQLDB)
Se inicializa automáticamente con un script SQL al arrancar la app.

El script de base de datos esta en la siguiente ruta:  
**src/main/resources/schema.sql**


##📘 Documentación 
   En la carpeta **docs/** estan los siguientes artefactos:  
     - api-randstad.postman_collection.json  
     - createUser.json  
     - DiagramaSolucion.docx  
     - Ejercicio_JAVA_v2.pdf  


**Swagger disponible en:**  
📍 http://localhost:8082/api-randstad/swagger-ui/index.html  
📍 http://localhost:8082/api-randstad/v3/api-docs


## 👤 Autor
  Álvaro Vergara Cortés  
  alvaro.vergara.cl@gmail.com



