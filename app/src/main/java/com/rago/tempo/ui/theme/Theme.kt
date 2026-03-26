package com.rago.tempo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// --- MODO OSCURO (Dark) ---
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFEBC248),
    onPrimary = Color(0xFF3C2F00),
    secondary = Color(0xFFD1C5B4),
    onSecondary = Color(0xFF373024),
    tertiary = Color(0xFFE5BFA9),
    onTertiary = Color(0xFF432B1C),
    surface = Color(0xFF17130B),
    onSurface = Color(0xFFE9E1D9)
)

// --- MODO CLARO (Light) ---
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF765B00),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF695E4E),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF7E523A),
    onTertiary = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1E1B16)
)

@Composable
fun TempoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}