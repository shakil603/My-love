package com.amora.app

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat

/* ---------- Language (real Android per-app locale) ---------- */

private data class Lang(val tag: String, val native: String, val english: String)

private val LANGS = listOf(
    Lang("en", "English", "English"),
    Lang("bn", "বাংলা", "Bengali"),
    Lang("ar", "العربية", "Arabic"),
    Lang("es", "Español", "Spanish"),
    Lang("hi", "हिन्दी", "Hindi"),
    Lang("zh", "中文", "Chinese"),
    Lang("fr", "Français", "French"),
    Lang("pt", "Português", "Portuguese"),
    Lang("ru", "Русский", "Russian"),
    Lang("ja", "日本語", "Japanese"),
    Lang("de", "Deutsch", "German"),
    Lang("tr", "Türkçe", "Turkish"),
    Lang("ur", "اردو", "Urdu"),
    Lang("id", "Bahasa Indonesia", "Indonesian")
)

@Composable
fun LanguageScreen(onBack: () -> Unit) {
    var current by remember {
        mutableStateOf(AppCompatDelegate.getApplicationLocales().toLanguageTags().ifBlank { "en" })
    }
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("Language · ভাষা", onBack, sub = "Applies across the whole app, instantly")
        Row(
            Modifier.padding(horizontal = 18.dp).fillMaxWidth()
                .clip(RoundedCornerShape(14.dp)).background(Color(0xFFF6E7FF)).padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Public, null, tint = Orchid, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                "Also available in Android Settings → Apps → Amora → Language",
                fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFA21CAF), lineHeight = 14.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
            items(LANGS.size) { i ->
                val l = LANGS[i]
                val selected = current.startsWith(l.tag)
                Row(
                    Modifier.fillMaxWidth()
                        .clickable {
                            current = l.tag
                            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(l.tag))
                        }
                        .padding(horizontal = 22.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(l.native, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
                        Text(l.english, fontSize = 11.sp, color = Txt3, fontWeight = FontWeight.Medium)
                    }
                    if (selected) {
                        Box(
                            Modifier.size(24.dp).clip(CircleShape).background(AuroraGradient),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
                    } else {
                        Box(Modifier.size(24.dp).clip(CircleShape).border(2.dp, Mist, CircleShape))
                    }
                }
            }
        }
    }
}

/* ---------- Permissions (real Android runtime permissions) ---------- */

private data class PermItem(
    val icon: ImageVector,
    val title: String,
    val desc: String,
    val perms: List<String>
)

@Composable
fun PermissionsScreen(onDone: () -> Unit) {
    val ctx = LocalContext.current
    var refresh by remember { mutableIntStateOf(0) }

    val items = remember {
        buildList {
            add(PermItem(Icons.Filled.Camera, "Camera", "Photos, video calls and stories", listOf(android.Manifest.permission.CAMERA)))
            add(PermItem(Icons.Filled.Mic, "Microphone", "Voice messages and calls", listOf(android.Manifest.permission.RECORD_AUDIO)))
            if (Build.VERSION.SDK_INT >= 33) {
                add(PermItem(Icons.Filled.Notifications, "Notifications", "Messages, calls and matches", listOf("android.permission.POST_NOTIFICATIONS")))
                add(PermItem(Icons.Filled.PhotoLibrary, "Photos & media", "Share any file with preview", listOf("android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO")))
            } else {
                add(PermItem(Icons.Filled.PhotoLibrary, "Photos & media", "Share any file with preview", listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)))
            }
            add(PermItem(Icons.Filled.LocationOn, "Location", "People nearby and live location", listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION)))
        }
    }

    fun granted(p: PermItem): Boolean {
        refresh // recompose dependency
        return p.perms.all { ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        refresh++
    }

    Column(
        Modifier.fillMaxSize().background(Color(0xFFFBFAFE)).padding(horizontal = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(60.dp))
        Box(
            Modifier.size(72.dp).clip(RoundedCornerShape(24.dp)).background(AuroraGradient),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(32.dp)) }
        Spacer(Modifier.height(18.dp))
        Text("Allow permissions", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
        Spacer(Modifier.height(6.dp))
        Text(
            "Amora works worldwide — these Android permissions unlock calls, voice, media and nearby features. You stay in control.",
            fontSize = 12.sp, color = Txt2, lineHeight = 18.sp, fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(22.dp))
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            items.forEach { p ->
                val ok = granted(p)
                Row(
                    Modifier.fillMaxWidth().padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(18.dp)).background(Color.White)
                        .border(1.dp, if (ok) Mint else Color(0xFFEFEBF8), RoundedCornerShape(18.dp))
                        .clickable { launcher.launch(p.perms.toTypedArray()) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(42.dp).clip(RoundedCornerShape(14.dp))
                            .background(if (ok) Color(0xFFE9FBF3) else Color(0xFFF6E7FF)),
                        contentAlignment = Alignment.Center
                    ) { Icon(p.icon, null, tint = if (ok) Color(0xFF10B981) else Orchid, modifier = Modifier.size(19.dp)) }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(p.title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Ink)
                        Text(p.desc, fontSize = 11.sp, color = Txt2, fontWeight = FontWeight.Medium)
                    }
                    Text(
                        if (ok) "Granted" else "Allow",
                        fontSize = 11.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (ok) Color(0xFF10B981) else Color.White,
                        modifier = Modifier.clip(RoundedCornerShape(50))
                            .background(if (ok) Color(0xFFE9FBF3) else Ink)
                            .padding(horizontal = 13.dp, vertical = 7.dp)
                    )
                }
            }
        }
        GradientButton("Continue", Modifier.fillMaxWidth(), onClick = onDone)
        Spacer(Modifier.height(10.dp))
        Text(
            "Skip for now", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Txt2,
            modifier = Modifier.clickable(onClick = onDone).padding(8.dp)
        )
        Spacer(Modifier.height(18.dp))
    }
}

/* ---------- Create group (no limits) ---------- */

@Composable
fun CreateGroupScreen(onBack: () -> Unit, onCreated: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(setOf<String>()) }
    Column(Modifier.fillMaxSize().background(Color(0xFFFBFAFE))) {
        ScreenHeader("New group", onBack, sub = "Unlimited groups · unlimited members")
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            TextField(
                value = name, onValueChange = { name = it },
                placeholder = { Text("Group name…", fontSize = 13.sp, color = Txt3) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))
            Text("ADD MEMBERS · ${selected.size} SELECTED", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3)
            Spacer(Modifier.height(6.dp))
            Mock.users.forEach { u ->
                val on = u.id in selected
                Row(
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selected = if (on) selected - u.id else selected + u.id }
                        .padding(vertical = 9.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(u.avatar, 44)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(u.name, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Ink)
                        Text(u.handle, fontSize = 11.sp, color = Txt3, fontWeight = FontWeight.Medium)
                    }
                    if (on) {
                        Box(
                            Modifier.size(24.dp).clip(CircleShape).background(AuroraGradient),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
                    } else {
                        Box(Modifier.size(24.dp).clip(CircleShape).border(2.dp, Mist, CircleShape))
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        Box(Modifier.padding(18.dp)) {
            GradientButton(
                if (selected.isEmpty()) "Create group" else "Create group (${selected.size + 1} members)",
                Modifier.fillMaxWidth(),
                onClick = onCreated
            )
        }
    }
}
