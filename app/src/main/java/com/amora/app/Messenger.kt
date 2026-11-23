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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
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
fun ChatsScreen(onOpenChat: (String) -> Unit, go: (String) -> Unit = {}) {
    val mctx = androidx.compose.ui.platform.LocalContext.current
    var msgPriv by remember { mutableStateOf(Store.get(mctx, "priv_msg", "Public")) }
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
                Text(androidx.compose.ui.res.stringResource(R.string.tab_chats), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { go("incoming/1") }) { Icon(Icons.Filled.Videocam, "New call", tint = Ink) }
                IconButton(onClick = { go("creategroup") }) { Icon(Icons.Filled.Add, "New group", tint = Ink) }
            }
        }
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (msgPriv) {
                            "Private" -> Color(0xFFFFF1F2)
                            "Friends" -> Color(0xFFEEF2FF)
                            else -> Color(0xFFECFDF5)
                        }
                    )
                    .clickable {
                        msgPriv = when (msgPriv) { "Public" -> "Friends"; "Friends" -> "Private"; else -> "Public" }
                        Store.set(mctx, "priv_msg", msgPriv)
                    }
                    .padding(horizontal = 12.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Lock, null,
                    tint = when (msgPriv) { "Private" -> Rose; "Friends" -> Indigo; else -> Mint },
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    when (msgPriv) {
                        "Private" -> "Messages: Private — nobody new can message you"
                        "Friends" -> "Messages: Friends — only friends can message you"
                        else -> "Messages: Public — anyone can message you"
                    },
                    fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                    color = when (msgPriv) { "Private" -> Color(0xFFBE123C); "Friends" -> Indigo; else -> Color(0xFF047857) },
                    modifier = Modifier.weight(1f)
                )
                Text("tap to change", fontSize = 9.sp, color = Txt3, fontWeight = FontWeight.Bold)
            }
        }
        item {
            TextField(
                value = query, onValueChange = { query = it },
                placeholder = { Text(androidx.compose.ui.res.stringResource(R.string.search_hint) + "…", fontSize = 12.sp, color = Txt3) },
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
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(AuroraGradient)
                    .clickable { go("livehub") }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Bolt, null, tint = Color.White, modifier = Modifier.size(20.dp)) }
                Spacer(Modifier.width(11.dp))
                Column(Modifier.weight(1f)) {
                    Text("Live messaging — real people", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Global & private rooms over the open network", color = Color.White.copy(alpha = 0.85f), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                }
                Box(Modifier.size(8.dp).clip(CircleShape).background(Mint))
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
                                .background(Paper),
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
                            Icon(Icons.Filled.PushPin, "Pinned", tint = Txt3, modifier = Modifier.size(11.dp))
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
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val msgs = remember {
        ((if (isGroup) Mock.groupMessages else Mock.messages) + Store.loadChat(ctx, chatId)).toMutableStateList()
    }
    var input by remember { mutableStateOf("") }
    var typing by remember { mutableStateOf(false) }
    var replyTo by remember { mutableStateOf<Message?>(null) }
    var pickerFor by remember { mutableIntStateOf(-1) }
    var editing by remember { mutableIntStateOf(-1) }
    var showAttach by remember { mutableStateOf(false) }
    var vanish by remember { mutableStateOf(false) }
    var sendCount by remember { mutableIntStateOf(0) }
    var isBlocked by remember { mutableStateOf(chatId in Store.blocked(ctx)) }
    var secureHere by remember { mutableStateOf(Store.get(ctx, "sec_chat_$chatId", "false") == "true") }
    var chatMenu by remember { mutableStateOf(false) }
    /* the other side's own capture rule — synced from their account; Yuki demos "Private" */
    val ownerRec = Store.get(ctx, "rec_allow_$chatId", if (chatId == "5") "Private" else "Public")
    val ownerBlocks = ownerRec == "Private" || (ownerRec == "Friends" && chatId !in Store.following(ctx))
    androidx.compose.runtime.DisposableEffect(chatId, secureHere, ownerBlocks) {
        SecureX.local.value = secureHere || ownerBlocks
        onDispose { SecureX.local.value = false }
    }
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
        if (msg.mine && msg.text != null) Store.appendChat(ctx, chatId, msg.text)
        replyTo = null
        showAttach = false
        sendCount++
    }

    Box(Modifier.fillMaxSize().background(Paper)) {
        if (ThemeX.wallpaper.value.isNotEmpty()) {
            AsyncImage(
                model = ThemeX.wallpaper.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = if (ThemeX.dark) 0.45f else 0.8f,
                modifier = Modifier.fillMaxSize()
            )
        }
    Column(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        /* header */
        Row(
            Modifier
                .fillMaxWidth()
                .background(Paper)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { go(if (isGroup) "groupinfo/$chatId" else "user/$chatId") }
            ) {
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
            }
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(if (vanish) Color(0xFFF6E7FF) else Color.Transparent)
                    .clickable { vanish = !vanish }
                    .padding(8.dp)
            ) {
                Icon(Icons.Filled.Timer, "Disappearing messages", tint = if (vanish) Orchid else Ink, modifier = Modifier.size(20.dp))
            }
            IconButton(onClick = { go("audiocall/$chatId") }) { Icon(Icons.Filled.Call, "Audio call", tint = Ink) }
            IconButton(onClick = { go("video/$chatId") }) { Icon(Icons.Filled.Videocam, "Video call", tint = Ink) }
            Box {
                IconButton(onClick = { chatMenu = true }) { Icon(Icons.Filled.MoreVert, "More", tint = Ink) }
                androidx.compose.material3.DropdownMenu(expanded = chatMenu, onDismissRequest = { chatMenu = false }) {
                    androidx.compose.material3.DropdownMenuItem(
                        text = {
                            Text(
                                if (secureHere) "Screen protection: ON ✓" else "Screen protection: OFF",
                                fontWeight = FontWeight.Bold, fontSize = 13.sp,
                                color = if (secureHere) Color(0xFF047857) else Ink
                            )
                        },
                        onClick = {
                            secureHere = !secureHere
                            Store.set(ctx, "sec_chat_$chatId", secureHere.toString())
                            android.widget.Toast.makeText(
                                ctx,
                                if (secureHere) "Screenshots & screen recording blocked in this ${if (isGroup) "group" else "chat"}"
                                else "Screen protection off for this ${if (isGroup) "group" else "chat"}",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    androidx.compose.material3.DropdownMenuItem(
                        text = {
                            Text(
                                if (isBlocked) "Unblock ${if (isGroup) "group" else chat.name}"
                                else "Block ${if (isGroup) "group" else chat.name}",
                                fontWeight = FontWeight.Bold, fontSize = 13.sp,
                                color = if (isBlocked) Ink else Rose
                            )
                        },
                        onClick = {
                            chatMenu = false
                            isBlocked = !isBlocked
                            val cur = Store.blocked(ctx)
                            Store.setBlocked(ctx, if (isBlocked) cur + chatId else cur - chatId)
                            android.widget.Toast.makeText(
                                ctx,
                                if (isBlocked) "Blocked — they can't message or call you" else "Unblocked",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text("Blocked list", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                        onClick = { chatMenu = false; go("blocked") }
                    )
                }
            }
        }
        if (ownerBlocks) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFF6FF))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Lock, null, tint = Color(0xFF1D4ED8), modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(7.dp))
                Text(
                    "${chat.name} blocked screen capture — screenshots & recording are disabled here",
                    fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D4ED8)
                )
            }
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
                    "Disappearing messages ON · new messages vanish after 24 h",
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
        /* reaction + actions picker */
        if (pickerFor >= 0) {
            val pm = msgs[pickerFor]
            Column(Modifier.fillMaxWidth().background(Paper)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
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
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    @Composable
                    fun ActionChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(AuroraSoft)
                                .clickable(onClick = onClick)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(icon, null, tint = Orchid, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Orchid)
                        }
                    }
                    ActionChip(Icons.Filled.Reply, "Reply") {
                        replyTo = pm
                        pickerFor = -1
                    }
                    if (pm.text != null) {
                        ActionChip(Icons.Filled.ContentCopy, "Copy") {
                            try {
                                val cb = ctx.getSystemService(android.content.ClipboardManager::class.java)
                                cb.setPrimaryClip(android.content.ClipData.newPlainText("Amora message", pm.text))
                                android.widget.Toast.makeText(ctx, "Message copied", android.widget.Toast.LENGTH_SHORT).show()
                            } catch (_: Exception) {}
                            pickerFor = -1
                        }
                    }
                    if (pm.mine && pm.text != null) {
                        ActionChip(Icons.Filled.Edit, "Edit") {
                            editing = pickerFor
                            input = pm.text
                            pickerFor = -1
                        }
                    }
                    if (pm.image != null || pm.fileName != null) {
                        ActionChip(Icons.Filled.Download, "Download") {
                            val allowed = pm.mine || Store.get(ctx, "dl_allow_${chat.id}", "Public") != "Private"
                            if (allowed) {
                                try {
                                    if (pm.image != null) {
                                        val dm = ctx.getSystemService(android.app.DownloadManager::class.java)
                                        val req = android.app.DownloadManager.Request(android.net.Uri.parse(pm.image))
                                            .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                            .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, "amora-photo.jpg")
                                        dm.enqueue(req)
                                        android.widget.Toast.makeText(ctx, "Downloading to Downloads folder…", android.widget.Toast.LENGTH_SHORT).show()
                                    } else {
                                        android.widget.Toast.makeText(ctx, "${pm.fileName} saved to Downloads ✓", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                } catch (_: Exception) {}
                            } else {
                                android.widget.Toast.makeText(ctx, "Owner turned off downloads for this content", android.widget.Toast.LENGTH_LONG).show()
                            }
                            pickerFor = -1
                        }
                    }
                }
            }
        }
        /* reply bar */
        replyTo?.let { r ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Paper)
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
                    .background(Paper)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AttachOption(Icons.Filled.PhotoCamera, "Photo") {
                    send(Message(true, image = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=500", time = "now", quoted = replyTo?.text))
                }
                AttachOption(Icons.Filled.PictureAsPdf, "PDF") {
                    send(Message(true, fileName = "shared-notes.pdf", fileMeta = "6 pages · 1.1 MB · preview ✓", time = "now"))
                }
                AttachOption(Icons.Filled.Mic, "Voice") {
                    send(Message(true, voice = true, voiceLen = "0:07", time = "now"))
                }
                AttachOption(Icons.Filled.LocationOn, "Location") {
                    send(Message(true, text = "\uD83D\uDCCD Live location shared · 42 min", time = "now"))
                }
                AttachOption(Icons.Filled.Folder, "Any file") { go("upload") }
            }
        }
        if (isBlocked) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF1F2))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Block, null, tint = Rose, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "You blocked ${chat.name} — no messages or calls",
                    fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFBE123C),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Unblock", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Ink)
                        .clickable {
                            isBlocked = false
                            Store.setBlocked(ctx, Store.blocked(ctx) - chatId)
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }
        }
        if (!isBlocked) {
        /* emoji quick bar */
        Row(
            Modifier
                .fillMaxWidth()
                .background(Paper)
                .padding(horizontal = 18.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("❤️", "\uD83D\uDE02", "\uD83D\uDE0D", "\uD83D\uDC4D", "\uD83C\uDF89", "\uD83D\uDE2E", "\uD83D\uDE4F", "\uD83D\uDD25").forEach { e ->
                Text(e, fontSize = 18.sp, modifier = Modifier.clickable { input += e })
            }
        }
        if (editing >= 0) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(AuroraSoft)
                    .padding(horizontal = 16.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Edit, null, tint = Orchid, modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(7.dp))
                Text("Editing message", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Orchid, modifier = Modifier.weight(1f))
                Icon(
                    Icons.Filled.Close, "Cancel edit", tint = Txt3,
                    modifier = Modifier.size(15.dp).clickable { editing = -1; input = "" }
                )
            }
        }
        /* input row */
        Row(
            Modifier
                .fillMaxWidth()
                .background(Paper)
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
                placeholder = { Text(androidx.compose.ui.res.stringResource(R.string.message_hint), fontSize = 13.sp, color = Txt3) },
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
                        if (editing >= 0 && input.isNotBlank()) {
                            msgs[editing] = msgs[editing].copy(text = input.trim(), time = "edited")
                            editing = -1
                            input = ""
                        } else if (input.isBlank()) {
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
                if (input.isBlank()) Icon(Icons.Filled.Mic, "Record voice", tint = Color.White, modifier = Modifier.size(19.dp))
                else Icon(Icons.Filled.Send, "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
        }
    }
    }
}

@Composable
private fun AttachOption(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
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
        ) { Icon(icon, label, tint = Orchid, modifier = Modifier.size(20.dp)) }
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
            .background(Paper)
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
                                .background(Paper)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            msg.time, fontSize = 9.sp,
                            color = if (msg.mine) Color.White.copy(alpha = 0.85f) else Txt3
                        )
                        if (msg.mine) {
                            Spacer(Modifier.width(3.dp))
                            Icon(Icons.Filled.DoneAll, "Seen", tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(12.dp))
                        }
                    }
                }
                msg.reaction?.let { r ->
                    Text(
                        r, fontSize = 12.sp,
                        modifier = Modifier
                            .offset(y = (-7).dp)
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Paper)
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
                .then(if (msg.mine) Modifier.background(Paper) else Modifier.background(AuroraGradient))
                .clickable { playing = !playing },
            contentAlignment = Alignment.Center
        ) {
            if (playing) {
                Icon(Icons.Filled.Pause, "Pause", tint = if (msg.mine) Orchid else Color.White, modifier = Modifier.size(18.dp))
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
