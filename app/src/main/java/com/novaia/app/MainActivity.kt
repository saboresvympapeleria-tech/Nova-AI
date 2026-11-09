package com.novaia.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private val Ink = Color(0xFF090B16)
private val Panel = Color(0xFF15182A)
private val Violet = Color(0xFF8B5CF6)
private val Cyan = Color(0xFF54C8FF)

data class ChatMessage(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NovaChat() }
    }
}

@Composable
fun NovaChat() {
    var input by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    val endpoint = "https://TU-SERVIDOR.example.com/chat"
    val messages = remember {
        mutableStateListOf(
            ChatMessage("¡Hola! Soy Nova IA. Estoy aquí para conversar, ayudarte y aprender tus preferencias con tu permiso.", false)
        )
    }
    val scope = rememberCoroutineScope()

    MaterialTheme(colorScheme = darkColorScheme(
        background = Ink, surface = Panel, primary = Violet, secondary = Cyan
    )) {
        Column(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color(0xFF10152A), Ink, Color(0xFF090B16)))
            ).padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(42.dp).background(Brush.linearGradient(listOf(Violet, Cyan)), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                    Text("N", color = Color.White, fontWeight = FontWeight.Black, fontSize = 23.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Nova IA", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Tu asistente inteligente", color = Cyan, fontSize = 12.sp)
                }
                Spacer(Modifier.weight(1f))
                Text("●", color = Color(0xFF53D99A), fontSize = 12.sp)
                Text(" En línea", color = Color.LightGray, fontSize = 12.sp)
            }
            Spacer(Modifier.height(18.dp))
            LazyColumn(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(messages) { msg ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = if (msg.fromUser) Arrangement.End else Arrangement.Start) {
                        Column(
                            Modifier.widthIn(max = 310.dp)
                                .background(if (msg.fromUser) Color(0xFF34305B) else Panel, RoundedCornerShape(18.dp))
                                .padding(14.dp)
                        ) {
                            Text(if (msg.fromUser) "Tú" else "Nova", color = if (msg.fromUser) Color(0xFFD9C8FF) else Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(5.dp))
                            Text(msg.text, color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
                if (busy) item { Text("Nova está pensando…", color = Cyan, fontSize = 13.sp) }
            }
            Row(
                Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(24.dp)).padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = input, onValueChange = { input = it },
                    modifier = Modifier.weight(1f).padding(8.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 15.sp),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Cyan),
                    decorationBox = { inner -> if (input.isEmpty()) Text("Escribe a Nova…", color = Color.Gray); inner() }
                )
                Button(
                    onClick = {
                        val prompt = input.trim()
                        if (prompt.isNotEmpty() && !busy) {
                            messages.add(ChatMessage(prompt, true)); input = ""; busy = true
                            scope.launch {
                                val answer = askNova(endpoint, prompt)
                                messages.add(ChatMessage(answer, false)); busy = false
                            }
                        }
                    },
                    enabled = input.isNotBlank() && !busy,
                    colors = ButtonDefaults.buttonColors(containerColor = Violet),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                ) { Text("Enviar", color = Color.White) }
            }
            Spacer(Modifier.height(8.dp))
            Text("La conexión con el cerebro de IA se configura en el servidor. No coloques claves secretas dentro del APK.", color = Color.Gray, fontSize = 10.sp)
        }
    }
}

private suspend fun askNova(endpoint: String, prompt: String): String = withContext(Dispatchers.IO) {
    try {
        if (endpoint.contains("TU-SERVIDOR")) return@withContext "La interfaz está lista. Falta conectar el servidor seguro de inteligencia artificial para que pueda responder."
        val conn = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15000
            readTimeout = 30000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
        }
        conn.outputStream.use { it.write(JSONObject().put("message", prompt).toString().toByteArray()) }
        val body = (if (conn.responseCode in 200..299) conn.inputStream else conn.errorStream).bufferedReader().use { it.readText() }
        conn.disconnect()
        JSONObject(body).optString("reply", "No recibí una respuesta válida del servidor.")
    } catch (e: Exception) {
        "No pude conectar con el servidor de Nova IA. Revisa la conexión y la dirección del servidor."
    }
}
