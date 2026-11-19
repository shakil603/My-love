package com.amora.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/* ---------- shared ---------- */

@Composable
fun ScreenHeader(title: String, onBack: () -> Unit, tint: Color = Ink, sub: String? = null) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = tint) }
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = tint)
            if (sub != null) Text(sub, fontSize = 10.sp, color = if (tint == Ink) Txt3 else tint.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun WhiteCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Paper)
            .padding(14.dp)
    ) { content() }
}

@Composable
fun ToggleRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, sub: String, initial: Boolean) {
    val tctx = androidx.compose.ui.platform.LocalContext.current
    var on by remember { mutableStateOf(Store.get(tctx, "tgl_" + title, initial.toString()) == "true") }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Cloud),
            contentAlignment = Alignment.Center
        ) { Icon(icon, null, tint = Orchid, modifier = Modifier.size(17.dp)) }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            Text(sub, fontSize = 10.sp, color = Txt3)
        }
        Switch(checked = on, onCheckedChange = { on = it; Store.set(tctx, "tgl_" + title, it.toString()) })
    }
}

/* ---------- Sign up & OTP ---------- */

@Composable
fun SignupScreen(onNext: () -> Unit, onBack: () -> Unit) {
    val sgctx = androidx.compose.ui.platform.LocalContext.current
    var phone by remember { mutableStateOf(Store.get(sgctx, "phone", "")) }
    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(10.dp))
        IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
        Box(
            Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(AuroraGradient),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(26.dp)) }
        Spacer(Modifier.height(20.dp))
        Text("Create your\naccount", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 33.sp)
        Spacer(Modifier.height(8.dp))
        Text("One account for love, friends and work.\nFree forever · open source.", fontSize = 13.sp, color = Txt2, lineHeight = 19.sp)
        Spacer(Modifier.height(22.dp))
        Text("PHONE NUMBER", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Txt2)
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(17.dp))
                .border(1.8.dp, Orchid, RoundedCornerShape(17.dp))
                .padding(horizontal = 14.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("\uD83C\uDDE7\uD83C\uDDE9 +880", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
            Spacer(Modifier.width(10.dp))
            Box(
                Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(Mist)
            )
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = phone,
                onValueChange = { v -> if (v.length <= 11 && v.all { it.isDigit() || it == ' ' }) phone = v },
                textStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink),
                keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone),
                singleLine = true,
                decorationBox = { inner ->
                    Box {
                        if (phone.isEmpty()) Text("17 1234 5678", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Txt3)
                        inner()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(14.dp))
        GradientButton("Continue", Modifier.fillMaxWidth(), onClick = {
            Store.set(sgctx, "phone", phone.trim())
            onNext()
        })
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.weight(1f).height(1.dp).background(Mist))
            Text("  or continue with  ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Txt3)
            Box(Modifier.weight(1f).height(1.dp).background(Mist))
        }
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .border(1.5.dp, Mist, RoundedCornerShape(15.dp))
                    .clickable(onClick = onNext)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) { Text("G  Google", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp) }
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .border(1.5.dp, Mist, RoundedCornerShape(15.dp))
                    .clickable(onClick = onNext)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Email, null, tint = Ink, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Email", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Text(
            "By continuing you agree to the Terms & Privacy Policy.\nYour data stays on open-source servers you can inspect.",
            fontSize = 10.sp, color = Txt3, lineHeight = 15.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 20.dp)
        )
    }
}

@Composable
fun OtpScreen(onNext: () -> Unit, onBack: () -> Unit) {
    val octx = androidx.compose.ui.platform.LocalContext.current
    val phone = remember { Store.get(octx, "phone", "") }
    var code by remember { mutableStateOf("") }
    var expected by remember { mutableStateOf("") }
    var err by remember { mutableStateOf(false) }
    var reveal by remember { mutableStateOf(false) }
    var resendKey by remember { mutableIntStateOf(0) }
    var secs by remember { mutableIntStateOf(30) }
    val permLauncher = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) resendKey++ }
    fun sendCode(): String {
        val c = (100000..999999).random().toString()
        try {
            val nm = octx.getSystemService(android.app.NotificationManager::class.java)
            nm.createNotificationChannel(
                android.app.NotificationChannel("amora_otp", "Verification codes", android.app.NotificationManager.IMPORTANCE_HIGH)
            )
            val n = androidx.core.app.NotificationCompat.Builder(octx, "amora_otp")
                .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
                .setContentTitle("Amora verification code")
                .setContentText("Your code is $c")
                .setStyle(androidx.core.app.NotificationCompat.BigTextStyle().bigText("Your code is $c — enter it in Amora to verify this phone."))
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
            nm.notify(7001, n)
        } catch (_: Exception) {}
        return c
    }
    LaunchedEffect(resendKey) {
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            octx.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            permLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        expected = sendCode()
        err = false
        reveal = false
        code = ""
        secs = 30
        while (secs > 0) { kotlinx.coroutines.delay(1000); secs-- }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(10.dp))
        IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
        Spacer(Modifier.height(10.dp))
        Text("Enter the code", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text(
            "A real 6-digit code was just sent to this phone as a notification" +
                (if (phone.isBlank()) "." else " for +880 $phone.") +
                " Pull down your notification shade to read it.",
            fontSize = 13.sp, color = Txt2, lineHeight = 19.sp
        )
        Spacer(Modifier.height(26.dp))
        BasicTextField(
            value = code,
            onValueChange = { v -> if (v.length <= 6 && v.all { it.isDigit() }) { code = v; err = false } },
            keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword),
            singleLine = true,
            decorationBox = { inner ->
                Box {
                    Box(Modifier.size(1.dp)) { inner() }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        repeat(6) { i ->
                            Box(
                                Modifier
                                    .size(width = 44.dp, height = 54.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .then(
                                        if (err) Modifier.border(1.8.dp, Color(0xFFF43F5E), RoundedCornerShape(14.dp))
                                        else if (i == code.length) Modifier.border(1.8.dp, Orchid, RoundedCornerShape(14.dp))
                                        else Modifier.background(Cloud)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(code.getOrNull(i)?.toString() ?: "", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (err) {
            Spacer(Modifier.height(10.dp))
            Text(
                "Wrong code — check the notification and try again.",
                fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF43F5E),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            if (secs > 0) "Resend code in 00:%02d".format(secs) else "Resend code ↻",
            fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
            color = if (secs > 0) Txt3 else Orchid,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { if (secs <= 0) resendKey++ }
                .padding(6.dp)
        )
        Text(
            if (reveal) "Your code: $expected" else "Notification blocked? Show code here",
            fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
            color = if (reveal) Ink else Txt3,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { reveal = true }
                .padding(6.dp)
        )
        Spacer(Modifier.height(14.dp))
        GradientButton(
            if (code.length == 6) "Verify" else "Verify (${code.length}/6)",
            Modifier.fillMaxWidth(),
            onClick = {
                if (code.length == 6 && code == expected) {
                    Store.set(octx, "verified", "true")
                    onNext()
                } else err = true
            }
        )
    }
}

/* ---------- Match ---------- */

@Composable
fun MatchScreen(onHello: () -> Unit, onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF5B21B6), Color(0xFF2E1065), Color(0xFF140A2E))))
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.height(200.dp).fillMaxWidth()) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400",
                contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(width = 126.dp, height = 164.dp)
                    .rotate(-9f)
                    .clip(RoundedCornerShape(26.dp))
                    .border(4.dp, Color.White, RoundedCornerShape(26.dp))
            )
            AsyncImage(
                model = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
                contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(width = 126.dp, height = 164.dp)
                    .rotate(9f)
                    .clip(RoundedCornerShape(26.dp))
                    .border(4.dp, Color.White, RoundedCornerShape(26.dp))
            )
            Box(
                Modifier
                    .align(Alignment.Center)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AuroraGradient)
                    .border(4.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(26.dp)) }
        }
        Spacer(Modifier.height(26.dp))
        Text("It's a match!", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text(
            "You and Amara liked each other.\n94% compatible · 6 shared interests",
            color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp, lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Spacer(Modifier.height(26.dp))
        GradientButton("\uD83D\uDCAC  Say hello", Modifier.fillMaxWidth(), onClick = onHello)
        Spacer(Modifier.height(12.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(19.dp))
                .border(1.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(19.dp))
                .clickable(onClick = onBack)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) { Text("Keep exploring", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
    }
}

/* ---------- Story & Reels ---------- */

@Composable
fun StoryScreen(onBack: () -> Unit) {
    val stctx = androidx.compose.ui.platform.LocalContext.current
    var reply by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=700",
            contentDescription = null, contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 14.dp)
        ) {
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                listOf(1f, 1f, 0.58f, 0.12f).forEach { p ->
                    Box(
                        Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.35f))
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(p)
                                .height(4.dp)
                                .background(Paper)
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar("https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200", 36, ringed = true)
                Spacer(Modifier.width(9.dp))
                Column(Modifier.weight(1f)) {
                    Text("Sofia Martínez ✔", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Costa Brava · 2 h ago", color = Color.White.copy(alpha = 0.75f), fontSize = 10.sp)
                }
                IconButton(onClick = onBack) { Icon(Icons.Filled.Close, null, tint = Color.White) }
            }
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Box(
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .border(1.5.dp, Color.White.copy(alpha = 0.45f), RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                ) {
                    BasicTextField(
                        value = reply,
                        onValueChange = { reply = it; sent = false },
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold),
                        singleLine = true,
                        decorationBox = { inner ->
                            Box {
                                if (reply.isEmpty()) Text(
                                    if (sent) "Sent \u2713 — reply again…" else "Reply privately…",
                                    color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp
                                )
                                inner()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Box(
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    var loved by remember { mutableStateOf(false) }
                    Icon(
                        if (loved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, "Love",
                        tint = Rose, modifier = Modifier.size(20.dp).clickable { loved = !loved }
                    )
                }
                Box(
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Send, "Send reply", tint = Color.White,
                        modifier = Modifier.size(18.dp).clickable {
                            if (reply.isNotBlank()) {
                                Store.appendChat(stctx, "1", reply.trim())
                                reply = ""
                                sent = true
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ReelsScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    val rctx = androidx.compose.ui.platform.LocalContext.current
    var rliked by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=700",
            contentDescription = null, contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Color.White) }
                Text("Reels", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                Column(Modifier.weight(1f).padding(start = 14.dp, bottom = 18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200", 36, ringed = true)
                        Spacer(Modifier.width(9.dp))
                        Text("yuki.tanaka", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Spacer(Modifier.width(9.dp))
                        Text(
                            "Follow", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .border(1.5.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                    Spacer(Modifier.height(9.dp))
                    Text(
                        "Tokyo after midnight \uD83C\uDF03 shot entirely on phone #tokyo #nightwalk",
                        color = Color.White, fontSize = 12.sp, lineHeight = 18.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "\uD83C\uDFB5 Yuki Tanaka · Original audio",
                        color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.Black.copy(alpha = 0.45f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Column(
                    Modifier.padding(end = 12.dp, bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            if (rliked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, "Like",
                            tint = if (rliked) Rose else Color.White,
                            modifier = Modifier.size(28.dp).clickable { rliked = !rliked }
                        )
                        Text(if (rliked) "12.5k" else "12.4k", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.ChatBubbleOutline, "Comments", tint = Color.White,
                            modifier = Modifier.size(25.dp).clickable { go("comments") }
                        )
                        Text("482", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Share, "Share", tint = Color.White,
                            modifier = Modifier.size(24.dp).clickable {
                                val it2 = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(android.content.Intent.EXTRA_TEXT, "Watch this on Amora \uD83D\uDC9C https://github.com/shakil603/My-love/releases/latest")
                                }
                                rctx.startActivity(android.content.Intent.createChooser(it2, "Share"))
                            }
                        )
                        Text("Share", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
            LinearProgressIndicator(
                progress = { 0.42f },
                modifier = Modifier.fillMaxWidth().height(3.dp),
                color = Orchid,
                trackColor = Color.White.copy(alpha = 0.25f)
            )
        }
    }
}

/* ---------- Notifications ---------- */

private data class Notif(val emoji: String, val title: String, val sub: String, val action: String?)

@Composable
fun NotificationsScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    val notifs = listOf(
        Notif("\uD83D\uDC98", "Amara matched with you — 94% compatible", "8 min ago", "Say hi"),
        Notif("❤️", "Sofia and 213 others liked your post", "26 min ago", null),
        Notif("\uD83C\uDF99", "Rafi sent you a voice message", "1 h ago · 0:42", "Play"),
        Notif("\uD83D\uDCC5", "Stand-up in 20 min · Project Phoenix", "6 people joining", "Join"),
        Notif("\uD83D\uDCC4", "Daniel shared Launch-Playbook-2026.pdf with you", "2 h ago · preview ready ✓", null),
        Notif("\uD83C\uDF93", "UX course — module 7 unlocked", "68% complete · keep going!", null)
    )
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding()) {
        ScreenHeader(androidx.compose.ui.res.stringResource(R.string.notifications), onBack)
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(notifs.size) { i ->
                val n = notifs[i]
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (i == 0) Color(0xFFFDEBF3) else Cloud),
                        contentAlignment = Alignment.Center
                    ) { Text(n.emoji, fontSize = 17.sp) }
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text(n.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, lineHeight = 17.sp)
                        Text(n.sub, fontSize = 10.sp, color = Txt3, fontWeight = FontWeight.Bold)
                    }
                    n.action?.let {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            it, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(AuroraGradient)
                                .clickable {
                                    when (it) {
                                        "Say hi" -> go("chat/1")
                                        "Play" -> go("chat/3")
                                        "Join" -> go("calendar")
                                        else -> {}
                                    }
                                }
                                .padding(horizontal = 13.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

/* ---------- Search ---------- */

@Composable
fun SearchScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    var q by remember { mutableStateOf("design workshop") }
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
            TextField(
                value = q, onValueChange = { q = it },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = Orchid) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
                    .clip(RoundedCornerShape(17.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Cloud, unfocusedContainerColor = Cloud,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            var selCat by remember { mutableStateOf("All") }
            listOf("All", "People", "Posts", "Files", "Groups").forEach { c ->
                val on = c == selCat
                Text(
                    c, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                    color = if (on) Color.White else Txt2,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (on) Ink else Cloud)
                        .clickable { selCat = c }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item {
                Text("PEOPLE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { go("user/1") }.padding(vertical = 8.dp)
                ) {
                    Avatar("https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=200", 44)
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Amara Okonkwo ✔", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Text("UX Designer · 4 km away · \uD83D\uDC98 matched", fontSize = 10.sp, color = Txt3)
                    }
                    Text(
                        "Message", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Ink,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Cloud)
                            .clickable { go("chat/1") }
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { go("user/8") }.padding(vertical = 8.dp)
                ) {
                    Avatar("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200", 44)
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Daniel Okafor", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Text("Product Lead · runs design community", fontSize = 10.sp, color = Txt3)
                    }
                    Text(
                        "Follow", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(AuroraGradient)
                            .clickable { go("user/8") }
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text("FILES · WITH PREVIEW", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                FileChip("workshop-kit.pdf", "In: My posts · 24 pages · full-text match")
                Spacer(Modifier.height(8.dp))
                FileChip("brand-refresh.fig", "In: Personal vault · Figma · preview ready ✓")
                Spacer(Modifier.height(12.dp))
                Text("GROUPS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { go("chat/2") }.padding(vertical = 6.dp)
                ) {
                    Avatar("https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=200", 44)
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Project Phoenix \uD83D\uDCBC", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Text("6 members · files, tasks & stand-ups", fontSize = 10.sp, color = Txt3)
                    }
                    Text(
                        "Open", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Ink,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Cloud)
                            .clickable { go("chat/2") }
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    )
                }
            }
        }
    }
}

/* ---------- Comments ---------- */

@Composable
fun CommentsScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding().navigationBarsPadding()) {
        val ctx = androidx.compose.ui.platform.LocalContext.current
        val myName = remember { Store.get(ctx, "pname", "Shakil Rahman") }
        val comments = remember { androidx.compose.runtime.mutableStateListOf(
                Triple("Rafi Ahmed", "This light is unreal \uD83D\uDE0D Which lens did you use?", "2 h · ❤ 48" to "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200"),
                Triple("Sofia Martínez (Author)", "@Rafi 35mm f/1.4 — golden hour did the rest ✨", "1 h · ❤ 21" to "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200"),
                Triple("Yuki Tanaka", "Adding Costa Brava to my list right now \uD83E\uDDF3", "58 m · ❤ 12" to "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"),
                Triple("Omar Haddad", "\uD83D\uDCCE attached my edit preset pack for this vibe", "31 m · ❤ 34" to "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200")
            ) }
        ScreenHeader("Comments · ${comments.size}", onBack)
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp)) {
            items(comments.size) { i ->
                val (name, text, metaAv) = comments[i]
                Row(Modifier.padding(vertical = 8.dp)) {
                    Avatar(metaAv.second, 34)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Column(
                            Modifier
                                .clip(RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp))
                                .background(Cloud)
                                .padding(horizontal = 12.dp, vertical = 9.dp)
                        ) {
                            Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text(text, fontSize = 12.sp, lineHeight = 18.sp)
                            if (i == 3) {
                                Spacer(Modifier.height(7.dp))
                                FileChip("warm-presets.zip", "12 files · preview ✓", light = true)
                            }
                        }
                        Text(
                            "${metaAv.first} · Reply", fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold, color = Txt3,
                            modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(Paper)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = input, onValueChange = { input = it },
                placeholder = { Text("Add a comment…", fontSize = 12.sp, color = Txt3) },
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(50)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Cloud, unfocusedContainerColor = Cloud,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(AuroraGradient)
                    .clickable {
                        if (input.isNotBlank()) {
                            comments.add(
                                Triple(
                                    myName, input.trim(),
                                    "now · ❤ 0" to Store.avatar(ctx)
                                )
                            )
                            input = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Filled.Send, "Send", tint = Color.White, modifier = Modifier.size(17.dp)) }
        }
    }
}

/* ---------- Settings & Safety ---------- */

@Composable
fun SettingsScreen(onBack: () -> Unit, onSafety: () -> Unit, onLanguage: () -> Unit = {}, onSignOut: () -> Unit = {}, onEdit: () -> Unit = {}, onTheme: () -> Unit = {}) {
    val sctx = androidx.compose.ui.platform.LocalContext.current
    val sname = remember { Store.get(sctx, "pname", "Shakil Rahman") }
    val shandle = remember { Store.get(sctx, "phandle", "@shakil") }
    LazyColumn(Modifier.fillMaxSize().background(Cloud).statusBarsPadding(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item { ScreenHeader(androidx.compose.ui.res.stringResource(R.string.settings), onBack) }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                WhiteCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar(Store.avatar(sctx), 50)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(sname, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            Text("$shandle · amora.id", fontSize = 11.sp, color = Txt3)
                        }
                        Text("Edit", color = Orchid, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.clickable(onClick = onEdit).padding(6.dp))
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text("PRIVACY & SECURITY", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(7.dp))
                WhiteCard {
                    ToggleRow(Icons.Filled.Lock, "End-to-end encryption", "Default for all chats & calls", true)
                    ToggleRow(Icons.Filled.Favorite, "Dating visibility", "Hidden from contacts & coworkers", true)
                    ToggleRow(Icons.Filled.Visibility, "Read receipts", "Off for non-contacts", false)
                    Row(
                        Modifier.fillMaxWidth().clickable(onClick = onSafety).padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Shield, null, tint = Orchid, modifier = Modifier.size(17.dp)) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Safety center", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("Score 92 · incognito, reports, emergency", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text("APP", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(7.dp))
                WhiteCard {
                    Row(
                        Modifier.fillMaxWidth().clickable(onClick = onLanguage).padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Public, null, tint = Orchid, modifier = Modifier.size(17.dp)) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Text(androidx.compose.ui.res.stringResource(R.string.language), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("53 languages · also in Android Settings", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                    Row(
                        Modifier.fillMaxWidth().clickable(onClick = onTheme).padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Ink),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.DarkMode, null, tint = Paper, modifier = Modifier.size(17.dp)) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Appearance & colors", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("Dark mode · edit any color · wallpaper", fontSize = 10.sp, color = Txt3)
                        }
                        Text("${ThemeX.mode.value} ›", color = Orchid, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                    Row(Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Cloud, null, tint = Orchid, modifier = Modifier.size(17.dp)) }
                        Spacer(Modifier.width(11.dp))
                        Column(
                            Modifier.weight(1f).clickable {
                                try {
                                    sctx.startActivity(
                                        android.content.Intent(
                                            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            android.net.Uri.parse("package:" + sctx.packageName)
                                        )
                                    )
                                } catch (_: Exception) {}
                            }
                        ) {
                            Text("Storage & sync", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("Tap to open app storage in Android Settings", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                WhiteCard {
                    Text(
                        androidx.compose.ui.res.stringResource(R.string.sign_out), color = Color(0xFFF43F5E), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable(onClick = onSignOut)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SafetyScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    val fctx = androidx.compose.ui.platform.LocalContext.current
    LazyColumn(Modifier.fillMaxSize().background(Cloud).statusBarsPadding(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item { ScreenHeader("Safety center", onBack) }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF0F766E), Mint)))
                        .padding(16.dp)
                ) {
                    Text("\uD83D\uDEE1️  You're well protected", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    Text("Safety score 92 · 4 of 5 tools active", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(11.dp))
                    LinearProgressIndicator(
                        progress = { 0.92f },
                        modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(5.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )
                }
                Spacer(Modifier.height(12.dp))
                WhiteCard {
                    Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Check, null, tint = Mint)
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Photo verification", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("Verified · badge active", fontSize = 10.sp, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                        }
                    }
                    ToggleRow(Icons.Filled.VisibilityOff, "Incognito mode", "Only people you like can see you", true)
                    Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Block, null, tint = Orchid, modifier = Modifier.size(17.dp)) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f).clickable { go("followers") }) {
                            Text("Block list", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("3 blocked · also hides from contacts", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                    Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Emergency, null, tint = Rose, modifier = Modifier.size(17.dp)) }
                        Spacer(Modifier.width(11.dp))
                        Column(
                            Modifier.weight(1f).clickable {
                                try {
                                    fctx.startActivity(
                                        android.content.Intent(
                                            android.content.Intent.ACTION_DIAL,
                                            android.net.Uri.parse("tel:999")
                                        )
                                    )
                                } catch (_: Exception) {}
                            }
                        ) {
                            Text("Emergency contacts", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("Tap to open dialer · 999 · share live location", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFFFF1F2))
                        .border(1.5.dp, Color(0xFFFECDD3), RoundedCornerShape(18.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Flag, null, tint = Rose, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Report someone", color = Color(0xFFBE123C), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        Text("Anonymous · reviewed within 24 h", color = Color(0xFFE11D48), fontSize = 10.sp)
                    }
                    var reported by remember { mutableStateOf(false) }
                    Text(
                        if (reported) "Sent \u2713" else "Report",
                        color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (reported) Color(0xFF10B981) else Color(0xFFF43F5E))
                            .clickable { reported = !reported }
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                WhiteCard {
                    Text("Smart protection — always on", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    Spacer(Modifier.height(8.dp))
                    listOf(
                        "✅ Nude image auto-blur (on-device AI)",
                        "✅ Scam & phishing link detection in chats",
                        "✅ Screenshot alert in disappearing chats",
                        "✅ New-match calls stay in-app (no number shared)"
                    ).forEach { Text(it, fontSize = 11.sp, color = Txt2, lineHeight = 20.sp) }
                }
            }
        }
    }
}

/* ---------- Calendar & Board ---------- */

@Composable
fun CalendarScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    val today = remember { java.time.LocalDate.now() }
    val monday = remember { today.minusDays((today.dayOfWeek.value - 1).toLong()) }
    val week = remember {
        (0..4).map { off ->
            val d = monday.plusDays(off.toLong())
            d.dayOfWeek.name.take(3) to d.dayOfMonth.toString()
        }
    }
    var selDay by remember {
        mutableIntStateOf((today.dayOfWeek.value - 1).coerceIn(0, 4))
    }
    val monthTitle = remember {
        today.month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault()) + " " + today.year
    }
    LazyColumn(Modifier.fillMaxSize().background(Paper).statusBarsPadding(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item { ScreenHeader(monthTitle, onBack) }
        item {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                week.forEachIndexed { i, (d, n) ->
                    Column(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .then(if (i == selDay) Modifier.background(AuroraGradient) else Modifier)
                            .clickable { selDay = i }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(d, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = if (i == selDay) Color.White.copy(alpha = 0.85f) else Txt3)
                        Text(n, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = if (i == selDay) Color.White else Ink)
                    }
                }
            }
        }
        item {
            Column(Modifier.padding(16.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Brush.linearGradient(listOf(Indigo, Color(0xFF7C3AED))))
                        .padding(16.dp)
                ) {
                    Text("IN 20 MINUTES · 09:30 – 09:45", color = Color.White.copy(alpha = 0.85f), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    Spacer(Modifier.height(7.dp))
                    Text("Daily stand-up · Phoenix", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    Spacer(Modifier.height(11.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("6 people joining", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(
                            "Join call", color = Indigo, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Paper)
                                .clickable { go("video/phoenix") }
                                .padding(horizontal = 18.dp, vertical = 9.dp)
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                listOf(
                    Triple("11:00", "Design review — onboarding v2" to "11:00–12:00 · Meet room · \uD83D\uDCCE brand-refresh.fig", Mint),
                    Triple("13:00", "Photo walk with Amara \uD83D\uDCF8" to "Golden hour · Riverside park", Amber),
                    Triple("17:30", "Coffee with Amara ☕\uD83D\uDC98" to "Crimson Cup · share live location ON", Rose),
                    Triple("20:00", "UX course · module 7 \uD83C\uDF93" to "Flexible · 40 min remaining", Txt3)
                ).forEach { (time, titleSub, color) ->
                    Row(Modifier.padding(vertical = 5.dp)) {
                        Text(time, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Txt3, modifier = Modifier.width(44.dp).padding(top = 6.dp))
                        Row(
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(color.copy(alpha = 0.1f))
                        ) {
                            Box(Modifier.width(3.5.dp).height(56.dp).background(color))
                            Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp)) {
                                Text(titleSub.first, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                Text(titleSub.second, fontSize = 10.sp, color = Txt3)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoardScreen(onBack: () -> Unit) {
    data class Task(val id: Int, val tag: String, val tagColor: Color, val tagBg: Color, val title: String, val col: Int)
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "HIGH", Color(0xFFBE123C), Color(0xFFFFE4E6), "Final pricing page copy", 0),
                Task(2, "DESIGN", Indigo, Color(0xFFE0E7FF), "Empty-state illustrations", 0),
                Task(3, "COPY", Color(0xFF047857), Color(0xFFD1FAE5), "Release notes v2.0", 0),
                Task(4, "SPRINT 4", Color(0xFFA21CAF), Color(0xFFFAE8FF), "Q3 forecast deck", 1),
                Task(5, "REVIEW", Color(0xFF92400E), Color(0xFFFEF3C7), "Onboarding flow v2", 1),
                Task(6, "BRAND", Color(0xFF7B2CFF), Color(0xFFEDE9FE), "Brand refresh", 2)
            )
        )
    }
    var newTask by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().background(Cloud).statusBarsPadding()) {
        ScreenHeader("Project Phoenix", onBack, sub = "Tap a card to move it forward · long tasks welcome")
        Row(
            Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("To do", "In progress", "Done \u2713").forEachIndexed { col, colName ->
                val colTasks = tasks.filter { it.col == col }
                Column(
                    Modifier
                        .width(240.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (ThemeX.dark) Color(0xFF221D33) else Color(0xFFEFEDF6))
                        .padding(10.dp)
                ) {
                    Text(
                        "$colName · ${colTasks.size}",
                        fontWeight = FontWeight.ExtraBold, fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    colTasks.forEach { t ->
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Paper)
                                .clickable {
                                    tasks = tasks.map { x -> if (x.id == t.id) x.copy(col = (x.col + 1) % 3) else x }
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                t.tag, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = t.tagColor,
                                modifier = Modifier.clip(RoundedCornerShape(50)).background(t.tagBg).padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(t.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(Modifier.height(7.dp))
                            Text(
                                if (col < 2) "Tap \u2192 move to ${if (col == 0) "In progress" else "Done"}" else "Tap \u2192 back to To do",
                                fontSize = 9.sp, color = Txt3, fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (col == 0) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                                .padding(horizontal = 10.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BasicTextField(
                                value = newTask,
                                onValueChange = { newTask = it },
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Ink),
                                singleLine = true,
                                decorationBox = { inner ->
                                    Box {
                                        if (newTask.isEmpty()) Text("+ Add task…", fontSize = 11.sp, color = Txt3, fontWeight = FontWeight.Bold)
                                        inner()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                            if (newTask.isNotBlank()) {
                                Icon(
                                    Icons.Filled.Add, "Add",
                                    tint = Orchid,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            tasks = tasks + Task(
                                                (tasks.maxOfOrNull { it.id } ?: 0) + 1,
                                                "NEW", Color(0xFF0369A1), Color(0xFFE0F2FE), newTask.trim(), 0
                                            )
                                            newTask = ""
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------- Upload (real system file picker) ---------- */

@Composable
fun UploadScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val files = remember { Store.loadFiles(ctx).toMutableStateList() }
    fun fileMeta(uri: android.net.Uri): Array<String> {
        var name = "file"; var size = 0L
        try {
            ctx.contentResolver.query(uri, null, null, null, null)?.use { c ->
                if (c.moveToFirst()) {
                    val ni = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    val si = c.getColumnIndex(android.provider.OpenableColumns.SIZE)
                    if (ni >= 0) name = c.getString(ni) ?: "file"
                    if (si >= 0) size = c.getLong(si)
                }
            }
        } catch (_: Exception) {}
        val mime = try { ctx.contentResolver.getType(uri) ?: "" } catch (_: Exception) { "" }
        val sz = when {
            size >= 1048576L -> "%.1f MB".format(size / 1048576.0)
            size > 0L -> "${size / 1024} KB"
            else -> ""
        }
        return arrayOf(name, uri.toString(), mime, sz)
    }
    val launcher = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        uris.forEach { uri ->
            try {
                ctx.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            val f = fileMeta(uri)
            Store.addFile(ctx, f[0], f[1], f[2], f[3])
            files.add(f)
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        ScreenHeader("My files", onBack, sub = "Upload anything — code, projects, images, archives · live preview")
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            val cats = listOf(
                listOf(Triple("Photos", arrayOf("image/*"), Rose), Triple("Videos", arrayOf("video/*"), Color(0xFFF59E0B)), Triple("Audio", arrayOf("audio/*"), Color(0xFF10B981)), Triple("PDF", arrayOf("application/pdf"), Indigo)),
                listOf(Triple("Code", arrayOf("text/*", "application/json", "application/xml", "application/octet-stream"), Sky), Triple("Archives", arrayOf("application/zip", "application/x-zip-compressed", "application/gzip"), Color(0xFFEC4899)), Triple("APK", arrayOf("application/vnd.android.package-archive"), Violet), Triple("Any file", arrayOf("*/*"), Ink))
            )
            cats.forEach { rowCats ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    rowCats.forEach { (label, mimes, c1) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clip(RoundedCornerShape(16.dp)).clickable { launcher.launch(mimes) }.padding(4.dp)
                        ) {
                            Box(
                                Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Brush.linearGradient(listOf(c1, Orchid))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (label == "Code") Icons.Filled.Code else Icons.Filled.UploadFile,
                                    label, tint = Color.White, modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Txt2)
                        }
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (files.isEmpty()) "No files yet" else "${files.size} file${if (files.size > 1) "s" else ""} · saved on device",
                    fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, modifier = Modifier.weight(1f)
                )
                if (files.isNotEmpty()) Text("Previews live ✓", fontSize = 10.sp, color = Mint, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            if (files.isEmpty()) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Cloud).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.InsertDriveFile, null, tint = Txt3, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Tap a category — pick one or many files of ANY type. They stay here with previews until you remove them.",
                        fontSize = 11.sp, color = Txt2, fontWeight = FontWeight.Medium, lineHeight = 16.sp
                    )
                }
            } else {
                files.forEachIndexed { idx, f ->
                    FilePreviewCard(
                        name = f[0], uri = f[1], mime = f[2], size = f[3],
                        onRemove = {
                            Store.removeFile(ctx, idx)
                            files.removeAt(idx)
                        },
                        onReplace = { launcher.launch(arrayOf("*/*")) }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
            Spacer(Modifier.height(12.dp))
        }
        Box(Modifier.padding(18.dp)) {
            GradientButton(
                if (files.isEmpty()) "Upload your first file" else "Send ${files.size} to chat",
                Modifier.fillMaxWidth()
            ) {
                if (files.isEmpty()) launcher.launch(arrayOf("*/*"))
                else {
                    files.forEach { f -> Store.appendChat(ctx, "1", "\uD83D\uDCCE ${f[0]}${if (f[3].isNotBlank()) " · " + f[3] else ""}") }
                    go("chat/1")
                }
            }
        }
    }
}

private val CodeExts = setOf(
    "kt", "kts", "java", "py", "js", "ts", "jsx", "tsx", "c", "cpp", "h", "hpp", "cs", "go", "rs",
    "rb", "php", "swift", "dart", "html", "css", "scss", "json", "xml", "yml", "yaml", "md", "txt",
    "sh", "bat", "sql", "gradle", "toml", "ini", "csv", "log", "properties"
)

@Composable
fun FilePreviewCard(name: String, uri: String, mime: String, size: String, onRemove: () -> Unit, onReplace: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val ext = name.substringAfterLast('.', "").lowercase()
    val isImage = mime.startsWith("image/") || ext in setOf("jpg", "jpeg", "png", "gif", "webp", "bmp")
    val isCode = !isImage && (ext in CodeExts || mime.startsWith("text/"))
    var expanded by remember { mutableStateOf(false) }
    val codeHead = remember(uri, expanded) {
        if (isCode) try {
            ctx.contentResolver.openInputStream(android.net.Uri.parse(uri))?.use { ins ->
                val buf = ByteArray(if (expanded) 4000 else 400)
                val n = ins.read(buf)
                if (n > 0) String(buf, 0, n) else ""
            } ?: ""
        } catch (_: Exception) { "(preview unavailable)" } else ""
    }
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Cloud).padding(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(width = 38.dp, height = 46.dp).clip(RoundedCornerShape(9.dp))
                    .background(
                        Brush.linearGradient(
                            if (isCode) listOf(Sky, Indigo)
                            else if (isImage) listOf(Rose, Orchid)
                            else listOf(Color(0xFFF43F5E), Color(0xFFC2185B))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    (if (ext.isNotBlank()) ext else "file").uppercase().take(4),
                    color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, maxLines = 1)
                Text(
                    listOf(size, mime).filter { it.isNotBlank() }.joinToString(" · ").ifBlank { "ready" },
                    fontSize = 10.sp, color = Txt3, maxLines = 1
                )
            }
            Icon(
                Icons.Filled.Refresh, "Replace", tint = Txt3,
                modifier = Modifier.size(18.dp).clickable(onClick = onReplace)
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                Icons.Filled.Close, "Remove", tint = Rose,
                modifier = Modifier.size(18.dp).clickable(onClick = onRemove)
            )
        }
        if (isImage) {
            Spacer(Modifier.height(8.dp))
            AsyncImage(
                model = uri, contentDescription = name, contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(AuroraGradient)
                    .clickable {
                        ThemeX.setWallpaper(ctx, uri)
                        ThemeX.themeFromImage(ctx, uri)
                        android.widget.Toast.makeText(
                            ctx, "App background + button colors updated", android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Set as app background + theme", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
        if (isCode && codeHead.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFF14121F))
                    .clickable { expanded = !expanded }.padding(10.dp)
            ) {
                Text(
                    codeHead,
                    color = Color(0xFFA7F3D0), fontSize = 10.sp, lineHeight = 14.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    maxLines = if (expanded) 60 else 8
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (expanded) "Tap to collapse" else "Tap to expand preview",
                    color = Color(0xFF8C85A6), fontSize = 9.sp, fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
