# ControlPark-IQ Móvil

Aplicación móvil Android (Java) del sistema **ControlPark-IQ**, para la gestión de parqueaderos en conjuntos residenciales. Consume la API REST de ControlPark-IQ desplegada en Render.

**Evidencia:** GA8-220501096-AA2-EV02 · Desarrollar módulos móviles según requerimientos del proyecto
**Programa:** Tecnólogo en Análisis y Desarrollo de Software (ADSO) · SENA · Ficha 3235898
**Aprendiz:** Carlos Eduardo Valero
**Instructora:** Adriana Milena Rangel Márquez

## Módulos

| Módulo | Rol | Funciones |
|---|---|---|
| Autenticación | Todos | Inicio de sesión con token, validación de campos, cierre de sesión y sesión guardada |
| Portería | PORTERIA | Entrada por placa con espacio asignado automáticamente, vehículos dentro, salida, factura y pago del visitante |
| Administrador | ADMINISTRADOR | Tablero con ocupación, ingresos, cobros pendientes, mensualidades, ocupación por zona y espacios por estado |
| Residente | RESIDENTE | Mensualidad con detalle por vehículo, vehículos registrados, vehículos dentro e historial |

## Tecnologías

- Java 11 · Android SDK 37 (mínimo API 24)
- Android Studio · Gradle (Kotlin DSL)
- Material Components, RecyclerView y SwipeRefreshLayout (presentación)
- Retrofit 2, Gson y OkHttp (consumo de la API REST)
- JUnit 4 y MockWebServer (pruebas unitarias)
- Git y GitHub (control de versiones)

## Arquitectura por capas

```
co.edu.sena.controlparkiq.movil
├── data
│   ├── api          Retrofit: interfaz de la API, cliente, interceptor del token
│   ├── modelo       DTO que llegan y salen de la API
│   └── repositorio  Repositorios: llaman a la API y entregan el resultado
├── ui
│   ├── comun        Componentes reutilizables: actividad base, navegador, adaptador
│   ├── login        Módulo de autenticación
│   ├── porteria     Módulo de portería
│   ├── admin        Módulo del administrador
│   └── residente    Módulo del residente
└── util             Sesión, validaciones y formatos
```

Patrones de diseño: Repository, Singleton, Adapter, DTO, Interceptor e inversión de dependencias (interfaz `AlmacenSesion`).

## Cómo ejecutar

1. Clonar el repositorio:
   ```
   git clone https://github.com/CarlosEvalero82/ControlPark-IQ-Movil.git
   ```
2. Abrir la carpeta en Android Studio y esperar la sincronización de Gradle.
3. Iniciar un emulador (API 24 o superior) y presionar **Run 'app'**.

La primera conexión puede tardar hasta un minuto, porque el servidor de Render se activa con la primera petición.

## Servidor

- API REST: https://controlpark-iq-api.onrender.com/
- Autenticación: cabecera `Authorization: Bearer <token>`

## Usuarios de prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| admin.torres | Parque2026 | Administrador |
| porteria.torre1 | Garita2026 | Portería |
| laura.gomez | Residente2026 | Residente |

## Pruebas unitarias

21 pruebas en `app/src/test`: validaciones (`ValidadorTest`), formatos (`FormatoTest`), capa de datos contra un servidor simulado (`ControlParkApiTest`) y repositorios (`RepositoriosTest`).

En Android Studio: clic derecho sobre `co.edu.sena.controlparkiq.movil (test)` y luego **Run 'Tests in 'co.edu.sena.controlparkiq.movil''**.
