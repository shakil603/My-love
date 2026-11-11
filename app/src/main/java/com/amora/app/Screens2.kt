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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            .background(Color.White)
            .padding(14.dp)
    ) { content() }
}

@Composable
fun ToggleRow(emoji: String, title: String, sub: String, initial: Boolean) {
    var on by remember { mutableStateOf(initial) }
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
        ) { Text(emoji, fontSize = 15.sp) }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            Text(sub, fontSize = 10.sp, color = Txt3)
        }
        Switch(checked = on, onCheckedChange = { on = it })
    }
}

/* ---------- Sign up & OTP ---------- */

@Composable
fun SignupScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
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
            Text("17 1234 5678", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(Modifier.height(14.dp))
        GradientButton("Continue", Modifier.fillMaxWidth(), onClick = onNext)
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
            ) { Text("✉️  Email", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp) }
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
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(10.dp))
        IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
        Spacer(Modifier.height(10.dp))
        Text("Enter the code", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text("We sent a 6-digit code to +880 17•• ••• 678", fontSize = 13.sp, color = Txt2)
        Spacer(Modifier.height(26.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            listOf("7", "3", "9", "", "", "").forEachIndexed { i, d ->
                Box(
                    Modifier
                        .size(width = 44.dp, height = 54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .then(
                            if (i == 3) Modifier.border(1.8.dp, Orchid, RoundedCornerShape(14.dp))
                            else Modifier.background(Cloud)
                        ),
                    contentAlignment = Alignment.Center
                ) { Text(d, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold) }
            }
        }
        Spacer(Modifier.height(18.dp))
        Text(
            "Resend code in 00:24",
            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Txt3,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(18.dp))
        GradientButton("Verify", Modifier.fillMaxWidth(), onClick = onNext)
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
                                .background(Color.White)
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
                ) { Text("Reply privately…", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp) }
                Box(
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Favorite, null, tint = Rose, modifier = Modifier.size(20.dp)) }
                Box(
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Send, null, tint = Color.White, modifier = Modifier.size(18.dp)) }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun ReelsScreen(onBack: () -> Unit) {
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
                        Icon(Icons.Filled.Favorite, null, tint = Rose, modifier = Modifier.size(28.dp))
                        Text("12.4k", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Email, null, tint = Color.White, modifier = Modifier.size(26.dp))
                        Text("482", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Share, null, tint = Color.White, modifier = Modifier.size(24.dp))
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
fun NotificationsScreen(onBack: () -> Unit) {
    val notifs = listOf(
        Notif("\uD83D\uDC98", "Amara matched with you — 94% compatible", "8 min ago", "Say hi"),
        Notif("❤️", "Sofia and 213 others liked your post", "26 min ago", null),
        Notif("\uD83C\uDF99", "Rafi sent you a voice message", "1 h ago · 0:42", "Play"),
        Notif("\uD83D\uDCC5", "Stand-up in 20 min · Project Phoenix", "6 people joining", "Join"),
        Notif("\uD83D\uDCC4", "Daniel shared Launch-Playbook-2026.pdf with you", "2 h ago · preview ready ✓", null),
        Notif("\uD83C\uDF93", "UX course — module 7 unlocked", "68% complete · keep going!", null)
    )
    Column(Modifier.fillMaxSize().background(Color.White).statusBarsPadding()) {
        ScreenHeader("Activity", onBack)
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
fun SearchScreen(onBack: () -> Unit) {
    var q by remember { mutableStateOf("design workshop") }
    Column(Modifier.fillMaxSize().background(Color.White).statusBarsPadding()) {
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
            listOf("All" to true, "People" to false, "Posts" to false, "Files" to false, "Groups" to false).forEach { (c, on) ->
                Text(
                    c, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                    color = if (on) Color.White else Txt2,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (on) Ink else Cloud)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item {
                Text("PEOPLE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
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
                            .padding(horizontal = 14.dp, vertical = 9.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
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
                Text("SHOPS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF047857)))),
                        contentAlignment = Alignment.Center
                    ) { Text("\uD83D\uDCF7", fontSize = 18.sp) }
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Shakil Prints & Presets", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Text("⭐ 4.9 · prints & design assets", fontSize = 10.sp, color = Txt3)
                    }
                    Text(
                        "Visit", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Ink,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Cloud)
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
    Column(Modifier.fillMaxSize().background(Color.White).statusBarsPadding().navigationBarsPadding()) {
        ScreenHeader("Comments · 186", onBack)
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp)) {
            val comments = listOf(
                Triple("Rafi Ahmed", "This light is unreal \uD83D\uDE0D Which lens did you use?", "2 h · ❤ 48" to "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200"),
                Triple("Sofia Martínez (Author)", "@Rafi 35mm f/1.4 — golden hour did the rest ✨", "1 h · ❤ 21" to "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200"),
                Triple("Yuki Tanaka", "Adding Costa Brava to my list right now \uD83E\uDDF3", "58 m · ❤ 12" to "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"),
                Triple("Omar Haddad", "\uD83D\uDCCE attached my edit preset pack for this vibe", "31 m · ❤ 34" to "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200")
            )
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
                .background(Color.White)
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
            Icon(Icons.Filled.Send, null, tint = Orchid)
        }
    }
}

/* ---------- Settings & Safety ---------- */

@Composable
fun SettingsScreen(onBack: () -> Unit, onSafety: () -> Unit, onLanguage: () -> Unit = {}) {
    LazyColumn(Modifier.fillMaxSize().background(Cloud).statusBarsPadding(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item { ScreenHeader("Settings", onBack) }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                WhiteCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200", 50)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Shakil Rahman", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            Text("@shakil · amora.id/shakil", fontSize = 11.sp, color = Txt3)
                        }
                        Text("Edit", color = Orchid, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text("PRIVACY & SECURITY", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(7.dp))
                WhiteCard {
                    ToggleRow("\uD83D\uDD12", "End-to-end encryption", "Default for all chats & calls", true)
                    ToggleRow("\uD83D\uDC98", "Dating visibility", "Hidden from contacts & coworkers", true)
                    ToggleRow("\uD83D\uDC41", "Read receipts", "Off for non-contacts", false)
                    Row(
                        Modifier.fillMaxWidth().clickable(onClick = onSafety).padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Text("\uD83D\uDEE1", fontSize = 15.sp) }
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
                            Text("Language · ভাষা", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("14 languages · also in Android Settings", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                    Row(Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Ink),
                            contentAlignment = Alignment.Center
                        ) { Text("\uD83C\uDF19", fontSize = 15.sp) }
                        Spacer(Modifier.width(11.dp))
                        Text("Appearance", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                        Text("Auto ›", color = Txt2, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                    Row(Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Text("☁️", fontSize = 15.sp) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Storage & sync", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("Auto-backup on this device", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                WhiteCard {
                    Text(
                        "Sign out", color = Color(0xFFF43F5E), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun SafetyScreen(onBack: () -> Unit) {
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
                    ToggleRow("\uD83D\uDD76", "Incognito mode", "Only people you like can see you", true)
                    Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Text("\uD83E\uDEE5", fontSize = 15.sp) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Block list", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("3 blocked · also hides from contacts", fontSize = 10.sp, color = Txt3)
                        }
                        Text("›", color = Txt3, fontSize = 18.sp)
                    }
                    Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Text("\uD83D\uDEA8", fontSize = 15.sp) }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Emergency contacts", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text("Share live location on a date · 2 added", fontSize = 10.sp, color = Txt3)
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
                    Text("\uD83D\uDEA9", fontSize = 17.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Report someone", color = Color(0xFFBE123C), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        Text("Anonymous · reviewed within 24 h", color = Color(0xFFE11D48), fontSize = 10.sp)
                    }
                    Text(
                        "Report", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFF43F5E))
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
fun CalendarScreen(onBack: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().background(Color.White).statusBarsPadding(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item { ScreenHeader("November", onBack) }
        item {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("MON" to "10", "TUE" to "11", "WED" to "12", "THU" to "13", "FRI" to "14").forEachIndexed { i, (d, n) ->
                    Column(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .then(if (i == 1) Modifier.background(AuroraGradient) else Modifier)
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(d, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = if (i == 1) Color.White.copy(alpha = 0.85f) else Txt3)
                        Text(n, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = if (i == 1) Color.White else Ink)
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
                                .background(Color.White)
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
    Column(Modifier.fillMaxSize().background(Cloud).statusBarsPadding()) {
        ScreenHeader("Project Phoenix", onBack, sub = "Office space · 6 members · due Nov 28")
        Row(
            Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                Modifier
                    .width(240.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEFEDF6))
                    .padding(10.dp)
            ) {
                Text("To do · 4", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                WhiteCard {
                    Text("HIGH", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFBE123C),
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFFFE4E6)).padding(horizontal = 8.dp, vertical = 3.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Final pricing page copy", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.height(7.dp))
                    Text("\uD83D\uDCCE 2 · \uD83D\uDCAC 5", fontSize = 9.sp, color = Txt3, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                WhiteCard {
                    Text("DESIGN", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = Indigo,
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFE0E7FF)).padding(horizontal = 8.dp, vertical = 3.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Empty-state illustrations", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.height(7.dp))
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1519904981063-b0cf448d479e?w=300",
                        contentDescription = null, contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(9.dp))
                    )
                }
            }
            Column(
                Modifier
                    .width(240.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEFEDF6))
                    .padding(10.dp)
            ) {
                Text("In progress · 3", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                WhiteCard {
                    Text("SPRINT 4", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFA21CAF),
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFFAE8FF)).padding(horizontal = 8.dp, vertical = 3.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Q3 forecast deck", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.height(7.dp))
                    FileChip("Q3-Forecast.xlsx", "2.1 MB · live preview")
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.7f },
                        modifier = Modifier.fillMaxWidth().height(5.dp).clip(RoundedCornerShape(4.dp)),
                        color = Orchid, trackColor = Mist
                    )
                    Spacer(Modifier.height(5.dp))
                    Text("70% · today", fontSize = 9.sp, color = Txt3, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                WhiteCard {
                    Text("REVIEW", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF92400E),
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFFEF3C7)).padding(horizontal = 8.dp, vertical = 3.dp))
                    Spacer(Modifier.height(6.dp))
                    Text("Onboarding flow v2", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(Modifier.height(7.dp))
                    Text("\uD83D\uDCAC 12", fontSize = 9.sp, color = Txt3, fontWeight = FontWeight.Bold)
                }
            }
            Column(
                Modifier
                    .width(160.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEFEDF6))
                    .padding(10.dp)
            ) {
                Text("Done ✓", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                WhiteCard { Text("Brand refresh", fontSize = 11.sp, color = Txt3, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

/* ---------- Upload ---------- */

@Composable
fun UploadScreen(onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        ScreenHeader("Share anything", onBack, sub = "Any file type · previews before sending")
        Column(Modifier.padding(horizontal = 18.dp)) {
            val cats = listOf(
                Triple("Gallery", Rose, Color(0xFFFF8E53)),
                Triple("Camera", Violet, Orchid),
                Triple("Document", Indigo, Sky),
                Triple("Audio", Color(0xFF10B981), Mint),
                Triple("Video", Color(0xFFF59E0B), Color(0xFFF97316)),
                Triple("Code", Color(0xFF64748B), Color(0xFF334155)),
                Triple("Archive", Color(0xFFEC4899), Color(0xFFD946EF)),
                Triple("Any file", Ink, Txt2)
            )
            for (row in cats.chunked(4)) {
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    row.forEach { (label, c1, c2) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Brush.linearGradient(listOf(c1, c2))),
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Filled.Add, null, tint = Color.White, modifier = Modifier.size(20.dp)) }
                            Spacer(Modifier.height(6.dp))
                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Txt2)
                        }
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ready to send · 4 items", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                Text("All previews ✓", fontSize = 10.sp, color = Txt3, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1519904981063-b0cf448d479e?w=200",
                    contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(14.dp))
                )
                Box(
                    Modifier.size(64.dp).clip(RoundedCornerShape(14.dp)).background(Ink),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.PlayArrow, null, tint = Color.White) }
                Box(
                    Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFFF43F5E), Color(0xFFC2185B)))),
                    contentAlignment = Alignment.Center
                ) { Text("PDF\n12p", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold) }
                Box(
                    Modifier.size(64.dp).clip(RoundedCornerShape(14.dp)).background(Cloud),
                    contentAlignment = Alignment.Center
                ) { Text("ZIP\n48", color = Txt2, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold) }
            }
            Spacer(Modifier.height(16.dp))
            GradientButton("Send 4 files", Modifier.fillMaxWidth(), onClick = onBack)
        }
    }
}
