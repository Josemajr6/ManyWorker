## 🔗 Enlaces para trello y swagger
- Trello: https://trello.com/b/YDlNQD1j/manyworker-security
- Swagger: http://localhost:8080/swagger-ui/index.html

## 🔗 Enlaces para pruebas de postman
- Autenticación: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50047569-51024c93-0a73-45b2-a96d-6e360ef75626?action=share&creator=50003188
- Admin: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-b1d891f4-b5ca-4d45-86a8-29519490a994?action=share&creator=50003188
- Categoría: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-66293ee8-4972-4887-99eb-a86fde33a2af?action=share&creator=50003188
- Cliente: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-03533094-0d7b-4f30-9f26-79c5ca83eae5?action=share&creator=50003188
- Mensaje: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-cc5fe77a-5c6c-4c70-a10e-3f5931ff04f0?action=share&creator=50003188
- Perfil social: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-d92653df-381f-46b2-b919-f48c570123a1?action=share&creator=50003188
- Solicitud: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-edbc6ef7-fe4d-4be9-a82a-4a2c50b4feb3?action=share&creator=50003188
- Tarea: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-a173bbba-ec59-468f-9ba7-83b7ba14fb81?action=share&creator=50003188
- Trabajador: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-dfa52a79-b593-453e-ae93-7cd85f5e1ccd?action=share&creator=50003188
- Tutorial: https://juke15yt-2853671.postman.co/workspace/ManyWorker's-Workspace~49b505d5-0131-4ada-8939-ec739b3315bb/collection/50003188-605c9d20-d120-4b98-9bd8-997f41b06d25?action=share&creator=50003188

## 🧠 Cambios entre `Spring-Security` y `main`
### 🔐 Seguridad y autenticación
- Modificado el Actor para implementar la seguridad.
- Se modificó `ActorService` para implementar seguridad y `JWTUtils`.
- Configuración del filtro JWT y endpoints seguros para roles (`admin`, `trabajador`, `cliente`).
- SecurityConfiguration actualizada con comentarios y delimitaciones de rutas protegidas.

### 🛠 Ajustes y nuevos endpoints
- Añadidos endpoints de `categoría`, `tarea`, `mensaje` y `Tutorial` con protección por rol.
- Endpoints de `Perfil Social` integrados con seguridad y servicios ajustados.
- Añadidos roles y encriptación de contraseñas al registrar `cliente`, `trabajador` y `admin`.
- Correcciones de sintaxis de rutas en `SecurityConfiguration`.

### 📌 Mejoras y correcciones generales
- Corregida duración del token JWT (24h) para pruebas en Postman.
- SolicitudService ajustado para evitar errores de objetos transitorios.
- Actualizada documentación de Swagger y revisada la seguridad de la API (roles renombrados, control de errores 401/403).
