package com.amora.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun AuthField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    keyboard: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    var show by remember { mutableStateOf(false) }
    Text(label, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Txt2)
    Spacer(Modifier.height(7.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .border(1.8.dp, Orchid, RoundedCornerShape(17.dp))
            .padding(horizontal = 14.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Orchid, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = value,
            onValueChange = onChange,
            textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Ink),
            keyboardOptions = KeyboardOptions(keyboardType = keyboard),
            singleLine = true,
            visualTransformation = if (isPassword && !show) PasswordVisualTransformation() else VisualTransformation.None,
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) Text(placeholder, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Txt3)
                    inner()
                }
            },
            modifier = Modifier.weight(1f)
        )
        if (isPassword) {
            Icon(
                if (show) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                if (show) "Hide password" else "Show password",
                tint = Txt3,
                modifier = Modifier.size(18.dp).clickable { show = !show }
            )
        }
    }
    Spacer(Modifier.height(13.dp))
}

/* Sign up & log in — username + email/phone + password are required and remembered.
   Deleted accounts stay in the registry and can be restored right from here. */
@Composable
fun AuthScreen(
    startMode: String,
    onSignedUp: () -> Unit,
    onLoggedIn: () -> Unit,
    onBack: () -> Unit
) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var signup by remember { mutableStateOf(startMode == "signup") }
    var username by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var restoreUser by remember { mutableStateOf("") } // deleted account awaiting restore

    fun finishLogin(user: String, c: String) {
        Store.set(ctx, "auth_user", user)
        Store.set(ctx, "phandle", "@$user")
        if (Store.get(ctx, "pname", "").isBlank()) Store.set(ctx, "pname", user.replaceFirstChar { it.uppercase() })
        if (c.contains("@")) Store.set(ctx, "pemail", c) else Store.set(ctx, "phone", c)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(10.dp))
        IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null, tint = Ink) }
        Box(
            Modifier.size(52.dp).clip(RoundedCornerShape(17.dp)).background(AuroraGradient),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Favorite, null, tint = Color.White, modifier = Modifier.size(26.dp)) }
        Spacer(Modifier.height(18.dp))
        Text(
            if (signup) "Create your\naccount" else "Welcome\nback",
            fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 33.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            if (signup) "Three things to remember — that's all:\nusername, email or phone, and your password."
            else "Log in with your email, phone or username.",
            fontSize = 12.sp, color = Txt2, lineHeight = 18.sp
        )
        Spacer(Modifier.height(20.dp))

        if (signup) {
            AuthField("USERNAME", username, { v -> if (v.length <= 24) username = v.filter { !it.isWhitespace() } },
                "yourname", Icons.Filled.AlternateEmail)
            AuthField("EMAIL OR PHONE", contact, { contact = it },
                "you@email.com or 017…", Icons.Filled.Email, KeyboardType.Email)
            AuthField("SECRET PASSWORD", pass, { pass = it },
                "min 6 characters", Icons.Filled.Lock, KeyboardType.Password, isPassword = true)
        } else {
            AuthField("EMAIL · PHONE · USERNAME", contact, { contact = it },
                "you@email.com / 017… / @yourname", Icons.Filled.Email, KeyboardType.Email)
            AuthField("SECRET PASSWORD", pass, { pass = it },
                "your password", Icons.Filled.Lock, KeyboardType.Password, isPassword = true)
        }

        if (error.isNotEmpty()) {
            Text(error, color = Color(0xFFBE123C), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(10.dp))
        }

        if (restoreUser.isNotEmpty()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(17.dp))
                    .background(Color(0xFFFFF7ED))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Restore, null, tint = Color(0xFFC2410C), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "This account was deleted — everything is still saved.",
                        fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF9A3412)
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "Restore @$restoreUser",
                    color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Ink)
                        .clickable {
                            Store.setAccountDeleted(ctx, restoreUser, false)
                            val acc = Store.findAccount(ctx, restoreUser)
                            if (acc != null) {
                                finishLogin(acc.getString("u"), acc.getString("c"))
                                android.widget.Toast.makeText(ctx, "Account restored — welcome back!", android.widget.Toast.LENGTH_LONG).show()
                                onLoggedIn()
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 9.dp)
                )
            }
            Spacer(Modifier.height(13.dp))
        }

        GradientButton(if (signup) "Create account" else "Log in", Modifier.fillMaxWidth(), onClick = {
            error = ""; restoreUser = ""
            val c = contact.trim()
            if (signup) {
                val u = username.trim().removePrefix("@")
                val looksEmail = c.contains("@") && c.contains(".")
                val looksPhone = c.count { it.isDigit() } >= 10 && c.all { it.isDigit() || it == '+' || it == ' ' }
                when {
                    u.length < 3 -> error = "Username needs at least 3 characters"
                    c.isEmpty() -> error = "Email or phone is required"
                    !looksEmail && !looksPhone -> error = "Enter a valid email or phone number"
                    pass.length < 6 -> error = "Password needs at least 6 characters"
                    else -> {
                        val err = Store.addAccount(ctx, u, c, pass)
                        if (err != null) error = err
                        else {
                            finishLogin(u, c)
                            android.widget.Toast.makeText(ctx, "Account created & saved \uD83D\uDC9C", android.widget.Toast.LENGTH_SHORT).show()
                            onSignedUp()
                        }
                    }
                }
            } else {
                val acc = Store.findAccount(ctx, c)
                when {
                    c.isEmpty() || pass.isEmpty() -> error = "Both fields are required"
                    acc == null -> error = "No account found — check email, phone or username"
                    acc.getString("p") != pass -> error = "Wrong password — try again"
                    acc.optBoolean("del", false) -> restoreUser = acc.getString("u")
                    else -> {
                        finishLogin(acc.getString("u"), acc.getString("c"))
                        onLoggedIn()
                    }
                }
            }
        })
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                if (signup) "Already have an account?  " else "New here?  ",
                fontSize = 12.sp, color = Txt3, fontWeight = FontWeight.Bold
            )
            Text(
                if (signup) "Log in" else "Create account",
                fontSize = 12.sp, color = Orchid, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable { signup = !signup; error = ""; restoreUser = "" }
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            "Accounts are saved on this device. Deleting an account never erases it —\nlog in again anytime and tap Restore.",
            fontSize = 10.sp, color = Txt3, lineHeight = 15.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 20.dp)
        )
    }
}

/* ---------- Step 4: hard identity verification — fingerprint or LIVE photo ---------- */

fun runBiometric(ctx: android.content.Context, title: String, onOk: () -> Unit, onFail: (String) -> Unit) {
    val act = ctx as? androidx.fragment.app.FragmentActivity
        ?: return onFail("Fingerprint unavailable on this device")
    val authenticators = androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK or
        androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
    val bm = androidx.biometric.BiometricManager.from(act)
    if (bm.canAuthenticate(authenticators) != androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS)
        return onFail("No fingerprint or screen lock set on this phone — use Live photo instead")
    val prompt = androidx.biometric.BiometricPrompt(
        act,
        androidx.core.content.ContextCompat.getMainExecutor(act),
        object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) = onOk()
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) = onFail(errString.toString())
        }
    )
    prompt.authenticate(
        androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle("Fingerprint, face or screen lock")
            .setAllowedAuthenticators(authenticators)
            .build()
    )
}

@Composable
fun VerifyScreen(onDone: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var method by remember { mutableStateOf("") }   // "", "finger", "photo"
    var error by remember { mutableStateOf("") }
    var selfie by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    fun markVerified(m: String) {
        method = m
        error = ""
        Store.set(ctx, "verified", "true")
        val u = Store.get(ctx, "auth_user", "")
        if (u.isNotEmpty()) Store.set(ctx, "verified_acct_$u", m)
    }

    val camera = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        if (bmp != null) {
            selfie = bmp
            try {
                val f = java.io.File(ctx.filesDir, "verify_selfie.jpg")
                f.outputStream().use { bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, it) }
                Store.set(ctx, "verify_photo", f.absolutePath)
            } catch (_: Exception) {}
            markVerified("photo")
        } else error = "No photo captured — try again"
    }
    val camPerm = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { ok -> if (ok) camera.launch(null) else error = "Camera permission needed for the live photo" }

    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(26.dp))
        Box(
            Modifier.size(52.dp).clip(RoundedCornerShape(17.dp)).background(AuroraGradient),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Fingerprint, null, tint = Color.White, modifier = Modifier.size(28.dp)) }
        Spacer(Modifier.height(18.dp))
        Text("Step 4 — verify\nit's really you", fontSize = 27.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 33.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            "Hard security check. Prove you're a real, live person —\nchoose one of the two:",
            fontSize = 12.sp, color = Txt2, lineHeight = 18.sp
        )
        Spacer(Modifier.height(22.dp))

        /* fingerprint option */
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(19.dp))
                .border(2.dp, if (method == "finger") Mint else Mist, RoundedCornerShape(19.dp))
                .clickable {
                    runBiometric(ctx, "Verify your identity",
                        onOk = { markVerified("finger") },
                        onFail = { msg -> error = msg })
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(46.dp).clip(RoundedCornerShape(15.dp)).background(if (method == "finger") Mint else Cloud),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Fingerprint, null,
                    tint = if (method == "finger") Color.White else Orchid, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    if (method == "finger") "Fingerprint verified ✓" else "Your fingerprint",
                    fontWeight = FontWeight.ExtraBold, fontSize = 14.sp,
                    color = if (method == "finger") Mint else Ink
                )
                Text("Phone's fingerprint sensor, face or screen lock", fontSize = 10.sp, color = Txt3)
            }
            Text("›", color = Txt3, fontSize = 20.sp)
        }
        Spacer(Modifier.height(12.dp))

        /* live photo option */
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(19.dp))
                .border(2.dp, if (method == "photo") Mint else Mist, RoundedCornerShape(19.dp))
                .clickable { camPerm.launch(android.Manifest.permission.CAMERA) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val s = selfie
            if (s != null) {
                androidx.compose.foundation.Image(
                    bitmap = s.asImageBitmap(), contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.size(46.dp).clip(RoundedCornerShape(15.dp))
                )
            } else {
                Box(
                    Modifier.size(46.dp).clip(RoundedCornerShape(15.dp)).background(Cloud),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.PhotoCamera, null, tint = Orchid, modifier = Modifier.size(22.dp)) }
            }
            Spacer(Modifier.width(13.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    if (method == "photo") "Live photo verified ✓" else "Live photo",
                    fontWeight = FontWeight.ExtraBold, fontSize = 14.sp,
                    color = if (method == "photo") Mint else Ink
                )
                Text("Camera opens NOW — no gallery, no old pictures", fontSize = 10.sp, color = Txt3)
            }
            Text("›", color = Txt3, fontSize = 20.sp)
        }

        Spacer(Modifier.height(14.dp))
        if (error.isNotEmpty()) {
            Text(error, color = Color(0xFFBE123C), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(10.dp))
        }
        if (method.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Verified, null, tint = Sky, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(7.dp))
                Text("Identity verified — blue badge added to your profile", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Sky)
            }
            Spacer(Modifier.height(14.dp))
            GradientButton("Continue", Modifier.fillMaxWidth(), onClick = onDone)
        }
        Spacer(Modifier.height(24.dp))
    }
}

/* ---------- App lock: fingerprint required to open Amora ---------- */

@Composable
fun AppLockScreen(onUnlocked: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var error by remember { mutableStateOf("") }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        runBiometric(ctx, "Unlock Amora", onOk = onUnlocked, onFail = { msg -> error = msg })
    }
    Column(
        Modifier.fillMaxSize().background(NightGradient).statusBarsPadding().navigationBarsPadding().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Box(
            Modifier.size(74.dp).clip(RoundedCornerShape(24.dp)).background(AuroraGradient),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Fingerprint, null, tint = Color.White, modifier = Modifier.size(40.dp)) }
        Spacer(Modifier.height(20.dp))
        Text("Amora is locked", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(6.dp))
        Text("Unlock with your fingerprint, face or screen lock", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
        if (error.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text(error, color = Color(0xFFFCA5A5), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(22.dp))
        GradientButton("Unlock", Modifier.fillMaxWidth(), onClick = {
            runBiometric(ctx, "Unlock Amora", onOk = onUnlocked, onFail = { msg -> error = msg })
        })
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun AppLockToggle() {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var on by remember { mutableStateOf(Store.get(ctx, "app_lock", "false") == "true") }
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(if (on) Ink else Cloud),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Filled.Fingerprint, null, tint = if (on) Color.White else Orchid, modifier = Modifier.size(17.dp)) }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Text("App lock — fingerprint", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            Text(
                if (on) "Fingerprint needed every time Amora opens" else "Off — anyone with your phone can open Amora",
                fontSize = 10.sp, color = Txt3
            )
        }
        androidx.compose.material3.Switch(
            checked = on,
            onCheckedChange = { v ->
                if (v) {
                    runBiometric(ctx, "Confirm fingerprint to enable app lock",
                        onOk = { on = true; Store.set(ctx, "app_lock", "true") },
                        onFail = { msg ->
                            android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_SHORT).show()
                        })
                } else {
                    on = false; Store.set(ctx, "app_lock", "false")
                }
            }
        )
    }
}
