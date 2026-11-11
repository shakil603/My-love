package com.amora.app

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/* ---------- User profile (view another user) ---------- */

@Composable
fun UserProfileScreen(userId: String, onBack: () -> Unit, go: (String) -> Unit = {}) {
    val u = Mock.user(userId)
    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFFBFAFE)), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Box(Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = u.cover, contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(190.dp)
                )
                Box(
                    Modifier.fillMaxWidth().height(190.dp)
                        .background(Brush.verticalGradient(0f to Color(0x66000000), 0.5f to Color.Transparent, 1f to Color(0x33000000)))
                )
                Row(
                    Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back", tint = Color.White) }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {}) { Icon(Icons.Filled.MoreVert, "More", tint = Color.White) }
                }
            }
            Column(
                Modifier.fillMaxWidth().offset(y = (-48).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Box(
                        Modifier.size(104.dp).clip(CircleShape).background(Color.White),
                        contentAlignment = Alignment.Center
                    ) { Avatar(u.avatar, 96, ringed = true) }
                    if (u.online) {
                        Box(
                            Modifier.align(Alignment.BottomEnd).padding(6.dp).size(16.dp)
                                .clip(CircleShape).background(Color.White),
                            contentAlignment = Alignment.Center
                        ) { Box(Modifier.size(11.dp).clip(CircleShape).background(Mint)) }
                    }
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(u.name, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                    if (u.verified) {
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Filled.Verified, "Verified", tint = Sky, modifier = Modifier.size(18.dp))
                    }
                }
                Text("${u.handle} · ${u.location}", fontSize = 12.sp, color = Txt2, fontWeight = FontWeight.Medium)
                if (u.match > 0) {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(AuroraGradient)
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(11.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("${u.match}% match with you", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    u.bio, fontSize = 13.sp, color = Txt2, lineHeight = 19.sp,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(26.dp)) {
                    listOf(u.postCount to "POSTS", u.followers to "FOLLOWERS", u.following to "FOLLOWING").forEach { (n, l) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(n, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                            Text(l, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Txt3)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GradientButton("Message", Modifier.weight(1f)) { go("chat/${u.id}") }
                    ProfileIconAction(Icons.Filled.Call, "Audio call") { go("audiocall/${u.id}") }
                    ProfileIconAction(Icons.Filled.Videocam, "Video call") { go("video/${u.id}") }
                    ProfileIconAction(Icons.Filled.Favorite, "Like", tint = Rose) { go("match") }
                }
            }
        }
        item {
            Column(Modifier.offset(y = (-28).dp).padding(horizontal = 16.dp)) {
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color.White)
                        .border(1.dp, Color(0xFFEFEBF8), RoundedCornerShape(18.dp)).padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoCell(Icons.Filled.Work, u.job.substringBefore(" ·"))
                    InfoCell(Icons.Filled.LocationOn, u.location.substringBefore(","))
                    InfoCell(Icons.Filled.NearMe, u.distance)
                }
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    u.interests.take(5).forEach {
                        Text(
                            it, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Orchid,
                            modifier = Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFF6E7FF))
                                .padding(horizontal = 11.dp, vertical = 6.dp)
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                Text("Photos", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.height(8.dp))
                for (row in u.photos.chunked(3)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        row.forEach { url ->
                            AsyncImage(
                                model = url, contentDescription = null, contentScale = ContentScale.Crop,
                                modifier = Modifier.weight(1f).height(110.dp).clip(RoundedCornerShape(12.dp))
                            )
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                    Spacer(Modifier.height(5.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileIconAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, tint: Color = Ink, onClick: () -> Unit) {
    Box(
        Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).border(1.5.dp, Mist, RoundedCornerShape(16.dp))
            .background(Color.White).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Icon(icon, label, tint = tint, modifier = Modifier.size(20.dp)) }
}

@Composable
private fun InfoCell(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = Orchid, modifier = Modifier.size(17.dp))
        Spacer(Modifier.height(4.dp))
        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Txt2)
    }
}

/* ---------- Edit profile ---------- */

@Composable
fun EditProfileScreen(onBack: () -> Unit) {
    var name by remember { mutableStateOf("Shakil Rahman") }
    var handle by remember { mutableStateOf("@shakil") }
    var bio by remember { mutableStateOf("Builder of Amora · dreaming big from Mymensingh \uD83C\uDF0C") }
    var work by remember { mutableStateOf("Founder · Amora") }
    var location by remember { mutableStateOf("Mymensingh, Bangladesh") }
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("Edit profile", onBack)
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            Box {
                Avatar("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300", 96, ringed = true)
                Box(
                    Modifier.align(Alignment.BottomEnd).size(32.dp).clip(CircleShape)
                        .background(AuroraGradient).border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.PhotoCamera, "Change photo", tint = Color.White, modifier = Modifier.size(15.dp)) }
            }
            Spacer(Modifier.height(18.dp))
            EditField("Name", name) { name = it }
            EditField("Username", handle) { handle = it }
            EditField("Bio", bio, minLines = 3) { bio = it }
            EditField("Work", work) { work = it }
            EditField("Location", location) { location = it }
            Spacer(Modifier.height(8.dp))
        }
        Box(Modifier.padding(18.dp)) { GradientButton("Save changes", Modifier.fillMaxWidth(), onClick = onBack) }
    }
}

@Composable
private fun EditField(label: String, value: String, minLines: Int = 1, onChange: (String) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(label.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
        Spacer(Modifier.height(5.dp))
        TextField(
            value = value, onValueChange = onChange, minLines = minLines,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

/* ---------- Followers / Following ---------- */

@Composable
fun FollowersScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    var tab by remember { mutableStateOf(0) }
    val following = remember { mutableStateOf(setOf("3", "5")) }
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("Connections", onBack)
        Row(Modifier.padding(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Followers · 48.2k", "Following · 1,284").forEachIndexed { i, t ->
                val on = tab == i
                Text(
                    t, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                    color = if (on) Color.White else Txt2,
                    modifier = Modifier.clip(RoundedCornerShape(50))
                        .background(if (on) Ink else Color.White)
                        .border(1.dp, if (on) Ink else Mist, RoundedCornerShape(50))
                        .clickable { tab = i }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
            items(Mock.users.size) { i ->
                val u = Mock.users[i]
                val isF = u.id in following.value
                Row(
                    Modifier.fillMaxWidth()
                        .clickable { go("user/${u.id}") }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(u.avatar, 48)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(u.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Ink)
                            if (u.verified) {
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.Filled.Verified, null, tint = Sky, modifier = Modifier.size(13.dp))
                            }
                        }
                        Text(u.handle, fontSize = 11.sp, color = Txt3, fontWeight = FontWeight.Medium)
                    }
                    Text(
                        if (isF) "Following" else "Follow",
                        fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (isF) Txt2 else Color.White,
                        modifier = Modifier.clip(RoundedCornerShape(50))
                            .background(if (isF) Cloud else Ink)
                            .clickable {
                                following.value = if (isF) following.value - u.id else following.value + u.id
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
