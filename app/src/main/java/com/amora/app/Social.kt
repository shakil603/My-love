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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/* ---------- Liked you (dating) ---------- */

@Composable
fun LikedYouScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("Liked you", onBack, sub = "27 people liked your profile")
        Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            for (row in Mock.users.chunked(2)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { u ->
                        Box(
                            Modifier.weight(1f).height(210.dp).clip(RoundedCornerShape(20.dp))
                                .clickable { go("user/${u.id}") }
                        ) {
                            AsyncImage(
                                model = u.avatar, contentDescription = u.name, contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                Modifier.fillMaxSize().background(
                                    Brush.verticalGradient(0.5f to Color.Transparent, 1f to Color(0xCC0A0714))
                                )
                            )
                            if (u.match > 0) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(10.dp).clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.25f))
                                        .padding(horizontal = 9.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(9.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("${u.match}%", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                            Column(Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                                Text(u.name.substringBefore(" "), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                Text(u.distance, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    repeat(2 - row.size) { Spacer(Modifier.weight(1f)) }
                }
                Spacer(Modifier.height(10.dp))
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

/* ---------- People nearby ---------- */

@Composable
fun NearbyScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("People nearby", onBack, sub = "Sharing approximate location only")
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 6.dp)) {
            items(Mock.users.size) { i ->
                val u = Mock.users[i]
                Row(
                    Modifier.fillMaxWidth()
                        .clickable { go("user/${u.id}") }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Avatar(u.avatar, 52, ringed = u.match > 0)
                        if (u.online) {
                            Box(
                                Modifier.align(Alignment.BottomEnd).size(14.dp).clip(CircleShape).background(Color.White),
                                contentAlignment = Alignment.Center
                            ) { Box(Modifier.size(10.dp).clip(CircleShape).background(Mint)) }
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(u.name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Ink)
                            if (u.verified) {
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.Filled.Verified, null, tint = Sky, modifier = Modifier.size(13.dp))
                            }
                        }
                        Text(u.job, fontSize = 11.sp, color = Txt2, fontWeight = FontWeight.Medium, maxLines = 1)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFF6E7FF))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(Icons.Filled.NearMe, null, tint = Orchid, modifier = Modifier.size(10.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(u.distance.substringBefore(" away"), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Orchid)
                    }
                }
            }
        }
    }
}

/* ---------- Group info ---------- */

@Composable
fun GroupInfoScreen(chatId: String, onBack: () -> Unit, go: (String) -> Unit = {}) {
    val chat = Mock.chats.firstOrNull { it.id == chatId } ?: Mock.chats[1]
    val members = Mock.users.take(5)
    LazyColumn(Modifier.fillMaxSize().background(Color(0xFFFBFAFE)), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            ScreenHeader("Group info", onBack)
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Avatar(chat.avatar, 96, ringed = true)
                Spacer(Modifier.height(12.dp))
                Text(chat.name, fontSize = 21.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Text("${members.size + 1} members · End-to-end encrypted", fontSize = 11.sp, color = Txt2, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GroupAction(Icons.Filled.PersonAdd, "Add") {}
                    GroupAction(Icons.Filled.Search, "Search") { go("search") }
                    GroupAction(Icons.Filled.NotificationsOff, "Mute") {}
                    GroupAction(Icons.Filled.Event, "Events") { go("events") }
                }
                Spacer(Modifier.height(18.dp))
            }
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("Shared media", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Mock.user("1").photos.take(4).forEach { url ->
                        AsyncImage(
                            model = url, contentDescription = null, contentScale = ContentScale.Crop,
                            modifier = Modifier.weight(1f).height(80.dp).clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                FileChip("Q3-Forecast.xlsx", "3 sheets · 2.1 MB · live preview")
                Spacer(Modifier.height(6.dp))
                FileChip("Launch-Playbook-2026.pdf", "40 pages · 8.2 MB")
                Spacer(Modifier.height(18.dp))
                Text("Members", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                Spacer(Modifier.height(4.dp))
            }
        }
        items(members.size) { i ->
            val u = members[i]
            Row(
                Modifier.fillMaxWidth()
                    .clickable { go("user/${u.id}") }
                    .padding(horizontal = 18.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(u.avatar, 42)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(u.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
                    Text(if (i == 0) "Admin" else "Member", fontSize = 10.sp, color = if (i == 0) Orchid else Txt3, fontWeight = FontWeight.Bold)
                }
                if (u.online) Box(Modifier.size(9.dp).clip(CircleShape).background(Mint))
            }
        }
        item {
            Row(
                Modifier.padding(18.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF1F2)).clickable(onClick = onBack).padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Filled.Logout, null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Leave group", color = Color(0xFFE11D48), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
private fun GroupAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(50.dp).clip(RoundedCornerShape(16.dp)).background(Color.White)
                .border(1.dp, Mist, RoundedCornerShape(16.dp)).clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) { Icon(icon, label, tint = Orchid, modifier = Modifier.size(20.dp)) }
        Spacer(Modifier.height(5.dp))
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Txt2)
    }
}

/* ---------- Saved posts ---------- */

@Composable
fun SavedPostsScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("Saved", onBack, sub = "Only you can see what you save")
        Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            for (row in (Mock.user("1").photos + Mock.user("5").photos).take(9).chunked(3)) {
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
            Spacer(Modifier.height(10.dp))
            FileChip("Launch-Playbook-2026.pdf", "Saved from Daniel · 40 pages")
            Spacer(Modifier.height(6.dp))
            FileChip("Trip-Itinerary-Bali.pdf", "Saved from chat · 12 pages")
            Spacer(Modifier.height(20.dp))
        }
    }
}

/* ---------- Events ---------- */

private data class Evt(val day: String, val month: String, val title: String, val place: String, val going: String)

@Composable
fun EventsScreen(onBack: () -> Unit) {
    val events = listOf(
        Evt("14", "NOV", "Design & Chill — Dhaka meetup", "Gulshan Social House · 6:30 PM", "48 going"),
        Evt("21", "NOV", "Amora Creators Live Q&A", "Online · Amora Live", "1.2k going"),
        Evt("28", "NOV", "Weekend hiking — Sajek trail", "Meet at Khagrachari point", "23 going"),
        Evt("05", "DEC", "Open-source sprint · Phoenix", "Hub71 cowork · all day", "16 going")
    )
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("Events", onBack, sub = "From your groups and interests")
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)) {
            items(events.size) { i ->
                val e = events[i]
                Row(
                    Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(18.dp))
                        .background(Color.White).border(1.dp, Color(0xFFEFEBF8), RoundedCornerShape(18.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(AuroraGradient)
                            .padding(top = 7.dp)
                    ) {
                        Text(e.day, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 18.sp)
                        Text(e.month, color = Color.White.copy(alpha = 0.85f), fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(e.title, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Ink, maxLines = 1)
                        Text(e.place, fontSize = 11.sp, color = Txt2, fontWeight = FontWeight.Medium, maxLines = 1)
                        Text(e.going, fontSize = 10.sp, color = Orchid, fontWeight = FontWeight.ExtraBold)
                    }
                    Text(
                        "Join", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.clip(RoundedCornerShape(50)).background(Ink)
                            .clickable {}.padding(horizontal = 15.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
