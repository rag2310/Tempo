package com.rago.tempo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.rago.tempo.presentation.home.HomeScreen
import com.rago.tempo.presentation.login.LoginScreen
import com.rago.tempo.ui.theme.TempoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TempoTheme {
                MainApp()
            }
        }
    }
}

@Serializable
sealed interface Route {
    @Serializable
    data object Login : Route
    @Serializable
    data object Home : Route
}

@Composable
fun MainApp() {
    val backStack = remember { mutableStateListOf<Route>(Route.Login) }

    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
    ) { route ->
        when (route) {
            Route.Login -> NavEntry(route) {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(Route.Home)
                    }
                )
            }
            Route.Home -> NavEntry(route) {
                HomeScreen()
            }
        }
    }
}
