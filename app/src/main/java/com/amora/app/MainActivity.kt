package com.amora.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmoraTheme {
                AmoraApp()
            }
        }
    }
}

@Composable
fun AmoraApp() {
    val nav = rememberNavController()
    val go: (String) -> Unit = { route -> nav.navigate(route) }
    val back: () -> Unit = { nav.popBackStack() }
    NavHost(navController = nav, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(
                onCreate = { nav.navigate("signup") },
                onLogin = { nav.navigate("main") { popUpTo("onboarding") { inclusive = true } } }
            )
        }
        composable("signup") { SignupScreen(onNext = { nav.navigate("otp") }, onBack = back) }
        composable("otp") {
            OtpScreen(
                onNext = { nav.navigate("main") { popUpTo("onboarding") { inclusive = true } } },
                onBack = back
            )
        }
        composable("main") { MainScreen(go = go) }
        composable("chat/{id}") { backStack ->
            val id = backStack.arguments?.getString("id") ?: "1"
            ConversationScreen(chatId = id, onBack = back, go = go)
        }
        composable("match") {
            MatchScreen(onHello = { nav.navigate("chat/1") { popUpTo("main") } }, onBack = back)
        }
        composable("story") { StoryScreen(onBack = back) }
        composable("reels") { ReelsScreen(onBack = back) }
        composable("call") { CallScreen(onBack = back) }
        composable("notifications") { NotificationsScreen(onBack = back) }
        composable("search") { SearchScreen(onBack = back) }
        composable("comments") { CommentsScreen(onBack = back) }
        composable("settings") { SettingsScreen(onBack = back, onSafety = { nav.navigate("safety") }) }
        composable("safety") { SafetyScreen(onBack = back) }
        composable("wallet") { WalletScreen(onBack = back) }
        composable("shop") { ShopScreen(onBack = back) }
        composable("calendar") { CalendarScreen(onBack = back) }
        composable("board") { BoardScreen(onBack = back) }
        composable("upload") { UploadScreen(onBack = back) }
    }
}

private data class Tab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun MainScreen(go: (String) -> Unit) {
    var selected by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        Tab("Home", Icons.Filled.Home),
        Tab("Discover", Icons.Filled.Favorite),
        Tab("Chats", Icons.Filled.Email),
        Tab("Spaces", Icons.Filled.Menu),
        Tab("Profile", Icons.Filled.Person)
    )
    Scaffold(
        containerColor = Cloud,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                tabs.forEachIndexed { i, tab ->
                    NavigationBarItem(
                        selected = selected == i,
                        onClick = { selected = i },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Orchid,
                            selectedTextColor = Orchid,
                            indicatorColor = Color(0xFFF6E7FF),
                            unselectedIconColor = Txt3,
                            unselectedTextColor = Txt3
                        )
                    )
                }
            }
        }
    ) { pad ->
        Box(Modifier.padding(pad)) {
            when (selected) {
                0 -> FeedScreen(go = go)
                1 -> DiscoverScreen(go = go)
                2 -> ChatsScreen(onOpenChat = { id -> go("chat/$id") })
                3 -> SpacesScreen(go = go)
                else -> ProfileScreen(go = go)
            }
        }
    }
}
