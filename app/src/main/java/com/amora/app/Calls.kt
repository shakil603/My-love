package com.amora.app

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

/* ---------- shared pieces ---------- */

private fun fmt(sec: Int) = "%d:%02d".format(sec / 60, sec % 60)

@Composable
private fun CallButton(
    icon: ImageVector,
    label: String,
    active: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(if (active) Color.White else Color.White.copy(alpha = 0.14f))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, label, tint = if (active) Color(0xFF1A0B2E) else Color.White, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, color = Color.White.copy(alpha = 0.65f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EndCallButton(size: Int = 60, onClick: () -> Unit) {
    Box(
        Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color(0xFFF43F5E))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.CallEnd, "End call", tint = Color.White, modifier = Modifier.size((size * 0.45).dp))
    }
}

@Composable
private fun EncryptedChip(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.14f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(Icons.Filled.Lock, null, tint = Color.White, modifier = Modifier.size(11.dp))
        Spacer(Modifier.width(5.dp))
        Text(text, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
    }
}

/* ---------- Audio call ---------- */

@Composable
fun AudioCallScreen(userId: String, onBack: () -> Unit, go: (String) -> Unit = {}) {
    val u = Mock.user(userId)
    var sec by remember { mutableIntStateOf(0) }
    var muted by remember { mutableStateOf(false) }
    var speaker by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { while (true) { delay(1000); sec++ } }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A0B2E), Color(0xFF2E1065), InkDeep)))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(26.dp))
            EncryptedChip("End-to-end encrypted · Voice")
            Spacer(Modifier.height(46.dp))
            Box(contentAlignment = Alignment.Center) {
                val t = rememberInfiniteTransition(label = "pulse")
                val a by t.animateFloat(
                    0.08f, 0.35f,
                    infiniteRepeatable(tween(1100), RepeatMode.Reverse), label = "a"
                )
                Box(Modifier.size(196.dp).clip(CircleShape).border(2.dp, Color.White.copy(alpha = a), CircleShape))
                Box(Modifier.size(162.dp).clip(CircleShape).border(2.dp, Color.White.copy(alpha = a * 1.8f), CircleShape))
                Avatar(u.avatar, 128, ringed = true)
            }
            Spacer(Modifier.height(26.dp))
            Text(u.name, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(6.dp))
            Text(fmt(sec), color = Mint, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(4.dp))
            Text("Voice call · Crystal-clear HD", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.07f))
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallButton(if (muted) Icons.Filled.MicOff else Icons.Filled.Mic, if (muted) "Unmute" else "Mute", active = muted) { muted = !muted }
                CallButton(Icons.Filled.VolumeUp, "Speaker", active = speaker) { speaker = !speaker }
                CallButton(Icons.Filled.Videocam, "Video") { go("video/${u.id}") }
                CallButton(Icons.Filled.ChatBubble, "Message") { go("chat/${u.id}") }
            }
            Spacer(Modifier.height(18.dp))
            EndCallButton(onClick = onBack)
            Spacer(Modifier.height(26.dp))
        }
    }
}

/* ---------- Video call ---------- */

@Composable
fun VideoCallScreen(userId: String = "1", onBack: () -> Unit) {
    val u = Mock.user(userId)
    var sec by remember { mutableIntStateOf(0) }
    var muted by remember { mutableStateOf(false) }
    var camOff by remember { mutableStateOf(false) }
    var captions by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) { while (true) { delay(1000); sec++ } }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AsyncImage(
            model = u.cover.replace("w=900", "w=800"),
            contentDescription = null, contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        AsyncImage(
            model = u.avatar.replace("w=400", "w=800"),
            contentDescription = null, contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color(0x99000000), 0.25f to Color.Transparent,
                        0.65f to Color.Transparent, 1f to Color(0xCC000000)
                    )
                )
        )
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(u.name, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Lock, null, tint = Mint, modifier = Modifier.size(10.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${fmt(sec)} · encrypted · HD", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.PersonAdd, "Add person", tint = Color.White, modifier = Modifier.size(17.dp)) }
            }
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                if (captions) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.55f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Filled.ClosedCaption, null, tint = Mint, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            "“…so I'll see you at the airport?”",
                            color = Color.White, fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Spacer(Modifier.weight(1f))
                }
                Spacer(Modifier.width(10.dp))
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300",
                    contentDescription = "You", contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 92.dp, height = 126.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(2.5.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(18.dp))
                )
            }
            Spacer(Modifier.height(14.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color(0xB8141023))
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CallButton(Icons.Filled.Cameraswitch, "Flip") {}
                CallButton(if (muted) Icons.Filled.MicOff else Icons.Filled.Mic, if (muted) "Unmute" else "Mute", active = muted) { muted = !muted }
                EndCallButton(size = 56, onClick = onBack)
                CallButton(if (camOff) Icons.Filled.VideocamOff else Icons.Filled.Videocam, "Camera", active = camOff) { camOff = !camOff }
                CallButton(Icons.Filled.ClosedCaption, "Captions", active = captions) { captions = !captions }
            }
        }
    }
}

/* ---------- Incoming call ---------- */

@Composable
fun IncomingCallScreen(userId: String = "1", onAccept: () -> Unit, onDecline: () -> Unit) {
    val u = Mock.user(userId)
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A0B2E), Color(0xFF2E1065), InkDeep)))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            EncryptedChip("End-to-end encrypted · Amora Video")
            Spacer(Modifier.height(40.dp))
            Box(contentAlignment = Alignment.Center) {
                val t = rememberInfiniteTransition(label = "ring")
                val a by t.animateFloat(
                    0.1f, 0.4f,
                    infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "ra"
                )
                Box(Modifier.size(190.dp).clip(CircleShape).border(2.dp, Color.White.copy(alpha = a), CircleShape))
                Box(Modifier.size(158.dp).clip(CircleShape).border(2.dp, Color.White.copy(alpha = a * 1.6f), CircleShape))
                Avatar(u.avatar, 122, ringed = true)
            }
            Spacer(Modifier.height(28.dp))
            Text(u.name, color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(7.dp))
            Text("Incoming video call…", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 13.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.ChatBubble, null, tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Message", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 13.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Filled.Alarm, null, tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Remind me", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.weight(1f))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EndCallButton(size = 62, onClick = onDecline)
                    Spacer(Modifier.height(8.dp))
                    Text("Decline", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(74.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Color(0xFF10B981), Mint)))
                            .clickable(onClick = onAccept),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Videocam, "Accept", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Accept", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
