package com.amora.app

import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
    val uctx = androidx.compose.ui.platform.LocalContext.current
    var ublocked by remember { mutableStateOf(u.id in Store.blocked(uctx)) }
    var usec by remember { mutableStateOf(Store.get(uctx, "sec_user_${u.id}", "false") == "true") }
    /* this user's own capture rule — synced from their account; Yuki demos "Private" */
    val uOwnerRec = Store.get(uctx, "rec_allow_${u.id}", if (u.id == "5") "Private" else "Public")
    val uOwnerBlocks = uOwnerRec == "Private" || (uOwnerRec == "Friends" && u.id !in Store.following(uctx))
    androidx.compose.runtime.DisposableEffect(usec, uOwnerBlocks) {
        SecureX.local.value = usec || uOwnerBlocks
        onDispose { SecureX.local.value = false }
    }
    LazyColumn(Modifier.fillMaxSize().background(Paper), contentPadding = PaddingValues(bottom = 24.dp)) {
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
                    if (uOwnerBlocks) {
                        Row(
                            Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Color.Black.copy(alpha = 0.45f))
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("\uD83D\uDD12 No screen capture", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                        }
                        Spacer(Modifier.width(6.dp))
                    }
                    Box {
                        var menuOpen by remember { mutableStateOf(false) }
                        val mctx = androidx.compose.ui.platform.LocalContext.current
                        IconButton(onClick = { menuOpen = true }) { Icon(Icons.Filled.MoreVert, "More", tint = Color.White) }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            DropdownMenuItem(
                                text = { Text("Share profile", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Filled.Share, null, tint = Orchid, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    menuOpen = false
                                    try {
                                        val share = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(
                                                android.content.Intent.EXTRA_TEXT,
                                                "Check out ${u.name} (${u.handle}) on Amora \uD83D\uDC9C amora://profile/${u.id} · Get the app: https://github.com/shakil603/My-love/releases/latest"
                                            )
                                        }
                                        mctx.startActivity(android.content.Intent.createChooser(share, "Share profile"))
                                    } catch (_: Exception) {}
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Copy profile link", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Filled.ContentCopy, null, tint = Orchid, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    menuOpen = false
                                    try {
                                        val cb = mctx.getSystemService(android.content.ClipboardManager::class.java)
                                        cb.setPrimaryClip(
                                            android.content.ClipData.newPlainText("Amora profile", "amora://profile/${u.id}")
                                        )
                                        android.widget.Toast.makeText(mctx, "Profile link copied", android.widget.Toast.LENGTH_SHORT).show()
                                    } catch (_: Exception) {}
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Report", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Filled.Flag, null, tint = Rose, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    menuOpen = false
                                    android.widget.Toast.makeText(mctx, "Report sent — reviewed within 24 h", android.widget.Toast.LENGTH_LONG).show()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (usec) "Screen protection: ON ✓" else "Screen protection: OFF",
                                        fontWeight = FontWeight.Bold, fontSize = 13.sp,
                                        color = if (usec) Mint else Ink
                                    )
                                },
                                leadingIcon = { Icon(Icons.Filled.Verified, null, tint = if (usec) Mint else Txt3, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    usec = !usec
                                    Store.set(mctx, "sec_user_${u.id}", usec.toString())
                                    android.widget.Toast.makeText(
                                        mctx,
                                        if (usec) "Screenshots blocked while viewing this profile" else "Screen protection off for this profile",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (ublocked) "Unblock" else "Block",
                                        fontWeight = FontWeight.Bold, fontSize = 13.sp,
                                        color = if (ublocked) Ink else Rose
                                    )
                                },
                                leadingIcon = { Icon(Icons.Filled.Block, null, tint = Rose, modifier = Modifier.size(18.dp)) },
                                onClick = {
                                    menuOpen = false
                                    ublocked = !ublocked
                                    val cur = Store.blocked(mctx)
                                    Store.setBlocked(mctx, if (ublocked) cur + u.id else cur - u.id)
                                    android.widget.Toast.makeText(
                                        mctx,
                                        if (ublocked) "${u.name} blocked — they can no longer see or message you" else "${u.name} unblocked",
                                        android.widget.Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Blocked list", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Filled.MoreVert, null, tint = Txt3, modifier = Modifier.size(18.dp)) },
                                onClick = { menuOpen = false; go("blocked") }
                            )
                        }
                    }
                }
            }
            Column(
                Modifier.fillMaxWidth().offset(y = (-48).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Box(
                        Modifier.size(104.dp).clip(CircleShape).background(Paper),
                        contentAlignment = Alignment.Center
                    ) { Avatar(u.avatar, 96, ringed = true) }
                    if (u.online) {
                        Box(
                            Modifier.align(Alignment.BottomEnd).padding(6.dp).size(16.dp)
                                .clip(CircleShape).background(Paper),
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
                    val upctx = androidx.compose.ui.platform.LocalContext.current
                    var ufollow by remember { mutableStateOf(u.id in Store.following(upctx)) }
                    val followsYou = u.id in setOf("2", "5", "8")
                    Text(
                        if (ufollow) "Following ✓" else if (followsYou) "Follow back" else "Follow",
                        fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (ufollow) Txt2 else Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(17.dp))
                            .then(if (ufollow) Modifier.background(Cloud) else Modifier.background(AuroraGradient))
                            .clickable {
                                ufollow = !ufollow
                                val cur = Store.following(upctx)
                                Store.setFollowing(upctx, if (ufollow) cur + u.id else cur - u.id)
                            }
                            .padding(vertical = 15.dp)
                    )
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
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Paper)
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
                                    .clickable { go("story") }
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
            .background(Paper).clickable(onClick = onClick),
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
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var avatar by remember { mutableStateOf(Store.avatar(ctx)) }
    var banner by remember { mutableStateOf(Store.get(ctx, "pbanner", "")) }
    var email by remember { mutableStateOf(Store.get(ctx, "pemail", "")) }
    var recovery by remember { mutableStateOf(Store.get(ctx, "precovery", "")) }
    var pin by remember { mutableStateOf(Store.get(ctx, "ppin", "")) }
    val bannerPicker = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                ctx.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            banner = uri.toString()
            Store.set(ctx, "pbanner", banner)
        }
    }
    val avatarPicker = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                ctx.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            avatar = uri.toString()
            Store.set(ctx, "pavatar", avatar)
        }
    }
    var name by remember { mutableStateOf(Store.get(ctx, "pname", "Shakil Rahman")) }
    var handle by remember { mutableStateOf(Store.get(ctx, "phandle", "@shakil")) }
    var bio by remember { mutableStateOf(Store.get(ctx, "pbio", "Builder of Amora · dreaming big from Mymensingh \uD83C\uDF0C")) }
    var work by remember { mutableStateOf(Store.get(ctx, "pwork", "Founder · Amora")) }
    var location by remember { mutableStateOf(Store.get(ctx, "ploc", "Mymensingh, Bangladesh")) }
    Column(Modifier.fillMaxSize().background(Paper)) {
        ScreenHeader(androidx.compose.ui.res.stringResource(R.string.edit_profile), onBack)
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AuroraGradient)
                    .clickable { bannerPicker.launch(arrayOf("image/*")) }
            ) {
                if (banner.isNotBlank()) {
                    coil.compose.AsyncImage(
                        model = banner, contentDescription = "Banner",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.PhotoCamera, "Change banner", tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(5.dp))
                    Text("Change banner", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(Modifier.height(14.dp))
            Box(Modifier.clickable { avatarPicker.launch(arrayOf("image/*")) }) {
                Avatar(avatar, 96, ringed = true)
                Box(
                    Modifier.align(Alignment.BottomEnd).size(32.dp).clip(CircleShape)
                        .background(AuroraGradient).border(2.dp, Paper, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.PhotoCamera, "Change photo", tint = Color.White, modifier = Modifier.size(15.dp)) }
            }
            Spacer(Modifier.height(6.dp))
            Text("Tap to upload a new photo", fontSize = 10.sp, color = Txt3, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(18.dp))
            EditField("Name", name) { name = it }
            EditField("Username", handle) { handle = it }
            EditField("Bio", bio, minLines = 3) { bio = it }
            EditField("Work", work) { work = it }
            EditField("Location", location) { location = it }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Verified, null, tint = Mint, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("SECURE YOUR PROFILE", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
            }
            Spacer(Modifier.height(8.dp))
            EditField("Email (account recovery)", email) { email = it }
            EditField("Recovery phone", recovery) { v -> if (v.all { it.isDigit() || it == ' ' || it == '+' }) recovery = v }
            EditField("Security PIN (4–6 digits)", pin) { v -> if (v.length <= 6 && v.all { it.isDigit() }) pin = v }
            Text(
                when {
                    email.isNotBlank() && recovery.isNotBlank() && pin.length >= 4 -> "Profile fully secured ✓"
                    email.isNotBlank() || recovery.isNotBlank() || pin.isNotBlank() -> "Almost there — fill all three to fully secure"
                    else -> "Add these so you never lose your account"
                },
                fontSize = 10.sp, fontWeight = FontWeight.Bold,
                color = if (email.isNotBlank() && recovery.isNotBlank() && pin.length >= 4) Mint else Txt3,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }
        Box(Modifier.padding(18.dp)) {
            GradientButton("Save changes", Modifier.fillMaxWidth()) {
                Store.set(ctx, "pname", name.trim())
                Store.set(ctx, "phandle", handle.trim())
                Store.set(ctx, "pbio", bio.trim())
                Store.set(ctx, "pwork", work.trim())
                Store.set(ctx, "ploc", location.trim())
                Store.set(ctx, "pemail", email.trim())
                Store.set(ctx, "precovery", recovery.trim())
                Store.set(ctx, "ppin", pin)
                onBack()
            }
        }
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
    val fctx = androidx.compose.ui.platform.LocalContext.current
    val following = remember { mutableStateOf(Store.following(fctx)) }
    Column(Modifier.fillMaxSize().background(Paper)) {
        ScreenHeader("Connections", onBack)
        Row(Modifier.padding(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                "Followers · ${Mock.users.size}",
                "Following · ${Store.following(fctx).count { fid -> Mock.users.any { it.id == fid } }}"
            ).forEachIndexed { i, t ->
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
            if (Mock.users.isEmpty()) {
                item {
                    Column(
                        Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("\uD83E\uDD1D", fontSize = 40.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("No connections yet", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Ink)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Demo accounts are removed — real followers will show up here.",
                            fontSize = 11.sp, color = Txt3, lineHeight = 17.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
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
                                Store.setFollowing(fctx, following.value)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

/* ---------- Full profile setup for new accounts ---------- */
@Composable
fun ProfileSetupScreen(onDone: () -> Unit, onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var avatar by remember { mutableStateOf(Store.get(ctx, "pavatar", "")) }
    var name by remember { mutableStateOf(Store.get(ctx, "pname", "")) }
    var handle by remember { mutableStateOf(Store.get(ctx, "phandle", "")) }
    var bio by remember { mutableStateOf(Store.get(ctx, "pbio", "")) }
    var work by remember { mutableStateOf(Store.get(ctx, "pwork", "")) }
    var location by remember { mutableStateOf(Store.get(ctx, "ploc", "")) }
    var gender by remember { mutableStateOf(Store.get(ctx, "pgender", "")) }
    var bday by remember { mutableStateOf(Store.get(ctx, "pbday", "")) }
    var interests by remember { mutableStateOf(Store.get(ctx, "pinterests", "")) }
    var banner by remember { mutableStateOf(Store.get(ctx, "pbanner", "")) }
    var email by remember { mutableStateOf(Store.get(ctx, "pemail", "")) }
    var recovery by remember { mutableStateOf(Store.get(ctx, "precovery", "")) }
    var pin by remember { mutableStateOf(Store.get(ctx, "ppin", "")) }
    val bannerPicker = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                ctx.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            banner = uri.toString()
        }
    }
    val picker = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                ctx.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            avatar = uri.toString()
        }
    }
    val allInterests = listOf(
        "Travel", "Music", "Coding", "Photography", "Food", "Movies",
        "Books", "Fitness", "Art", "Gaming", "Nature", "Business"
    )
    val selInterests = interests.split(",").filter { it.isNotBlank() }.toSet()
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding().navigationBarsPadding()) {
        ScreenHeader("Create your profile", onBack, sub = "All your info · photo · interests")
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))
            Box(Modifier.clickable { picker.launch(arrayOf("image/*")) }) {
                if (avatar.isNotBlank()) {
                    Avatar(avatar, 104, ringed = true)
                } else {
                    Box(
                        Modifier.size(104.dp).clip(CircleShape).background(Cloud)
                            .border(2.dp, Mist, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.PhotoCamera, "Upload photo", tint = Txt3, modifier = Modifier.size(30.dp)) }
                }
                Box(
                    Modifier.align(Alignment.BottomEnd).size(32.dp).clip(CircleShape)
                        .background(AuroraGradient).border(2.dp, Paper, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.PhotoCamera, null, tint = Color.White, modifier = Modifier.size(15.dp)) }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                if (avatar.isBlank()) "Tap to upload your photo" else "Photo added ✓ · tap to change",
                fontSize = 10.sp, color = if (avatar.isBlank()) Txt3 else Mint, fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AuroraGradient)
                    .clickable { bannerPicker.launch(arrayOf("image/*")) }
            ) {
                if (banner.isNotBlank()) {
                    coil.compose.AsyncImage(
                        model = banner, contentDescription = "Banner",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Row(
                    Modifier.align(Alignment.Center)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.PhotoCamera, null, tint = Color.White, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        if (banner.isBlank()) "Upload profile banner" else "Banner added ✓ · tap to change",
                        color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            EditField("Name", name) { name = it }
            EditField("Username", handle) { handle = it }
            EditField("Bio", bio, minLines = 3) { bio = it }
            EditField("Work", work) { work = it }
            EditField("Location", location) { location = it }
            EditField("Birthday (DD/MM/YYYY)", bday) { bday = it }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Verified, null, tint = Mint, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("SECURE YOUR PROFILE", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
            }
            Spacer(Modifier.height(8.dp))
            EditField("Email (account recovery)", email) { email = it }
            EditField("Recovery phone", recovery) { v -> if (v.all { it.isDigit() || it == ' ' || it == '+' }) recovery = v }
            EditField("Security PIN (4–6 digits)", pin) { v -> if (v.length <= 6 && v.all { it.isDigit() }) pin = v }
            Text(
                when {
                    email.isNotBlank() && recovery.isNotBlank() && pin.length >= 4 -> "Profile fully secured ✓"
                    else -> "Email + recovery phone + PIN keep your account safe"
                },
                fontSize = 10.sp, fontWeight = FontWeight.Bold,
                color = if (email.isNotBlank() && recovery.isNotBlank() && pin.length >= 4) Mint else Txt3,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            Text("PRIVACY — PROFILE & MESSAGES", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            PrivacyPicker("Profile visibility", "Who can see your profile", "priv_profile", Icons.Filled.Verified)
            PrivacyPicker("Messages", "Who can message you", "priv_msg", Icons.Filled.Favorite)
            Spacer(Modifier.height(4.dp))
            Column(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                Text("GENDER", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Female", "Male", "Other").forEach { g ->
                        val on = gender == g
                        Box(
                            Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                                .background(if (on) Ink else Cloud)
                                .clickable { gender = g }
                                .padding(vertical = 11.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                g, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (on) Paper else Ink
                            )
                        }
                    }
                }
            }
            Column(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                Text("INTERESTS — PICK ANY", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
                Spacer(Modifier.height(6.dp))
                allInterests.chunked(3).forEach { rowI ->
                    Row(Modifier.fillMaxWidth().padding(bottom = 6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        rowI.forEach { tag ->
                            val on = tag in selInterests
                            Box(
                                Modifier.weight(1f).clip(RoundedCornerShape(50))
                                    .background(if (on) AuroraGradient else androidx.compose.ui.graphics.SolidColor(Cloud))
                                    .clickable {
                                        val next = if (on) selInterests - tag else selInterests + tag
                                        interests = next.joinToString(",")
                                    }
                                    .padding(vertical = 9.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    tag, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                                    color = if (on) Color.White else Txt2
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
        }
        Box(Modifier.padding(18.dp)) {
            GradientButton(
                if (name.isBlank()) "Add your name to continue" else "Create my profile", Modifier.fillMaxWidth()
            ) {
                if (name.isNotBlank()) {
                    if (avatar.isNotBlank()) Store.set(ctx, "pavatar", avatar)
                    Store.set(ctx, "pname", name.trim())
                    Store.set(ctx, "phandle", if (handle.isBlank()) "@" + name.trim().lowercase().replace(" ", "") else handle.trim())
                    Store.set(ctx, "pbio", bio.trim())
                    Store.set(ctx, "pwork", work.trim())
                    Store.set(ctx, "ploc", location.trim())
                    Store.set(ctx, "pgender", gender)
                    Store.set(ctx, "pbday", bday.trim())
                    Store.set(ctx, "pinterests", interests)
                    if (banner.isNotBlank()) Store.set(ctx, "pbanner", banner)
                    Store.set(ctx, "pemail", email.trim())
                    Store.set(ctx, "precovery", recovery.trim())
                    Store.set(ctx, "ppin", pin)
                    onDone()
                }
            }
        }
    }
}


/* ---------- Blocked list — unblock anyone anytime ---------- */
@Composable
fun BlockedScreen(onBack: () -> Unit, go: (String) -> Unit = {}) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var blocked by remember { mutableStateOf(Store.blocked(ctx)) }
    Column(Modifier.fillMaxSize().background(Paper).statusBarsPadding()) {
        ScreenHeader("Blocked", onBack, sub = "${blocked.size} blocked · unblock anytime")
        if (blocked.isEmpty()) {
            Column(
                Modifier.fillMaxSize().padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(60.dp))
                Icon(Icons.Filled.Block, null, tint = Mist, modifier = Modifier.size(56.dp))
                Spacer(Modifier.height(14.dp))
                Text("Nobody is blocked", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Block anyone from their profile, a chat or a group — they appear here and can be unblocked anytime.",
                    fontSize = 11.sp, color = Txt3, lineHeight = 17.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
                items(blocked.toList().size) { i ->
                    val id = blocked.toList()[i]
                    val user = Mock.usersAll.firstOrNull { it.id == id }
                    val chat = Mock.chatsAll.firstOrNull { it.id == id }
                    val name = user?.name ?: chat?.name ?: "User $id"
                    val avatar = user?.avatar ?: chat?.avatar ?: ""
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { go("user/$id") }
                            .padding(horizontal = 18.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (avatar.isNotBlank()) Avatar(avatar, 48)
                        else Box(
                            Modifier.size(48.dp).clip(CircleShape).background(Cloud),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Block, null, tint = Rose, modifier = Modifier.size(20.dp)) }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(name, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Ink)
                            Text("Can't see you, message you or call you", fontSize = 10.sp, color = Txt3)
                        }
                        Text(
                            "Unblock", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White,
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Ink)
                                .clickable {
                                    blocked = blocked - id
                                    Store.setBlocked(ctx, blocked)
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
