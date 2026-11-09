# Nova IA — proyecto Android inicial

Este paquete contiene el **código fuente inicial** de una aplicación Android nativa con interfaz de chat oscura para Nova IA.

## Estado real
- Incluye: proyecto Kotlin + Jetpack Compose, pantalla de chat y cliente HTTP.
- Aún no incluye: APK compilado, modelo de IA conectado, memoria persistente, voz en vivo, cámara, control de dispositivos, inicio de sesión ni publicación en Play Store.
- La app necesita un backend HTTPS. No se debe incrustar una clave de proveedor de IA dentro del APK.

## Abrir y compilar
1. Instala Android Studio en una computadora.
2. Abre la carpeta `NovaIA-Android`.
3. Permite que Gradle sincronice y descarga los componentes Android SDK solicitados.
4. Para generar un APK de prueba: **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

## Conectar el cerebro de IA
El cliente envía `POST` con JSON `{ "message": "..." }` a un endpoint HTTPS y espera `{ "reply": "..." }`.
En `MainActivity.kt`, cambia el valor de `endpoint` por la URL real de tu backend.

El backend debe guardar la clave de IA como variable de entorno y autenticar las solicitudes. No publiques una clave privada en la aplicación.

## Siguiente etapa
Implementar backend seguro y memoria con consentimiento; después voz en tiempo real, cámara con permisos visibles, autenticación y conectores de dispositivos/aplicaciones. Cada integración requiere permisos y compatibilidad propios.
