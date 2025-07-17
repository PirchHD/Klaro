package com.example.klaro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary            = Blue500,
    onPrimary          = White,
    primaryContainer   = Blue200,
    onPrimaryContainer = Neutral900,

    secondary          = Blue200,
    onSecondary        = Neutral900,

    background         = White,
    onBackground       = Neutral900,

    surface            = White,
    onSurface          = Neutral900,

    error              = RedError,
    onError            = White,

    tertiary           = Blue700,
    onTertiary         = White
)



private val DarkColors = darkColorScheme(
    primary            = Blue200,
    onPrimary          = Neutral900,
    primaryContainer   = Blue500,
    onPrimaryContainer = White,

    secondary          = Blue500,
    onSecondary        = White,

    background         = Neutral900,
    onBackground       = White,

    surface            = Neutral900,
    onSurface          = White,

    error              = RedError,
    onError            = White
)

@Composable
fun KlaroTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography  = Typography(),
        shapes      = Shapes(),
        content     = content
    )
}
