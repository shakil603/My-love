package com.amora.app

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/* ---------- Default brand palette ---------- */
val DefRose = Color(0xFFFF4D6D)
val DefOrchid = Color(0xFFC13BFF)
val DefViolet = Color(0xFF7B2CFF)

/* ---------- Screenshot / screen-record protection (FLAG_SECURE) ---------- */
object SecureX {
    val on = mutableStateOf(false)      // global toggle
    val local = mutableStateOf(false)   // active for the current chat/profile/group
}

/* ---------- Live theme engine: dark mode + user-editable colors + wallpaper ---------- */
object ThemeX {
    val mode = mutableStateOf("Auto")            // Auto | Light | Dark
    val systemDark = mutableStateOf(false)
    val rose = mutableStateOf(DefRose)
    val orchid = mutableStateOf(DefOrchid)
    val violet = mutableStateOf(DefViolet)
    val wallpaper = mutableStateOf("")

    val dark: Boolean
        get() = when (mode.value) { "Dark" -> true; "Light" -> false; else -> systemDark.value }

    fun load(ctx: Context) {
        mode.value = Store.get(ctx, "th_mode", "Auto")
        rose.value = col(Store.get(ctx, "th_rose", ""), DefRose)
        orchid.value = col(Store.get(ctx, "th_orchid", ""), DefOrchid)
        violet.value = col(Store.get(ctx, "th_violet", ""), DefViolet)
        wallpaper.value = Store.get(ctx, "wallpaper", "")
    }

    fun setMode(ctx: Context, m: String) { mode.value = m; Store.set(ctx, "th_mode", m) }

    fun setColor(ctx: Context, which: String, c: Color) {
        when (which) {
            "rose" -> rose.value = c
            "orchid" -> orchid.value = c
            "violet" -> violet.value = c
        }
        Store.set(ctx, "th_$which", c.toArgb().toString())
    }

    fun setWallpaper(ctx: Context, uri: String) { wallpaper.value = uri; Store.set(ctx, "wallpaper", uri) }

    /* Extract dominant colors from an uploaded image and re-theme buttons/gradients app-wide */
    fun themeFromImage(ctx: Context, uri: String) {
        Thread {
            try {
                val src = android.net.Uri.parse(uri)
                val bmp = if (android.os.Build.VERSION.SDK_INT >= 28) {
                    val d = android.graphics.ImageDecoder.createSource(ctx.contentResolver, src)
                    android.graphics.ImageDecoder.decodeBitmap(d) { dec, _, _ ->
                        dec.setTargetSize(220, 220)
                        dec.allocator = android.graphics.ImageDecoder.ALLOCATOR_SOFTWARE
                    }
                } else {
                    ctx.contentResolver.openInputStream(src)?.use {
                        android.graphics.BitmapFactory.decodeStream(it)
                    }
                } ?: return@Thread
                val p = androidx.palette.graphics.Palette.from(bmp).maximumColorCount(24).generate()
                val c1 = p.getVibrantColor(p.getLightVibrantColor(DefRose.toArgb()))
                val c2 = p.getLightVibrantColor(p.getMutedColor(DefOrchid.toArgb()))
                val c3 = p.getDarkVibrantColor(p.getDarkMutedColor(DefViolet.toArgb()))
                setColor(ctx, "rose", Color(c1))
                setColor(ctx, "orchid", Color(c2))
                setColor(ctx, "violet", Color(c3))
            } catch (_: Exception) {}
        }.start()
    }

    fun reset(ctx: Context) {
        setColor(ctx, "rose", DefRose); setColor(ctx, "orchid", DefOrchid); setColor(ctx, "violet", DefViolet)
        setMode(ctx, "Auto"); setWallpaper(ctx, "")
    }

    private fun col(s: String, def: Color): Color =
        try { if (s.isBlank()) def else Color(s.toInt()) } catch (_: Exception) { def }
}

/* ---------- Dynamic colors — every screen reads these live ---------- */
val Rose: Color get() = ThemeX.rose.value
val RoseLight: Color get() = ThemeX.rose.value.copy(alpha = 0.75f)
val Orchid: Color get() = ThemeX.orchid.value
val Violet: Color get() = ThemeX.violet.value
val Indigo = Color(0xFF4F46E5)
val Ink: Color get() = if (ThemeX.dark) Color(0xFFF3F1FA) else Color(0xFF17122B)
val InkDeep = Color(0xFF0E0B16)
val Paper: Color get() = if (ThemeX.dark) Color(0xFF1C1829) else Color.White
val Cloud: Color get() = if (ThemeX.dark) Color(0xFF131020) else Color(0xFFF6F5FA)
val Mist: Color get() = if (ThemeX.dark) Color(0xFF2E2842) else Color(0xFFECEAF4)
val Txt2: Color get() = if (ThemeX.dark) Color(0xFFC8C2DC) else Color(0xFF5D5775)
val Txt3: Color get() = if (ThemeX.dark) Color(0xFF8C85A6) else Color(0xFF9A94B0)
val Mint = Color(0xFF2DD4BF)
val Amber = Color(0xFFFFB020)
val Sky = Color(0xFF38BDF8)

val AuroraGradient: Brush get() = Brush.linearGradient(listOf(Rose, Orchid, Violet))
val AuroraSoft: Brush
    get() = if (ThemeX.dark) Brush.linearGradient(listOf(Color(0xFF3A1E33), Color(0xFF2A1E45)))
    else Brush.linearGradient(listOf(Color(0xFFFFE3E9), Color(0xFFEFE3FF)))
val NightGradient: Brush get() = Brush.verticalGradient(listOf(Color(0xFF1A0B2E), Color(0xFF3B0F3F), Violet))
val VaultGradient: Brush get() = Brush.linearGradient(listOf(Color(0xFF17122B), Color(0xFF372A5E)))

@Composable
fun AmoraTheme(content: @Composable () -> Unit) {
    ThemeX.systemDark.value = isSystemInDarkTheme()
    val scheme = if (ThemeX.dark) darkColorScheme(
        primary = Orchid, secondary = Rose, tertiary = Violet,
        background = Cloud, surface = Paper,
        onPrimary = Color.White, onBackground = Ink, onSurface = Ink
    ) else lightColorScheme(
        primary = Orchid, secondary = Rose, tertiary = Violet,
        background = Cloud, surface = Paper,
        onPrimary = Color.White, onBackground = Ink, onSurface = Ink
    )
    MaterialTheme(colorScheme = scheme) {
        CompositionLocalProvider(LocalContentColor provides Ink, content = content)
    }
}
