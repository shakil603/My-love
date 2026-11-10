package com.amora.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Rose = Color(0xFFFF4D6D)
val RoseLight = Color(0xFFFF758F)
val Orchid = Color(0xFFC13BFF)
val Violet = Color(0xFF7B2CFF)
val Indigo = Color(0xFF4F46E5)
val Ink = Color(0xFF17122B)
val InkDeep = Color(0xFF0E0B16)
val Cloud = Color(0xFFF6F5FA)
val Mist = Color(0xFFECEAF4)
val Txt2 = Color(0xFF5D5775)
val Txt3 = Color(0xFF9A94B0)
val Mint = Color(0xFF2DD4BF)
val Amber = Color(0xFFFFB020)
val Sky = Color(0xFF38BDF8)

val AuroraGradient = Brush.linearGradient(listOf(Rose, Orchid, Violet))
val AuroraSoft = Brush.linearGradient(listOf(Color(0xFFFFE3E9), Color(0xFFEFE3FF)))
val NightGradient = Brush.verticalGradient(listOf(Color(0xFF1A0B2E), Color(0xFF3B0F3F), Violet))
val VaultGradient = Brush.linearGradient(listOf(Ink, Color(0xFF372A5E)))

private val LightColors = lightColorScheme(
    primary = Orchid,
    secondary = Rose,
    tertiary = Violet,
    background = Cloud,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Ink,
    onSurface = Ink
)

@Composable
fun AmoraTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
