package com.fantasykingdom.app.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fantasykingdom.app.data.model.ParkZone
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKTopBar

/**
 * Feature #9 — Park Map. Renders labeled pins for each [ParkZone] positioned
 * proportionally (xPercent/yPercent, 0..1) over the park map background, so
 * it scales correctly to any screen size. Tapping a pin surfaces its info
 * card. Swap the background Box color for an AsyncImage(mapImageUrl) once a
 * real park map asset is uploaded to Firebase Storage.
 */
@Composable
fun ParkMapScreen() {
    val viewModel: MapViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { FKTopBar("Park Map") }) { padding ->
        if (uiState.isLoading) {
            FKLoadingIndicator(modifier = Modifier.padding(padding))
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    val widthPx = maxWidth
                    val heightPx = maxHeight
                    uiState.zones.forEach { zone ->
                        Box(
                            modifier = Modifier
                                .offset(x = widthPx * zone.xPercent - 16.dp, y = heightPx * zone.yPercent - 16.dp)
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .clickable { viewModel.selectZone(zone) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Place, contentDescription = zone.name, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
                uiState.selectedZone?.let { zone -> ZoneInfoCard(zone) }
            }
        }
    }
}

@Composable
private fun ZoneInfoCard(zone: ParkZone) {
    Card(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(zone.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(zone.description, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

