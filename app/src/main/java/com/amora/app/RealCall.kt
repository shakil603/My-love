package com.amora.app

import android.annotation.SuppressLint
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import java.net.URLEncoder

/* =====================================================================
   Real audio/video calls on an open-source Jitsi Meet server.
   Media is encrypted in transit (DTLS-SRTP, WebRTC standard) and the
   server (meet.ffmuc.net — community-run, fully open source) never
   stores calls. Room names are random-ish per chat room, shared only
   inside the E2E-encrypted live chat.
   ===================================================================== */

private const val MEET_SERVER = "https://meet.ffmuc.net"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MeetCallScreen(room: String, video: Boolean, onBack: () -> Unit) {
    val ctx = LocalContext.current
    val nick = remember { LivePrefs.nick(ctx) }
    val url = remember {
        val name = URLEncoder.encode("\"$nick\"", "UTF-8")
        MEET_SERVER + "/amora-secure-" + room +
            "#config.disableDeepLinking=true" +
            "&config.prejoinConfig.enabled=false" +
            (if (!video) "&config.startWithVideoMuted=true" else "") +
            "&userInfo.displayName=" + name
    }
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { c ->
                WebView(c).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    webViewClient = WebViewClient()
                    webChromeClient = object : WebChromeClient() {
                        override fun onPermissionRequest(request: PermissionRequest) {
                            request.grant(request.resources)
                        }
                    }
                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize(),
            onRelease = { w ->
                try {
                    w.loadUrl("about:blank")
                    w.destroy()
                } catch (_: Exception) {}
            }
        )
        Row(
            Modifier
                .statusBarsPadding()
                .padding(10.dp)
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(50))
                .background(Color(0x99000000))
                .padding(horizontal = 11.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Lock, null, tint = Mint, modifier = Modifier.size(11.dp))
            Spacer(Modifier.width(5.dp))
            Text(
                if (video) "Encrypted video call" else "Encrypted voice call",
                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold
            )
        }
        Box(
            Modifier
                .statusBarsPadding()
                .padding(10.dp)
                .align(Alignment.TopEnd)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0x99000000))
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Close, "End", tint = Color.White, modifier = Modifier.size(18.dp)) }
    }
}
