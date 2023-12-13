# Rescats Backend 🐾
 Este repositorio contiene el código del backend para la aplicación de adopción de gatos.
 
 ## Diagrama de clases
 
![Diagrama en blanco](https://github.com/florencianionquepan/gatos-spring-boot/assets/85314154/253dc0df-6b96-4555-94e9-38868537df0d)

## Funcionalidades 🐱

### Usuarios Comunes:
- 🔐 Registro y login de usuarios (Autenticación manual, próxima integración con Google Auth).
- 📝 Enviar solicitudes de adopción.
- 🙋‍♂️ Aplicar para ser voluntario o transito.
- 🌟 Apadrinar a un gatito mediante [MercadoPago](https://www.mercadopago.com/) (cuota mensual).

### Voluntarios:
- 📦 Cargar gatitos para adopción.
- 🔍 Buscar un tránsito para un gatito.
- 📬 Revisar solicitudes de adopción.

### Admins / Socios:
- 📋 Revisar solicitudes de voluntariado.
- 🔒 Bloquear usuarios.
- 👑 Dar permisos de admin a otros usuarios.
- 🔄 Actualizar cuotas mensuales (actualmente manual, futura implementación automática).

### Integraciones 🌐

- ☁️ Conexión con [Cloudinary](https://cloudinary.com/) para cargar imágenes de gatitos.
- 📁 Almacenamiento de fichas veterinarias en [Amazon S3](https://aws.amazon.com/s3/).
- 💳 Integración con [MercadoPago](https://www.mercadopago.com/) para apadrinamientos.


