package com.example.learneverythingbot.presentation.screen.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = TextPrimary,

    secondary = Secondary,
    onSecondary = TextOnSecondary,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = TextOnPrimary,

    tertiary = Success,
    onTertiary = TextOnPrimary,

    background = BackgroundLight,
    onBackground = TextPrimary,

    surface = SurfaceLight,
    surfaceVariant = Warning,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,

    error = Error,
    onError = Color.White,

    outline = BorderLight
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = TextOnPrimary,

    secondary = Secondary,
    onSecondary = TextOnSecondary,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = TextOnPrimary,

    background = BackgroundDark,
    onBackground = Color.White,

    surface = SurfaceDark,
    onSurface = Color.White,

    error = Error,
    onError = Color.White,

    outline = BorderDark
)

@Composable
fun LearnEverythingBotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    keepBrandOnDynamic: Boolean = true,
    content: @Composable () -> Unit
) {
    val ctx = LocalContext.current
    val scheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val dyn = if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
            if (keepBrandOnDynamic) {
                dyn.copy(
                    primary = Primary,
                    onPrimary = TextOnPrimary,
                    secondary = Secondary,
                    onSecondary = TextOnSecondary
                )
            } else dyn
        } else {
            if (darkTheme) DarkColors else LightColors
        }

    MaterialTheme(
        colorScheme = scheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
