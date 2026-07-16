package com.fantasykingdom.app.ui.rides

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKTopBar

/** Ride detail — reached from feature #5 grid or the Home dashboard's featured rides row. */
@Composable
fun RideDetailScreen(rideId: String, onBack: () -> Unit) {
    val viewModel: RideDetailViewModel = hiltViewModel()
    val ride by viewModel.ride.collectAsState()

    LaunchedEffect(rideId) { viewModel.load(rideId) }

    Scaffold(topBar = { FKTopBar(ride?.name ?: "Ride Details", onBack) }) { padding ->
        val currentRide = ride
        if (currentRide == null) {
            FKLoadingIndicator(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = currentRide.imageUrl,
                    contentDescription = currentRide.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
                )
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(currentRide.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(
                        currentRide.zone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        StatBlock(Icons.Filled.Timer, "${currentRide.waitTimeMinutes} min", "Wait time")
                        StatBlock(Icons.Filled.Height, "${currentRide.heightRequirementCm} cm", "Min. height")
                        StatBlock(Icons.Filled.Star, "${currentRide.thrillRating}/5", "Thrill level")
                    }

                    Text(
                        "About this ride",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    )
                    Text(currentRide.description, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.StatBlock(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
