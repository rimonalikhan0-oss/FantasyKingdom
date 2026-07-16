package com.fantasykingdom.app.ui.rides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fantasykingdom.app.data.model.Ride
import com.fantasykingdom.app.data.model.RideCategory
import com.fantasykingdom.app.ui.components.FKChip
import com.fantasykingdom.app.ui.components.FKContentCard
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKTopBar

/** Feature #5 — browse all rides, filterable by category (Thrill / Family / Kids / Water / Dark). */
@Composable
fun RideCategoriesScreen(onRideClick: (String) -> Unit) {
    val viewModel: RidesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { FKTopBar("Rides & Attractions") }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            CategoryFilterRow(
                selected = uiState.selectedCategory,
                onSelect = viewModel::selectCategory
            )
            if (uiState.isLoading) {
                FKLoadingIndicator()
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredRides) { ride ->
                        RideGridCard(ride, onClick = { onRideClick(ride.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(selected: RideCategory?, onSelect: (RideCategory?) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listOf(null) + RideCategory.entries) { category ->
            FilterChip(
                selected = selected == category,
                onClick = { onSelect(category) },
                label = { Text(category?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "All") }
            )
        }
    }
}

@Composable
private fun RideGridCard(ride: Ride, onClick: () -> Unit) {
    FKContentCard(onClick = onClick) {
        Column {
            AsyncImage(
                model = ride.imageUrl,
                contentDescription = ride.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.3f)
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(ride.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(
                    "${ride.zone} · Thrill ${ride.thrillRating}/5",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(modifier = Modifier.padding(top = 6.dp)) {
                    FKChip(
                        text = if (ride.isOpen) "${ride.waitTimeMinutes} min wait" else "Closed",
                        containerColor = if (ride.isOpen) Color(0xFFDFF5EC) else Color(0xFFFBE2E2),
                        contentColor = if (ride.isOpen) Color(0xFF1B7A57) else Color(0xFFA13A3A)
                    )
                }
            }
        }
    }
}
