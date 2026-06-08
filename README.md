LOYALTY FrontEnd
Aplicación móvil nativa para la gestión de fidelización de clientes en tiendas de ropa deportiva.

## Descripción del Proyecto
Sistema LOYALTY. Esta aplicación Android permite a los usuarios interactuar con el programa de fidelización, explorar el catálogo de productos, gestionar compras y canjear puntos por recompensas. Está desarrollada bajo una arquitectura moderna utilizando Jetpack Compose para la interfaz de usuario y Retrofit para la integración con el Backend desarrollado en Django.

## Características Principales

### Gestión de Usuarios y Sesión
* Registro de nuevos usuarios con validación de datos.
* Autenticación segura mediante tokens JWT.
* Persistencia de sesión para acceso rápido.
* Gestión de perfil de usuario y cierre de sesión.

### Sistema de Fidelización y Recompensas
* Visualización en tiempo real de puntos acumulados y nivel de membresía.
* Catálogo de recompensas canjeables según el puntaje del usuario.
* Validación automática de saldo de puntos antes del canje.
* Sincronización inmediata de puntos tras realizar acciones en la app.

### Experiencia de Tienda y Compras
* Navegación por catálogo de productos deportivos.
* Carrito de compras integrado para selección de múltiples artículos.
* Gestión de órdenes y visualización del historial de compras.
* Sistema de estados para seguimiento de pedidos (En local, En camino, Entregado).

### Panel de Administración
* Acceso restringido para personal administrativo.
* Gestión integral del catálogo: creación y edición de productos/recompensas.
* Control de inventario y stock directamente desde la aplicación.
* Gestión de estados de productos (Activo/Inactivo).
* Visualización de solicitudes de devoluciones.

## Arquitectura y Tecnologías
La aplicación ha sido construida siguiendo las guías de desarrollo de Android recomendadas por Google:

* **Lenguaje:** Kotlin 2.1.0
* **UI Framework:** Jetpack Compose con Material Design 3.
* **Arquitectura:** MVVM (Model-View-ViewModel) con controladores StateFlow.
* **Red:** Retrofit 2.11.0 para consumo de API REST.
* **Navegación:** Jetpack Navigation Compose para el flujo entre pantallas.
* **Inyección de Dependencias:** Gestión manual mediante ViewModels de Android.
* **Serialización:** GSON para el mapeo de objetos JSON a modelos de datos.

## Requisitos del Sistema
* Android 8.0 (API Level 26) o superior.
* Conexión a Internet.
* Android Studio Ladybug o superior para desarrollo/compilación.

## Estructura del Proyecto
* **ui/screens:** Definición de todas las pantallas de la aplicación (Home, Login, Tienda, Admin, etc.).
* **data/controller:** Lógica de negocio y gestión de estados de la UI.
* **data/model:** Definición de las entidades de datos (Producto, Compra, Recompensa).
* **data/remote:** Configuración de Retrofit y definición de rutas de la API.
* **ui/theme:** Configuración visual de la aplicación (colores, tipografía y formas).

## Instalación Local
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/KarlaRosales13F/LoyaltyFrontEnd.git
   ```
2. Abrir el proyecto en Android Studio.
3. Asegurarse de que la `BASE_URL` en `RetrofitClient` apunte a la dirección IP correcta de su servidor backend.
4. Sincronizar el proyecto con archivos Gradle.
5. Compilar y ejecutar en un emulador o dispositivo físico.

## Contacto
Desarrollado por Karla Rosales

Repositorio: https://github.com/KarlaRosales13F/LoyaltyFrontEnd.git
