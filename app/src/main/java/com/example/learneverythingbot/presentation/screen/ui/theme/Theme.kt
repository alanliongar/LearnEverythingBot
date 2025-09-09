package com.example.learneverythingbot.presentation.screen.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val P get() = BrandColors

private val LightColors = lightColorScheme(
    primary = P.Primary,
    onPrimary = P.TextOnPrimary,
    primaryContainer = P.PrimaryLight,
    onPrimaryContainer = P.TextPrimary,

    secondary = P.Secondary,
    onSecondary = P.TextOnSecondary,
    secondaryContainer = P.SecondaryLight,
    onSecondaryContainer = P.TextOnPrimary,

    tertiary = P.Success,
    onTertiary = P.TextOnPrimary,


    background = P.BackgroundLight,
    onBackground = P.TextPrimary,

    surface = P.SurfaceLight,
    surfaceVariant = P.Warning,
    onSurface = P.TextPrimary,
    onSurfaceVariant = P.TextSecondary,

    error = P.Error,
    onError = Color.White,

    outline = P.BorderLight
)

private val DarkColors = darkColorScheme(
    primary = P.Primary,
    onPrimary = P.TextOnPrimary,
    primaryContainer = P.PrimaryDark,
    onPrimaryContainer = P.TextOnPrimary,

    secondary = P.Secondary,
    onSecondary = P.TextOnSecondary,
    secondaryContainer = P.SecondaryDark,
    onSecondaryContainer = P.TextOnPrimary,


    /*tertiary = P.Success,
    onTertiary = P.TextOnPrimary,*/

    /*surfaceVariant = P.Warning,
    onSurfaceVariant = P.TextSecondary,*/


    background = P.BackgroundDark,
    onBackground = Color.White,

    surface = P.SurfaceDark,
    onSurface = Color.White,

    error = P.Error,
    onError = Color.White,

    outline = P.BorderDark
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
                    primary = P.Primary,
                    onPrimary = P.TextOnPrimary,
                    secondary = P.Secondary,
                    onSecondary = P.TextOnSecondary
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
