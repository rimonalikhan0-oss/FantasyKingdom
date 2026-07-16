package com.fantasykingdom.app.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fantasykingdom.app.navigation.Destinations
import kotlinx.coroutines.delay

/**
 * Feature #1 — Splash Screen. Shows an animated brand mark while
 * [SplashViewModel] checks Firebase Auth state, then routes to Login or
 * straight to Home for an already-signed-in user.
 */
@Composable
fun SplashScreen(onFinished: (String) -> Unit) {
    val viewModel: SplashViewModel = hiltViewModel()
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
        )
        delay(600)
        val destination = if (viewModel.isUserLoggedIn()) Destinations.HOME else Destinations.LOGIN
        onFinished(destination)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush()),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Castle,
                contentDescription = "Fantasy Kingdom logo",
                tint = Color(0xFFF4B740),
                modifier = Modifier
                    .size(96.dp)
                    .scale(scale.value)
            )
            androidx.compose.foundation.layout.Spacer(Modifier.padding(8.dp))
            Text(
                text = "Fantasy Kingdom",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(scale.value)
            )
            Text(
                text = "Where Magic Comes Alive",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp).scale(scale.value)
            )
        }
    }
}

private fun gradientBrush() = androidx.compose.ui.graphics.Brush.verticalGradient(
    colors = listOf(Color(0xFF4A2574), Color(0xFF6A3EA1))
)
