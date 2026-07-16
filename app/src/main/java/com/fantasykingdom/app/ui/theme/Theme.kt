package com.fantasykingdom.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = RoyalPurple,
    onPrimary = CloudWhite,
    primaryContainer = RoyalPurpleLight,
    onPrimaryContainer = DeepNight,
    secondary = MagicGold,
    onSecondary = DeepNight,
    secondaryContainer = SoftGray,
    tertiary = EmberPink,
    background = CloudWhite,
    onBackground = DeepNight,
    surface = CloudWhite,
    onSurface = DeepNight,
    surfaceVariant = SoftGray,
    error = ErrorRed,
)

private val DarkColors = darkColorScheme(
    primary = RoyalPurpleLight,
    onPrimary = DeepNight,
    primaryContainer = RoyalPurpleDark,
    onPrimaryContainer = CloudWhite,
    secondary = MagicGold,
    onSecondary = DeepNight,
    secondaryContainer = DarkSurfaceVariant,
    tertiary = EmberPink,
    background = DeepNight,
    onBackground = CloudWhite,
    surface = DarkSurface,
    onSurface = CloudWhite,
    surfaceVariant = DarkSurfaceVariant,
    error = ErrorRed,
)

/**
 * Feature #14 (Settings — Dark/Light Mode) root theme composable.
 *
 * @param darkTheme current preference, driven by [ThemeViewModel] and
 *   persisted via DataStore so it survives process death.
 * @param dynamicColor opts into Material You wallpaper-based color on
 *   Android 12+ when the user hasn't set an explicit brand-theme override.
 */
@Composable
fun FantasyKingdomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FantasyKingdomTypography,
        content = content
    )
}

/** CompositionLocal so any screen can read/toggle the theme without threading a ViewModel down. */
val LocalThemeState = staticCompositionLocalOf<ThemeViewModel> {
    error("No ThemeViewModel provided — wrap content in CompositionLocalProvider(LocalThemeState provides ...)")
}
