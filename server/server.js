import express from "express";
import cors from "cors";
import rateLimit from "express-rate-limit";

const app = express();
app.use(cors({ origin: process.env.ALLOWED_ORIGINS?.split(",") || "*" }));
app.use(express.json({ limit: "1mb" }));
app.use("/chat", rateLimit({ windowMs: 60_000, limit: 20 }));

app.get("/health", (_req, res) => res.json({ ok: true, service: "Nova IA" }));

app.post("/chat", async (req, res) => {
  try {
    const message = typeof req.body?.message === "string" ? req.body.message.trim() : "";
    if (!message) return res.status(400).json({ error: "Escribe un mensaje." });
    if (message.length > 12000) return res.status(413).json({ error: "El mensaje es demasiado largo." });
    if (!process.env.OPENAI_API_KEY) return res.status(503).json({ error: "El servidor aún no tiene configurada la clave de IA." });

    const response = await fetch("https://api.openai.com/v1/responses", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${process.env.OPENAI_API_KEY}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        model: process.env.OPENAI_MODEL || "gpt-4.1-mini",
        instructions: "Eres Nova IA, una asistente útil, amable y honesta. Conversa en el idioma y tono de la persona, adapta tu explicación a su nivel y recuerda el contexto que esté presente en esta conversación. No afirmes que puedes controlar dispositivos, ver cámaras o realizar acciones si no tienes una herramienta real y permiso explícito. Si no sabes algo, dilo claramente.",
        input: message,
        max_output_tokens: 900
      })
    });
    const data = await response.json();
    if (!response.ok) {
      console.error("OpenAI API error:", data);
      return res.status(502).json({ error: "El proveedor de IA no pudo completar la solicitud." });
    }
    const reply = (data.output || []).flatMap(item => item.content || [])
      .filter(item => item.type === "output_text")
      .map(item => item.text).join("\n").trim();
    return res.json({ reply: reply || "No recibí una respuesta de la IA." });
  } catch (error) {
    console.error("Nova server error:", error);
    return res.status(500).json({ error: "Error interno del servidor." });
  }
});

const port = Number(process.env.PORT || 3000);
app.listen(port, "0.0.0.0", () => console.log(`Nova IA server listening on ${port}`));
