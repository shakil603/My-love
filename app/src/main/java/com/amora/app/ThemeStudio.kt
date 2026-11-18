package com.amora.app

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Presets = listOf(
    Triple("Aurora", Color(0xFFFF4D6D), Color(0xFFC13BFF) to Color(0xFF7B2CFF)),
    Triple("Ocean", Color(0xFF06B6D4), Color(0xFF3B82F6) to Color(0xFF6366F1)),
    Triple("Sunset", Color(0xFFF97316), Color(0xFFEF4444) to Color(0xFFDB2777)),
    Triple("Forest", Color(0xFF22C55E), Color(0xFF10B981) to Color(0xFF0D9488)),
    Triple("Candy", Color(0xFFF472B6), Color(0xFFE879F9) to Color(0xFFA78BFA)),
    Triple("Gold", Color(0xFFF59E0B), Color(0xFFD97706) to Color(0xFF92400E))
)

private val Swatches = listOf(
    0xFFFF4D6D, 0xFFF43F5E, 0xFFEF4444, 0xFFF97316, 0xFFF59E0B, 0xFFEAB308,
    0xFF84CC16, 0xFF22C55E, 0xFF10B981, 0xFF14B8A6, 0xFF06B6D4, 0xFF0EA5E9,
    0xFF3B82F6, 0xFF6366F1, 0xFF7B2CFF, 0xFF8B5CF6, 0xFFA855F7, 0xFFC13BFF,
    0xFFD946EF, 0xFFEC4899, 0xFFF472B6, 0xFF64748B, 0xFF17122B, 0xFF000000
).map { Color(it) }

@Composable
fun ThemeScreen(onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var target by remember { mutableStateOf("rose") }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            try {
                ctx.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {}
            val mime = try { ctx.contentResolver.getType(uri) ?: "" } catch (_: Exception) { "" }
            if (mime.startsWith("image/") || mime.isBlank()) {
                ThemeX.setWallpaper(ctx, uri.toString())
            } else {
                Store.addFile(ctx, uri.lastPathSegment ?: "file", uri.toString(), mime, "")
                android.widget.Toast.makeText(
                    ctx, "Saved to My files — only a photo or GIF can be the wallpaper", android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LazyColumn(
        Modifier.fillMaxSize().background(Cloud).statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 30.dp)
    ) {
        item { ScreenHeader("Appearance & colors", onBack, sub = "Dark mode · theme colors · wallpaper") }

        /* ---- Mode ---- */
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("MODE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Auto", "Light", "Dark").forEach { m ->
                        val on = ThemeX.mode.value == m
                        Box(
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (on) Ink else Paper)
                                .clickable { ThemeX.setMode(ctx, m) }
                                .padding(vertical = 13.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                m, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (on) Paper else Ink
                            )
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    if (ThemeX.dark) "Dark mode is ON" else "Light mode is ON",
                    fontSize = 10.sp, color = Txt3, fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(18.dp))
            }
        }

        /* ---- Presets ---- */
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("THEME PRESETS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Presets.chunked(3).forEach { rowP ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowP.forEach { (name, c1, pair) ->
                            val (c2, c3) = pair
                            Column(
                                Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Paper)
                                    .clickable {
                                        ThemeX.setColor(ctx, "rose", c1)
                                        ThemeX.setColor(ctx, "orchid", c2)
                                        ThemeX.setColor(ctx, "violet", c3)
                                    }
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(22.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            androidx.compose.ui.graphics.Brush.linearGradient(listOf(c1, c2, c3))
                                        )
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(name, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        /* ---- Custom color editor ---- */
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("EDIT ANY COLOR", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Column(Modifier.clip(RoundedCornerShape(18.dp)).background(Paper).padding(14.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("rose" to "Primary", "orchid" to "Accent", "violet" to "Deep").forEach { (key, label) ->
                            val cur = when (key) { "rose" -> Rose; "orchid" -> Orchid; else -> Violet }
                            val on = target == key
                            Column(
                                Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .then(
                                        if (on) Modifier.border(2.dp, cur, RoundedCornerShape(12.dp))
                                        else Modifier.border(1.dp, Mist, RoundedCornerShape(12.dp))
                                    )
                                    .clickable { target = key }
                                    .padding(vertical = 9.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.size(20.dp).clip(CircleShape).background(cur))
                                Spacer(Modifier.height(4.dp))
                                Text(label, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Swatches.chunked(6).forEach { rowC ->
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowC.forEach { c ->
                                val sel = when (target) { "rose" -> Rose; "orchid" -> Orchid; else -> Violet } == c
                                Box(
                                    Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(c)
                                        .then(if (sel) Modifier.border(3.dp, Ink, CircleShape) else Modifier)
                                        .clickable { ThemeX.setColor(ctx, target, c) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (sel) Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    /* fine-tune sliders */
                    val cur = when (target) { "rose" -> Rose; "orchid" -> Orchid; else -> Violet }
                    var r by remember(target, cur.toArgb()) { mutableStateOf(cur.red) }
                    var g by remember(target, cur.toArgb()) { mutableStateOf(cur.green) }
                    var b by remember(target, cur.toArgb()) { mutableStateOf(cur.blue) }
                    listOf(
                        Triple("R", r, { v: Float -> r = v; ThemeX.setColor(ctx, target, Color(r, g, b)) }),
                        Triple("G", g, { v: Float -> g = v; ThemeX.setColor(ctx, target, Color(r, g, b)) }),
                        Triple("B", b, { v: Float -> b = v; ThemeX.setColor(ctx, target, Color(r, g, b)) })
                    ).forEach { (lbl, value, set) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(lbl, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, modifier = Modifier.width(16.dp))
                            Slider(
                                value = value, onValueChange = set,
                                colors = SliderDefaults.colors(thumbColor = cur, activeTrackColor = cur),
                                modifier = Modifier.weight(1f).height(26.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Live preview", fontSize = 10.sp, color = Txt3, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Box(
                        Modifier.fillMaxWidth().height(40.dp).clip(RoundedCornerShape(12.dp)).background(AuroraGradient),
                        contentAlignment = Alignment.Center
                    ) { Text("Buttons · gradients · accents", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold) }
                }
                Spacer(Modifier.height(18.dp))
            }
        }

        /* ---- Wallpaper ---- */
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("CHAT WALLPAPER — PHOTO OR GIF", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Txt3, letterSpacing = 2.sp)
                Spacer(Modifier.height(8.dp))
                Column(Modifier.clip(RoundedCornerShape(18.dp)).background(Paper).padding(14.dp)) {
                    if (ThemeX.wallpaper.value.isNotEmpty()) {
                        AsyncImage(
                            model = ThemeX.wallpaper.value,
                            contentDescription = "Wallpaper",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(130.dp).clip(RoundedCornerShape(14.dp))
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AuroraGradient)
                                .clickable { picker.launch(arrayOf("*/*")) }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.Image, null, tint = Color.White, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                if (ThemeX.wallpaper.value.isEmpty()) "Upload any file" else "Change",
                                color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold
                            )
                        }
                        if (ThemeX.wallpaper.value.isNotEmpty()) {
                            Row(
                                Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.5.dp, Mist, RoundedCornerShape(12.dp))
                                    .clickable { ThemeX.setWallpaper(ctx, "") }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.Close, null, tint = Rose, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(5.dp))
                                Text("Remove", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Rose)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Any file accepted — photos and animated GIFs become your chat skin; other files go to My files.", fontSize = 10.sp, color = Txt3)
                }
                Spacer(Modifier.height(18.dp))
            }
        }

        /* ---- Reset ---- */
        item {
            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Paper)
                    .clickable { ThemeX.reset(ctx) }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Reset to Amora default", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Rose)
            }
        }
    }
}
