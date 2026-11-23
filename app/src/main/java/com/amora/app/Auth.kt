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
