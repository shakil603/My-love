package com.amora.app

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

/* ---------- Chats list (search + filters + pinned) ---------- */

@Composable
fun ChatsScreen(onOpenChat: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }
    val filtered = Mock.chats.filter { c ->
        (query.isBlank() || c.name.contains(query, ignoreCase = true)) &&
            when (filter) {
                "Matches" -> c.kind == "match"
                "Groups" -> c.kind == "group" || c.kind == "work"
                "Work" -> c.kind == "work"
                else -> true
            }
    }.sortedByDescending { it.pinned }
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
                Icon(Icons.Filled.Add, null, tint = Ink)
            }
        }
        item {
            TextField(
                value = query, onValueChange = { query = it },
                placeholder = { Text("Search chats…", fontSize = 12.sp, color = Txt3) },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = Txt3) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Cloud, unfocusedContainerColor = Cloud,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                listOf("All", "Matches", "Groups", "Work").forEach { f ->
                    val on = filter == f
                    Text(
                        f, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (on) Color.White else Txt2,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (on) Ink else Cloud)
                            .clickable { filter = f }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }
        itemsIndexed(filtered) { _, chat ->
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
                        ) { Box(Modifier.size(9.dp).clip(CircleShape).background(Mint)) }
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(chat.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, maxLines = 1)
                        if (chat.pinned) {
                            Spacer(Modifier.width(5.dp))
                            Text("\uD83D\uDCCC", fontSize = 10.sp)
                        }
                    }
                    Text(chat.last, fontSize = 12.sp, color = Txt2, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                        ) { Text("${chat.unread}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold) }
                    }
                }
            }
        }
    }
}

/* ---------- Conversation (live send, reactions, reply, voice, attach) ---------- */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationScreen(chatId: String, onBack: () -> Unit, go: (String) -> Unit = {}) {
    val chat = Mock.chats.firstOrNull { it.id == chatId } ?: Mock.chats.first()
    val isGroup = chat.kind == "group" || chat.kind == "work"
    val msgs = remember { (if (isGroup) Mock.groupMessages else Mock.messages).toMutableStateList() }
    var input by remember { mutableStateOf("") }
    var typing by remember { mutableStateOf(false) }
    var replyTo by remember { mutableStateOf<Message?>(null) }
    var pickerFor by remember { mutableIntStateOf(-1) }
    var showAttach by remember { mutableStateOf(false) }
    var vanish by remember { mutableStateOf(false) }
    var sendCount by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    LaunchedEffect(sendCount) {
        if (sendCount > 0) {
            delay(900)
            typing = true
            delay(1500)
            typing = false
            msgs.add(
                Message(
                    false, text = Mock.replies[(sendCount - 1) % Mock.replies.size], time = "now",
                    sender = if (isGroup) "Rafi Ahmed" else null,
                    senderAvatar = if (isGroup) "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200" else null
                )
            )
        }
    }
    LaunchedEffect(msgs.size, typing) {
        listState.animateScrollToItem(msgs.size + if (typing) 1 else 0)
    }

    fun send(msg: Message) {
        msgs.add(msg)
        replyTo = null
        showAttach = false
        sendCount++
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFAFE))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        /* header */
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
            Column(Modifier.weight(1f)) {
                Text(chat.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, maxLines = 1)
                Text(
                    when {
                        typing -> if (isGroup) "Rafi is typing…" else "typing…"
                        isGroup -> "6 members · E2E encrypted \uD83D\uDD12"
                        chat.online -> "Online · E2E encrypted \uD83D\uDD12"
                        else -> "E2E encrypted \uD83D\uDD12"
                    },
                    fontSize = 10.sp,
                    color = if (typing) Orchid else Mint,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                "⏱", fontSize = 16.sp,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (vanish) Color(0xFFF6E7FF) else Color.Transparent)
                    .clickable { vanish = !vanish }
                    .padding(8.dp)
            )
            IconButton(onClick = { go("incoming") }) { Icon(Icons.Filled.Call, null, tint = Ink) }
            Text(
                "\uD83C\uDFA5", fontSize = 16.sp,
                modifier = Modifier
                    .clickable { go("call") }
                    .padding(8.dp)
            )
        }
        if (vanish) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF6E7FF))
                    .padding(horizontal = 16.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "⏱ Disappearing messages ON · new messages vanish after 24 h",
                    fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA21CAF)
                )
            }
        }
        /* messages */
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            itemsIndexed(msgs) { i, msg ->
                Column {
                    if (i == 0) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(
                                "Today", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Txt3,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Cloud)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(Modifier.height(9.dp))
                    }
                    MessageBubble(
                        msg = msg,
                        onLongPress = { pickerFor = i }
                    )
                }
            }
            if (typing) {
                itemsIndexed(listOf(0)) { _, _ -> TypingBubble() }
            }
        }
        /* reaction picker */
        if (pickerFor >= 0) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("❤️", "\uD83D\uDE02", "\uD83D\uDC4D", "\uD83D\uDE2E", "\uD83D\uDE22", "\uD83D\uDD25").forEach { e ->
                    Text(
                        e, fontSize = 22.sp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                msgs[pickerFor] = msgs[pickerFor].copy(reaction = e)
                                pickerFor = -1
                            }
                            .padding(6.dp)
                    )
                }
                Text(
                    "↩ Reply", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Orchid,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(AuroraSoft)
                        .clickable {
                            replyTo = msgs[pickerFor]
                            pickerFor = -1
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
        /* reply bar */
        replyTo?.let { r ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .width(3.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Orchid)
                )
                Spacer(Modifier.width(9.dp))
                Column(Modifier.weight(1f)) {
                    Text("Replying to", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Orchid)
                    Text(
                        r.text ?: r.fileName ?: if (r.voice) "Voice message · ${r.voiceLen}" else "Photo",
                        fontSize = 11.sp, color = Txt2, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    Icons.Filled.Close, null, tint = Txt3,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { replyTo = null }
                )
            }
        }
        /* attach quick row */
        if (showAttach) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AttachOption("\uD83D\uDCF7", "Photo") {
                    send(Message(true, image = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=500", time = "now", quoted = replyTo?.text))
                }
                AttachOption("\uD83D\uDCC4", "PDF") {
                    send(Message(true, fileName = "shared-notes.pdf", fileMeta = "6 pages · 1.1 MB · preview ✓", time = "now"))
                }
                AttachOption("\uD83C\uDF99", "Voice") {
                    send(Message(true, voice = true, voiceLen = "0:07", time = "now"))
                }
                AttachOption("\uD83D\uDCCD", "Location") {
                    send(Message(true, text = "\uD83D\uDCCD Live location shared · 42 min", time = "now"))
                }
                AttachOption("\uD83D\uDCC1", "Any file") { go("upload") }
            }
        }
        /* emoji quick bar */
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 18.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("❤️", "\uD83D\uDE02", "\uD83D\uDE0D", "\uD83D\uDC4D", "\uD83C\uDF89", "\uD83D\uDE2E", "\uD83D\uDE4F", "\uD83D\uDD25").forEach { e ->
                Text(e, fontSize = 18.sp, modifier = Modifier.clickable { input += e })
            }
        }
        /* input row */
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
                    .background(if (showAttach) AuroraGradient else AuroraSoft)
                    .clickable { showAttach = !showAttach },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Add, null,
                    tint = if (showAttach) Color.White else Orchid,
                    modifier = Modifier.size(20.dp)
                )
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
                    .clickable {
                        if (input.isBlank()) {
                            send(Message(true, voice = true, voiceLen = "0:05", time = "now"))
                        } else {
                            send(
                                Message(
                                    true, text = input.trim(), time = "now",
                                    quoted = replyTo?.let { it.text ?: it.fileName ?: "Voice message" }
                                )
                            )
                            input = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (input.isBlank()) Text("\uD83C\uDF99", fontSize = 16.sp)
                else Icon(Icons.Filled.Send, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun AttachOption(emoji: String, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Cloud),
            contentAlignment = Alignment.Center
        ) { Text(emoji, fontSize = 18.sp) }
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Txt2)
    }
}

@Composable
fun TypingBubble() {
    val t = rememberInfiniteTransition(label = "typing")
    val a by t.animateFloat(
        0.25f, 1f,
        infiniteRepeatable(tween(450), RepeatMode.Reverse),
        label = "alpha"
    )
    Row(
        Modifier
            .clip(RoundedCornerShape(19.dp, 19.dp, 19.dp, 6.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEFEBF8), RoundedCornerShape(19.dp, 19.dp, 19.dp, 6.dp))
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        repeat(3) {
            Box(
                Modifier
                    .size(7.dp)
                    .alpha(a)
                    .clip(CircleShape)
                    .background(Orchid)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(msg: Message, onLongPress: () -> Unit = {}) {
    val align = if (msg.mine) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (msg.mine)
        RoundedCornerShape(19.dp, 19.dp, 6.dp, 19.dp)
    else
        RoundedCornerShape(19.dp, 19.dp, 19.dp, 6.dp)
    Box(Modifier.fillMaxWidth(), contentAlignment = align) {
        Row(verticalAlignment = Alignment.Bottom) {
            if (!msg.mine && msg.senderAvatar != null) {
                Avatar(msg.senderAvatar, 26)
                Spacer(Modifier.width(6.dp))
            }
            Column(horizontalAlignment = if (msg.mine) Alignment.End else Alignment.Start) {
                Column(
                    Modifier
                        .fillMaxWidth(0.78f)
                        .then(
                            if (msg.mine) Modifier
                                .clip(shape)
                                .background(AuroraGradient)
                            else Modifier
                                .clip(shape)
                                .background(Color.White)
                                .border(1.dp, Color(0xFFEFEBF8), shape)
                        )
                        .combinedClickable(onClick = {}, onLongClick = onLongPress)
                        .padding(if (msg.image != null) 5.dp else 11.dp)
                ) {
                    if (!msg.mine && msg.sender != null) {
                        Text(msg.sender, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Orchid)
                        Spacer(Modifier.height(2.dp))
                    }
                    msg.quoted?.let { q ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (msg.mine) Color.White.copy(alpha = 0.22f) else Cloud)
                                .padding(horizontal = 9.dp, vertical = 6.dp)
                        ) {
                            Box(
                                Modifier
                                    .width(2.5.dp)
                                    .height(26.dp)
                                    .background(if (msg.mine) Color.White else Orchid)
                            )
                            Spacer(Modifier.width(7.dp))
                            Text(
                                q, fontSize = 10.sp,
                                color = if (msg.mine) Color.White.copy(alpha = 0.9f) else Txt2,
                                maxLines = 2, overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                    }
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
                    if (msg.voice) {
                        VoiceBubbleContent(msg)
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
                msg.reaction?.let { r ->
                    Text(
                        r, fontSize = 12.sp,
                        modifier = Modifier
                            .offset(y = (-7).dp)
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .border(1.dp, Mist, RoundedCornerShape(50))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VoiceBubbleContent(msg: Message) {
    var playing by remember { mutableStateOf(false) }
    val heights = listOf(8, 14, 20, 11, 17, 9, 15, 19, 7, 13, 18, 10, 16, 8, 12, 17, 9, 14)
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(
            Modifier
                .size(32.dp)
                .clip(CircleShape)
                .then(if (msg.mine) Modifier.background(Color.White) else Modifier.background(AuroraGradient))
                .clickable { playing = !playing },
            contentAlignment = Alignment.Center
        ) {
            if (playing) {
                Text("❚❚", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = if (msg.mine) Orchid else Color.White)
            } else {
                Icon(
                    Icons.Filled.PlayArrow, null,
                    tint = if (msg.mine) Orchid else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.5.dp)) {
            heights.forEachIndexed { i, h ->
                val played = playing && i < 7
                Box(
                    Modifier
                        .width(3.dp)
                        .height(h.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            when {
                                msg.mine -> if (played) Color.White else Color.White.copy(alpha = 0.55f)
                                played -> Orchid
                                else -> Mist
                            }
                        )
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(
            msg.voiceLen, fontSize = 10.sp, fontWeight = FontWeight.Bold,
            color = if (msg.mine) Color.White.copy(alpha = 0.9f) else Txt3
        )
    }
}

/* ---------- Incoming call ---------- */

@Composable
fun IncomingCallScreen(onAccept: () -> Unit, onDecline: () -> Unit) {
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
            Text(
                "\uD83D\uDD12 E2E encrypted · Amora Video",
                color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.14f))
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            )
            Spacer(Modifier.height(40.dp))
            Box(contentAlignment = Alignment.Center) {
                val t = rememberInfiniteTransition(label = "ring")
                val a by t.animateFloat(
                    0.1f, 0.4f,
                    infiniteRepeatable(tween(900), RepeatMode.Reverse),
                    label = "ringAlpha"
                )
                Box(
                    Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White.copy(alpha = a), CircleShape)
                )
                Box(
                    Modifier
                        .size(158.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White.copy(alpha = a * 1.6f), CircleShape)
                )
                Avatar("https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400", 122, ringed = true)
            }
            Spacer(Modifier.height(28.dp))
            Text("Amara \uD83D\uDC98", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(7.dp))
            Text("Incoming video call…", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "\uD83D\uDCAC Message", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 13.dp, vertical = 8.dp)
                )
                Text(
                    "⏰ Remind me", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(horizontal = 13.dp, vertical = 8.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(62.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF43F5E))
                            .clickable(onClick = onDecline),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Call, null, tint = Color.White, modifier = Modifier.size(26.dp).rotate(135f))
                    }
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
                        Icon(Icons.Filled.Call, null, tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Accept", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
