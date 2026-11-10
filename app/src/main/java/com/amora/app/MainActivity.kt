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
    NavHost(navController = nav, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(onDone = {
                nav.navigate("main") { popUpTo("onboarding") { inclusive = true } }
            })
        }
        composable("main") {
            MainScreen(onOpenChat = { id -> nav.navigate("chat/$id") })
        }
        composable("chat/{id}") { backStack ->
            val id = backStack.arguments?.getString("id") ?: "1"
            ConversationScreen(chatId = id, onBack = { nav.popBackStack() })
        }
    }
}

private data class Tab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun MainScreen(onOpenChat: (String) -> Unit) {
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
                0 -> FeedScreen()
                1 -> DiscoverScreen()
                2 -> ChatsScreen(onOpenChat = onOpenChat)
                3 -> SpacesScreen()
                else -> ProfileScreen()
            }
        }
    }
}
