package com.fantasykingdom.app.ui.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fantasykingdom.app.data.model.Restaurant
import com.fantasykingdom.app.ui.components.FKContentCard
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKTopBar

/** Feature #8 — Food & Restaurant List: browse dining options across the park. */
@Composable
fun FoodListScreen(onBack: () -> Unit) {
    val viewModel: FoodViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { FKTopBar("Food & Dining", onBack) }) { padding ->
        if (uiState.isLoading) {
            FKLoadingIndicator(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.restaurants) { restaurant ->
                    RestaurantRow(restaurant, modifier = Modifier.padding(bottom = 12.dp))
                }
            }
        }
    }
}

@Composable
private fun RestaurantRow(restaurant: Restaurant, modifier: Modifier = Modifier) {
    FKContentCard(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(84.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(restaurant.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "${restaurant.cuisine} · ${"$".repeat(restaurant.priceLevel)} · ${restaurant.zone}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = androidx.compose.ui.graphics.Color(0xFFF4B740), modifier = Modifier.size(16.dp))
                    Text(" ${restaurant.rating}", style = MaterialTheme.typography.bodySmall)
                }
                if (restaurant.menuHighlights.isNotEmpty()) {
                    Text(
                        restaurant.menuHighlights.joinToString(" · "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
