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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
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
fun OnboardingScreen(onCreate: () -> Unit, onLogin: () -> Unit, onLanguage: () -> Unit = {}) {
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
                    .clickable(onClick = onCreate)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(androidx.compose.ui.res.stringResource(R.string.onboard_create), color = Ink, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(19.dp))
                    .border(1.5.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(19.dp))
                    .clickable(onClick = onLogin)
                    .padding(vertical = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("I already have an account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.height(18.dp))
            Text(
                "English · বাংলা · Español · العربية · 中文 · हिन्दी · +8",
                color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.12f))
                    .clickable(onClick = onLanguage)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ---------- 02 · Feed ---------- */

@Composable
fun FeedScreen(go: (String) -> Unit = {}) {
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
                Icon(Icons.Filled.PlayArrow, null, tint = Ink, modifier = Modifier.clickable { go("reels") })
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Filled.Search, null, tint = Ink, modifier = Modifier.clickable { go("search") })
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Filled.Notifications, null, tint = Ink, modifier = Modifier.clickable { go("notifications") })
            }
        }
        item {
            LazyRow(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                items(Mock.stories) { story ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { if (!story.isAdd) go("story") else go("upload") }
                    ) {
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
            PostCard(post, go)
            Spacer(Modifier.height(14.dp))
        }
    }
}

@Composable
fun PostCard(post: Post, go: (String) -> Unit = {}) {
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
                Modifier
                    .clickable { go("user/${post.authorId}") }
                    .padding(14.dp),
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
                Text("\uD83D\uDCAC ${post.comments}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Txt2, modifier = Modifier.clickable { go("comments") })
                Spacer(Modifier.weight(1f))
                Icon(Icons.Filled.Send, null, tint = Txt2, modifier = Modifier.size(18.dp))
            }
        }
    }
}

/* ---------- 03 · Discover ---------- */

@Composable
fun DiscoverScreen(go: (String) -> Unit = {}) {
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Mist, CircleShape)
                        .clickable { go("nearby") },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.NearMe, "Nearby", tint = Ink, modifier = Modifier.size(18.dp)) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(AuroraGradient)
                        .clickable { go("likedyou") }
                        .padding(horizontal = 13.dp, vertical = 9.dp)
                ) {
                    Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(5.dp))
                    Text("Liked you · 27", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
        }
        Box(
            Modifier
                .padding(horizontal = 18.dp)
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(28.dp))
                .clickable { go("user/1") }
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
                    .clickable { go("match") },
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

/* ---------- 06 · Spaces ---------- */

@Composable
fun SpacesScreen(go: (String) -> Unit = {}) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 20.dp)) {
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(androidx.compose.ui.res.stringResource(R.string.spaces_title), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { go("creategroup") }) { Icon(Icons.Filled.Add, null, tint = Ink) }
            }
        }
        item {
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(AuroraGradient)
                    .clickable { go("creategroup") }
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(46.dp).clip(RoundedCornerShape(16.dp)).background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.Group, null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(androidx.compose.ui.res.stringResource(R.string.create_group), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Friends, family, work, business — unlimited, no restrictions", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                    Icon(Icons.Filled.Add, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.height(18.dp))
        }
        item {
            Text(
                "YOUR GROUPS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
            Spacer(Modifier.height(6.dp))
            Column(Modifier.padding(horizontal = 16.dp)) {
                Mock.chats.filter { it.kind == "group" || it.kind == "work" }.forEach { c ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFEFEBF8), RoundedCornerShape(18.dp))
                            .clickable { go("chat/${c.id}") }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Avatar(c.avatar, 46, ringed = c.unread > 0)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(c.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Ink, maxLines = 1)
                            Text(c.last, fontSize = 11.sp, color = Txt2, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        if (c.unread > 0) {
                            Box(
                                Modifier.clip(CircleShape).background(AuroraGradient).padding(horizontal = 7.dp, vertical = 2.dp)
                            ) { Text("${c.unread}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold) }
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
        item {
            Text(
                "TOOLS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3,
                modifier = Modifier.padding(horizontal = 22.dp)
            )
            Spacer(Modifier.height(6.dp))
            Column(Modifier.padding(horizontal = 16.dp)) {
                listOf(
                    listOf(
                        Triple(Icons.Filled.Description, "Notes & tasks", "board"),
                        Triple(Icons.Filled.DateRange, "Calendar", "calendar")
                    ),
                    listOf(
                        Triple(Icons.Filled.PhotoLibrary, "Media", "saved"),
                        Triple(Icons.Filled.CloudUpload, "Share any file", "upload")
                    )
                ).forEach { rowItems ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowItems.forEach { (icon, label, route) ->
                            Column(
                                Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFEFEBF8), RoundedCornerShape(20.dp))
                                    .clickable { go(route) }
                                    .padding(16.dp)
                            ) {
                                Box(
                                    Modifier.size(40.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFF6E7FF)),
                                    contentAlignment = Alignment.Center
                                ) { Icon(icon, null, tint = Orchid, modifier = Modifier.size(19.dp)) }
                                Spacer(Modifier.height(10.dp))
                                Text(label, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Ink)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("RECENT FILES", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
                Spacer(Modifier.weight(1f))
                Text("See all", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Orchid, modifier = Modifier.clickable { go("upload") })
            }
            Column(Modifier.padding(horizontal = 16.dp)) {
                FileChip("Q3-Report-final.pdf", "Project Phoenix · 2 h ago · shared with 8")
                Spacer(Modifier.height(6.dp))
                FileChip("brand-refresh.fig", "Personal · yesterday · preview ready")
                Spacer(Modifier.height(6.dp))
                FileChip("trip-photos.zip", "Family · Mon · 48 files")
            }
        }
    }
}

/* ---------- 07 · Profile ---------- */

@Composable
fun ProfileScreen(go: (String) -> Unit = {}) {
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { go("followers") }
                        ) {
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
                    GradientButton("Edit profile", Modifier.weight(1f)) { go("editprofile") }
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(17.dp))
                            .border(1.5.dp, Mist, RoundedCornerShape(17.dp))
                            .background(Color.White)
                            .clickable { go("settings") }
                            .padding(15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Settings, null, tint = Ink, modifier = Modifier.size(20.dp))
                    }
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
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier.padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Triple(Icons.Filled.BookmarkBorder, "Saved", "saved"),
                        Triple(Icons.Filled.Event, "Events", "events"),
                        Triple(Icons.Filled.Favorite, "Liked you", "likedyou")
                    ).forEach { (icon, label, route) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .border(1.dp, Mist, RoundedCornerShape(14.dp))
                                .clickable { go(route) }
                                .padding(vertical = 11.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(icon, null, tint = Orchid, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                        }
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
