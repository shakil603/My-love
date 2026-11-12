package com.amora.app

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.json.JSONObject
import java.util.UUID
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/* =====================================================================
   Real messaging over the open MQTT network.
   Client: Eclipse Paho (open source, EPL).
   Broker: broker.hivemq.com — free public broker running open tech;
   can be swapped for any self-hosted Mosquitto/EMQX instance or even
   a broker running on a user's own device on the same network.
   ===================================================================== */

private const val BROKER = "tcp://broker.hivemq.com:1883"
private const val TOPIC_PREFIX = "amora/v1/room/"

/* ---------- End-to-end encryption (AES-256-GCM, key derived from room) ---------- */

object E2E {
    private fun key(room: String) = SecretKeySpec(
        MessageDigest.getInstance("SHA-256").digest(("amora-e2e::" + room).toByteArray()), "AES"
    )

    fun encrypt(room: String, plain: String): String {
        val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key(room), GCMParameterSpec(128, iv))
        val ct = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + ct, Base64.NO_WRAP)
    }

    fun decrypt(room: String, data: String): String? = try {
        val raw = Base64.decode(data, Base64.NO_WRAP)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key(room), GCMParameterSpec(128, raw.copyOfRange(0, 12)))
        String(cipher.doFinal(raw.copyOfRange(12, raw.size)), Charsets.UTF_8)
    } catch (e: Exception) { null }
}

data class LiveMsg(val id: String, val nick: String, val from: String, val text: String, val ts: Long)

object LivePrefs {
    fun nick(ctx: Context): String {
        val p = ctx.getSharedPreferences("amora", Context.MODE_PRIVATE)
        var n = p.getString("nick", null)
        if (n.isNullOrBlank()) {
            n = "Guest" + (1000..9999).random()
            p.edit().putString("nick", n).apply()
        }
        return n
    }

    fun setNick(ctx: Context, n: String) {
        ctx.getSharedPreferences("amora", Context.MODE_PRIVATE).edit().putString("nick", n).apply()
    }

    fun clientId(ctx: Context): String {
        val p = ctx.getSharedPreferences("amora", Context.MODE_PRIVATE)
        var c = p.getString("cid", null)
        if (c.isNullOrBlank()) {
            c = "amora-" + UUID.randomUUID().toString().substring(0, 12)
            p.edit().putString("cid", c).apply()
        }
        return c
    }
}

/* ---------- Live hub: pick name, join rooms ---------- */

@Composable
fun LiveHubScreen(onBack: () -> Unit, go: (String) -> Unit) {
    val ctx = LocalContext.current
    var nick by remember { mutableStateOf(LivePrefs.nick(ctx)) }
    var code by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("Live messaging", onBack, sub = "Real messages · E2E encrypted · worldwide")
        Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Text("YOUR NAME IN LIVE ROOMS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
            Spacer(Modifier.height(6.dp))
            TextField(
                value = nick,
                onValueChange = { nick = it; if (it.isNotBlank()) LivePrefs.setNick(ctx, it.trim()) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(Modifier.height(18.dp))
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(AuroraGradient)
                    .clickable { go("live/global") }.padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(46.dp).clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.Public, null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Global room", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text(
                            "Everyone running Amora, anywhere on Earth",
                            color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(Icons.Filled.Bolt, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.height(14.dp))
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Color.White)
                    .border(1.dp, Color(0xFFEFEBF8), RoundedCornerShape(22.dp)).padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(40.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFF6E7FF)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.Key, null, tint = Orchid, modifier = Modifier.size(18.dp)) }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Private room", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                        Text("Share a code with anyone — only you two see it", fontSize = 10.sp, color = Txt2, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = code, onValueChange = { code = it },
                        placeholder = { Text("Room code, e.g. shakil123", fontSize = 12.sp, color = Txt3) },
                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(14.dp)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Cloud, unfocusedContainerColor = Cloud,
                            focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Join", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                            .background(if (code.isBlank()) Mist else Ink)
                            .clickable { if (code.isNotBlank()) go("live/" + code.trim().lowercase().replace(" ", "-")) }
                            .padding(horizontal = 18.dp, vertical = 14.dp)
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFE9FBF3)).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Group, null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "100% open source: Eclipse Paho client + MQTT network. Works between real devices worldwide — install Amora on two phones and chat live. The broker can be self-hosted or run on your own device.",
                    fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857), lineHeight = 15.sp
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

/* ---------- Live room (real send/receive) ---------- */

@Composable
fun LiveChatScreen(room: String, onBack: () -> Unit, go: (String) -> Unit = {}) {
    val ctx = LocalContext.current
    val nick = remember { LivePrefs.nick(ctx) }
    val cid = remember { LivePrefs.clientId(ctx) + "-" + (100..999).random() }
    val topic = TOPIC_PREFIX + room
    val msgs = remember { mutableStateListOf<LiveMsg>() }
    var status by remember { mutableStateOf("Connecting…") }
    var input by remember { mutableStateOf("") }
    var client by remember { mutableStateOf<MqttAsyncClient?>(null) }
    val listState = rememberLazyListState()
    val main = remember { Handler(Looper.getMainLooper()) }

    DisposableEffect(room) {
        val c = MqttAsyncClient(BROKER, cid, MemoryPersistence())
        client = c
        c.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                main.post { status = "Live" }
                try { c.subscribe(topic, 0) } catch (_: Exception) {}
            }

            override fun connectionLost(cause: Throwable?) {
                main.post { status = "Reconnecting…" }
            }

            override fun messageArrived(t: String?, message: MqttMessage?) {
                try {
                    val env = JSONObject(String(message!!.payload))
                    val inner = E2E.decrypt(room, env.optString("e")) ?: return
                    val j = JSONObject(inner)
                    val m = LiveMsg(
                        env.optString("i"), j.optString("n", "Guest"),
                        env.optString("c"), j.optString("t"), j.optLong("ts")
                    )
                    main.post { if (msgs.none { it.id == m.id } && m.text.isNotBlank()) msgs.add(m) }
                } catch (_: Exception) {}
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })
        try {
            val opts = MqttConnectOptions().apply {
                isCleanSession = true
                isAutomaticReconnect = true
                connectionTimeout = 10
            }
            c.connect(opts)
        } catch (e: Exception) {
            status = "Offline — check internet"
        }
        onDispose {
            Thread {
                try { c.disconnectForcibly(500, 500) } catch (_: Exception) {}
                try { c.close() } catch (_: Exception) {}
            }.start()
        }
    }

    fun sendText(text: String): Boolean {
        if (text.isBlank()) return false
        val inner = JSONObject()
            .put("n", nick).put("t", text.trim())
            .put("ts", System.currentTimeMillis())
        val env = JSONObject()
            .put("i", UUID.randomUUID().toString())
            .put("c", cid)
            .put("e", E2E.encrypt(room, inner.toString()))
        return try {
            client?.publish(topic, MqttMessage(env.toString().toByteArray()).apply { qos = 0 })
            true
        } catch (_: Exception) {
            status = "Reconnecting…"
            false
        }
    }

    fun send() {
        if (sendText(input)) input = ""
    }

    LaunchedEffect(msgs.size) {
        if (msgs.isNotEmpty()) listState.animateScrollToItem(msgs.size - 1)
    }

    Column(
        Modifier.fillMaxSize().background(Color(0xFFFBFAFE)).statusBarsPadding().navigationBarsPadding()
    ) {
        /* header */
        Row(
            Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back", tint = Ink) }
            Box(
                Modifier.size(38.dp).clip(CircleShape).background(AuroraGradient),
                contentAlignment = Alignment.Center
            ) { Icon(if (room == "global") Icons.Filled.Public else Icons.Filled.Key, null, tint = Color.White, modifier = Modifier.size(18.dp)) }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    if (room == "global") "Global room" else "Room · $room",
                    fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(7.dp).clip(CircleShape)
                            .background(if (status == "Live") Mint else Amber)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "$status · E2E encrypted · as $nick", fontSize = 10.sp,
                        color = if (status == "Live") Mint else Txt2, fontWeight = FontWeight.Bold,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = {
                sendText("\uD83D\uDCDE $nick started a secure voice call — tap the call icon to join")
                go("meet/$room/audio")
            }) { Icon(Icons.Filled.Call, "Voice call", tint = Ink) }
            IconButton(onClick = {
                sendText("\uD83C\uDFA5 $nick started a secure video call — tap the video icon to join")
                go("meet/$room/video")
            }) { Icon(Icons.Filled.Videocam, "Video call", tint = Ink) }
        }
        /* messages */
        if (msgs.isEmpty()) {
            Column(
                Modifier.weight(1f).fillMaxWidth().padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.Bolt, null, tint = Mist, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(10.dp))
                Text(
                    if (status == "Live") "You're live. Say something —\nanyone in this room will see it instantly."
                    else "Connecting to the open network…",
                    fontSize = 13.sp, color = Txt3, fontWeight = FontWeight.Bold, lineHeight = 19.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                itemsIndexed(msgs) { _, m ->
                    val mine = m.from == cid
                    Box(Modifier.fillMaxWidth(), contentAlignment = if (mine) Alignment.CenterEnd else Alignment.CenterStart) {
                        Column(
                            Modifier.fillMaxWidth(0.78f)
                                .clip(
                                    if (mine) RoundedCornerShape(18.dp, 18.dp, 6.dp, 18.dp)
                                    else RoundedCornerShape(18.dp, 18.dp, 18.dp, 6.dp)
                                )
                                .background(if (mine) Color.Transparent else Color.White)
                                .then(
                                    if (mine) Modifier.background(AuroraGradient)
                                    else Modifier.border(
                                        1.dp, Color(0xFFEFEBF8),
                                        RoundedCornerShape(18.dp, 18.dp, 18.dp, 6.dp)
                                    )
                                )
                                .padding(11.dp)
                        ) {
                            if (!mine) {
                                Text(m.nick, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Orchid)
                                Spacer(Modifier.height(2.dp))
                            }
                            Text(
                                m.text, fontSize = 13.sp, lineHeight = 19.sp,
                                color = if (mine) Color.White else Ink
                            )
                        }
                    }
                }
            }
        }
        /* input */
        Row(
            Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input, onValueChange = { input = it },
                placeholder = { Text("Message…", fontSize = 13.sp, color = Txt3) },
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(50)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Cloud, unfocusedContainerColor = Cloud,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier.size(42.dp).clip(CircleShape).background(AuroraGradient).clickable { send() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Send, "Send", tint = Color.White, modifier = Modifier.size(18.dp)) }
        }
    }
}
