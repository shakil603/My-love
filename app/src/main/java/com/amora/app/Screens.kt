package com.amora.app

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/* ---------- shared building blocks ---------- */

@Composable
fun Avatar(url: String, size: Int, ringed: Boolean = false) {
    val base = Modifier
        .size(size.dp)
        .clip(CircleShape)
    val mod = if (ringed) {
        Modifier
            .size(size.dp)
            .border(2.dp, AuroraGradient, CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
    } else base
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = mod
    )
}

@Composable
fun GradientButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(17.dp))
            .background(AuroraGradient)
            .clickable(onClick = onClick)
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
    }
}

@Composable
fun FileChip(name: String, meta: String, light: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (light) Color.White.copy(alpha = 0.95f) else Cloud)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(width = 38.dp, height = 46.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Brush.linearGradient(listOf(Color(0xFFF43F5E), Color(0xFFC2185B)))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                name.substringAfterLast('.').uppercase().take(4),
                color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Ink)
            Text(meta, fontSize = 10.sp, color = Txt3)
        }
    }
}

/* ---------- 01 · Onboarding ---------- */

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(NightGradient)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 26.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(AuroraGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.height(22.dp))
            Text(
                "Find love.\nKeep friends.\nGet things done.",
                color = Color.White, fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold, lineHeight = 40.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Messaging, dating, social and work — one secure home for everything, in 40+ languages.",
                color = Color.White.copy(alpha = 0.72f), fontSize = 14.sp, lineHeight = 21.sp
            )
            Spacer(Modifier.height(26.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(19.dp))
                    .background(Color.White)
                    .clickable(onClick = onDone)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Create account", color = Ink, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(19.dp))
                    .border(1.5.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(19.dp))
                    .clickable(onClick = onDone)
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("I already have an account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.height(18.dp))
            Text(
                "\uD83C\uDF10 English · বাংলা · Español · العربية · 中文 · +37",
                color = Color.White.copy(alpha = 0.55f), fontSize = 11.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ---------- 02 · Feed ---------- */

@Composable
fun FeedScreen() {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("amora", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Orchid)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.Search, null, tint = Ink)
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Filled.Notifications, null, tint = Ink)
            }
        }
        item {
            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                items(Mock.stories) { story ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (story.isAdd) {
                            Box(
                                Modifier
                                    .size(58.dp)
                                    .clip(CircleShape)
                                    .background(AuroraSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Add, null, tint = Orchid)
                            }
                        } else {
                            Avatar(story.url, 58, ringed = true)
                        }
                        Spacer(Modifier.height(5.dp))
                        Text(story.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Txt2)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
        }
        items(Mock.posts) { post ->
            PostCard(post)
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
fun PostCard(post: Post) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column {
            Row(
                Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(post.avatar, 38)
                Spacer(Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(post.author, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        if (post.verified) {
                            Spacer(Modifier.width(4.dp))
                            Text("✔", color = Sky, fontSize = 12.sp)
                        }
                    }
                    Text(post.meta, fontSize = 11.sp, color = Txt3)
                }
            }
            Text(
                post.text,
                fontSize = 13.sp, lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
            Spacer(Modifier.height(10.dp))
            post.image?.let {
                AsyncImage(
                    model = it, contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                )
            }
            post.fileName?.let {
                Box(Modifier.padding(horizontal = 14.dp)) {
                    FileChip(it, post.fileMeta ?: "")
                }
            }
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Favorite, null, tint = Rose, modifier = Modifier.size(19.dp))
                Spacer(Modifier.width(6.dp))
                Text(post.likes, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Rose)
                Spacer(Modifier.width(18.dp))
                Text("\uD83D\uDCAC ${post.comments}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Txt2)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.Send, null, tint = Txt2, modifier = Modifier.size(18.dp))
            }
        }
    }
}

/* ---------- 03 · Discover ---------- */

@Composable
fun DiscoverScreen() {
    val (photo, name, meta) = Mock.discover
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Discover", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
            Spacer(Modifier.weight(1f))
            Text("\uD83D\uDC98 Dating", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(AuroraGradient)
                    .padding(horizontal = 14.dp, vertical = 8.dp))
        }
        Box(
            Modifier
                .padding(horizontal = 18.dp)
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(28.dp))
        ) {
            AsyncImage(
                model = photo, contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.45f to Color.Transparent,
                            1f to Color(0xCC0A0714)
                        )
                    )
            )
            Text(
                "✦ 94% match", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(14.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.25f))
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            )
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(18.dp)
            ) {
                Text(name, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                Text(meta, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("\uD83C\uDFA8 Design", "\uD83C\uDFA7 Afrobeats", "✈️ Travel").forEach {
                        Text(
                            it, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.White.copy(alpha = 0.18f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = Color.White, shadowElevation = 4.dp) {
                IconButton(onClick = {}) { Icon(Icons.Filled.Close, null, tint = Txt3) }
            }
            Spacer(Modifier.width(18.dp))
            Box(
                Modifier
                    .size(66.dp)
                    .clip(CircleShape)
                    .background(AuroraGradient)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(18.dp))
            Surface(shape = CircleShape, color = Color.White, shadowElevation = 4.dp) {
                IconButton(onClick = {}) { Icon(Icons.Filled.Star, null, tint = Amber) }
            }
        }
    }
}

/* ---------- 04 · Chats ---------- */

@Composable
fun ChatsScreen(onOpenChat: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Chats", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.Search, null, tint = Ink)
            }
        }
        items(Mock.chats) { chat ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChat(chat.id) }
                    .padding(horizontal = 16.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Avatar(chat.avatar, 50, ringed = chat.unread > 0)
                    if (chat.online) {
                        Box(
                            Modifier
                                .align(Alignment.BottomEnd)
                                .size(13.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(Mint)
                            )
                        }
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(chat.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, maxLines = 1)
                    Text(
                        chat.last, fontSize = 12.sp, color = Txt2,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        chat.time, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = if (chat.unread > 0) Orchid else Txt3
                    )
                    if (chat.unread > 0) {
                        Spacer(Modifier.height(4.dp))
                        Box(
                            Modifier
                                .clip(CircleShape)
                                .background(AuroraGradient)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text("${chat.unread}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

/* ---------- 05 · Conversation ---------- */

@Composable
fun ConversationScreen(chatId: String, onBack: () -> Unit) {
    val chat = Mock.chats.firstOrNull { it.id == chatId } ?: Mock.chats.first()
    var input by remember { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFAFE))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
            Avatar(chat.avatar, 38)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(chat.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                Text(
                    if (chat.online) "Online · E2E encrypted \uD83D\uDD12" else "E2E encrypted \uD83D\uDD12",
                    fontSize = 10.sp, color = Mint, fontWeight = FontWeight.Bold
                )
            }
        }
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp)
        ) {
            items(Mock.messages) { msg ->
                MessageBubble(msg)
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(AuroraSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, null, tint = Orchid, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(8.dp))
            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Message…", fontSize = 13.sp, color = Txt3) },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(50)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Cloud,
                    unfocusedContainerColor = Cloud,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AuroraGradient)
                    .clickable { input = "" },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Send, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun MessageBubble(msg: Message) {
    val align = if (msg.mine) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (msg.mine)
        RoundedCornerShape(19.dp, 19.dp, 6.dp, 19.dp)
    else
        RoundedCornerShape(19.dp, 19.dp, 19.dp, 6.dp)
    Box(Modifier.fillMaxWidth(), contentAlignment = align) {
        Column(
            Modifier
                .fillMaxWidth(0.8f)
                .then(
                    if (msg.mine) Modifier
                        .clip(shape)
                        .background(AuroraGradient)
                    else Modifier
                        .clip(shape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFEFEBF8), shape)
                )
                .padding(if (msg.image != null) 5.dp else 11.dp)
        ) {
            msg.text?.let {
                Text(
                    it, fontSize = 13.sp, lineHeight = 19.sp,
                    color = if (msg.mine) Color.White else Ink
                )
            }
            msg.image?.let {
                AsyncImage(
                    model = it, contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(14.dp))
                )
            }
            msg.fileName?.let {
                FileChip(it, msg.fileMeta ?: "", light = msg.mine)
            }
            Spacer(Modifier.height(3.dp))
            Text(
                if (msg.mine) "${msg.time} ✓✓" else msg.time,
                fontSize = 9.sp,
                color = if (msg.mine) Color.White.copy(alpha = 0.85f) else Txt3,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

/* ---------- 06 · Spaces ---------- */

@Composable
fun SpacesScreen() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 20.dp)) {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Spaces", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.Add, null, tint = Ink)
            }
        }
        item {
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(VaultGradient)
                    .padding(16.dp)
            ) {
                Text("MY VAULT · SYNCED ✓", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB7A8FF), letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("34.2 GB", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.weight(1f))
                    Text("of 100 GB · cloud + this device", color = Color(0xFFB7B2CC), fontSize = 10.sp)
                }
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { 0.34f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Orchid,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )
                Spacer(Modifier.height(10.dp))
                Text("\uD83D\uDDBC 2,418 photos   \uD83D\uDCC4 312 docs   \uD83C\uDFAC 86 videos", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
        }
        item {
            val cards = listOf(
                Triple("Personal", "Notes, journal, goals · 3 reminders today", Rose),
                Triple("Office", "Project Phoenix · stand-up in 20 min", Indigo),
                Triple("Business", "Storefront · 12 orders · \$1,840 this week", Mint),
                Triple("Learning", "UX course · 68% · certificate soon \uD83C\uDF93", Amber)
            )
            Column(Modifier.padding(horizontal = 16.dp)) {
                for (row in cards.chunked(2)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (card in row) {
                            Column(
                                Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .padding(14.dp)
                            ) {
                                Box(
                                    Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .background(card.third.copy(alpha = 0.18f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(card.third)
                                    )
                                }
                                Spacer(Modifier.height(9.dp))
                                Text(card.first, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                Text(card.second, fontSize = 10.sp, color = Txt3, lineHeight = 15.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent files", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Text("See all", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Orchid)
            }
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                val files = listOf(
                    Triple("Q3-Report-final.pdf", "Office · 2 h ago · shared with 8", "✓ synced"),
                    Triple("brand-refresh.fig", "Personal · yesterday · preview ready", "✓ synced"),
                    Triple("orders-week-45.xlsx", "Business · Mon · auto-backup", "↑ device")
                )
                files.forEach { f ->
                    Row(Modifier.padding(vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Text("\uD83D\uDCC4", fontSize = 13.sp) }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(f.first, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                            Text(f.second, fontSize = 10.sp, color = Txt3)
                        }
                        Text(f.third, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = if (f.third.startsWith("✓")) Mint else Amber)
                    }
                }
            }
        }
    }
}

/* ---------- 07 · Profile ---------- */

@Composable
fun ProfileScreen() {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 20.dp)) {
        item {
            Box(Modifier.fillMaxWidth()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(AuroraGradient)
                )
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 100.dp)
                ) {
                    Box(
                        Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Avatar("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300", 88)
                    }
                }
            }
        }
        item {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))
                Text("Shakil Rahman ✔", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                Text("@shakil · Mymensingh, Bangladesh \uD83C\uDDE7\uD83C\uDDE9", fontSize = 12.sp, color = Txt2, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("1,284" to "FRIENDS", "48.2k" to "FOLLOWERS", "312" to "POSTS").forEach { (num, label) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(num, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                            Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Txt3)
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                Row(
                    Modifier.padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GradientButton("Edit profile", Modifier.weight(1f))
                    Box(
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(17.dp))
                            .border(1.5.dp, Mist, RoundedCornerShape(17.dp))
                            .background(Color.White)
                            .padding(vertical = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Share", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Ink)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
        item {
            Column(Modifier.padding(horizontal = 14.dp)) {
                for (row in Mock.profileGrid.chunked(3)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        row.forEach { url ->
                            AsyncImage(
                                model = url, contentDescription = null, contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}
