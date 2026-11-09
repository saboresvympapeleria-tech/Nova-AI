# Conectar el cerebro de Nova IA

Se añadió un backend Node.js que recibe mensajes en `POST /chat` y los envía a OpenAI Responses API. La clave permanece en el servidor.

## Falta completar una acción del propietario
1. Crear una cuenta en Render y elegir **New → Blueprint**.
2. Conectar el repositorio `saboresvympapeleria-tech/Nova-AI` y desplegar el archivo `render.yaml`.
3. En las variables de entorno del servicio, añadir `OPENAI_API_KEY` con una clave creada en https://platform.openai.com/api-keys.
4. Configurar facturación/límites de uso de API en la plataforma de OpenAI si corresponde.
5. Esperar a que el servicio esté disponible y comprobar `https://TU-SERVICIO.onrender.com/health`.

Luego se debe sustituir el endpoint de ejemplo en las aplicaciones por `https://TU-SERVICIO.onrender.com/chat` y volver a compilar Android y Windows.

**Seguridad:** nunca pegues la clave en GitHub, en el chat ni dentro del APK/EXE. Se introduce únicamente como variable secreta `OPENAI_API_KEY` en el panel del servidor. Las llamadas API pueden tener coste según el uso.
