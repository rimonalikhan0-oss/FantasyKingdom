package com.fantasykingdom.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.fantasykingdom.app.navigation.FantasyKingdomNavGraph
import com.fantasykingdom.app.ui.theme.FantasyKingdomTheme
import com.fantasykingdom.app.ui.theme.LocalThemeState
import com.fantasykingdom.app.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity host. All screens (feature #1 through #14) live inside one
 * Compose navigation graph hosted by this Activity, per modern Android
 * "single activity" architecture guidance.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be called before super.onCreate() and before setContent.
        // Keeps the Android 12+ system splash (feature #1) showing until the
        // app's own splash screen animation has run.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themeState by themeViewModel.themeState.collectAsState()
            val effectiveDarkTheme = if (themeState.useSystemDefault) isSystemInDarkTheme() else themeState.isDarkMode

            FantasyKingdomTheme(darkTheme = effectiveDarkTheme) {
                CompositionLocalProvider(LocalThemeState provides themeViewModel) {
                    FantasyKingdomNavGraph(
                        onSplashFinished = { keepSplashOnScreen = false }
                    )
                }
            }
        }
    }
}
