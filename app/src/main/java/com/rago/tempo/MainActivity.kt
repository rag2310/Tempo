package com.rago.tempo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.rago.tempo.domain.usecase.GetSessionUseCase
import com.rago.tempo.presentation.home.HomeScreen
import com.rago.tempo.presentation.login.LoginScreen
import com.rago.tempo.presentation.register.RegisterScreen
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
    data object Register : Route

    @Serializable
    data object Home : Route
}

@Composable
fun MainApp(
    getSessionUseCase: GetSessionUseCase = hiltViewModel<MainViewModel>().getSessionUseCase
) {
    val backStack = remember { mutableStateListOf<Route>() }
    var isLoadingSession by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        getSessionUseCase().collect { user ->
            if (isLoadingSession) {
                if (user != null) {
                    backStack.add(Route.Home)
                } else {
                    backStack.add(Route.Login)
                }
                isLoadingSession = false
            }
        }
    }

    if (isLoadingSession) {
        // Loading screen or empty box
        return
    }

    Scaffold(Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
        ) { route ->
            when (route) {
                Route.Login -> NavEntry(route) {
                    LoginScreen(
                        onLoginSuccess = {
                            backStack.clear()
                            backStack.add(Route.Home)
                        },
                        onNavigateToRegister = {
                            backStack.add(Route.Register)
                        }
                    )
                }

                Route.Register -> NavEntry(route) {
                    RegisterScreen(
                        onRegisterSuccess = {
                            backStack.clear()
                            backStack.add(Route.Home)
                        }
                    )
                }

                Route.Home -> NavEntry(route) {
                    HomeScreen(
                        onLogoutSuccess = {
                            backStack.clear()
                            backStack.add(Route.Login)
                        }
                    )
                }
            }
        }
    }
}
