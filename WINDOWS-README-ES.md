# Nova IA para Windows

Esta carpeta contiene una aplicación de escritorio para Windows 10/11 basada en Electron. El flujo de GitHub Actions genera un instalador `.exe` (NSIS) automáticamente.

## Descargar
En GitHub abre **Actions → Build Nova IA for Windows**, entra en la ejecución más reciente que termine en verde y descarga el artefacto **NovaIA-Windows-Installer**. Dentro del ZIP estará el instalador `.exe`.

## Estado actual
- Aplicación de escritorio con interfaz de chat.
- Guarda localmente la dirección HTTPS del backend configurado.
- No incluye un modelo de IA ni un servidor: para responder de verdad hay que desplegar un backend seguro que acepte `POST {"message":"..."}` y devuelva `{"reply":"..."}`.
- No incrustar claves privadas de proveedores de IA en la aplicación.

Este instalador es para Windows; el APK Android se construye por separado.
