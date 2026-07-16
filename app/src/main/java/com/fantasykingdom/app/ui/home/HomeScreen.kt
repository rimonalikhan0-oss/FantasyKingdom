package com.fantasykingdom.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fantasykingdom.app.data.model.EventOffer
import com.fantasykingdom.app.data.model.Ride
import com.fantasykingdom.app.navigation.Destinations
import com.fantasykingdom.app.ui.components.FKChip
import com.fantasykingdom.app.ui.components.FKContentCard
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKSectionHeader

/** Feature #4 — Home Dashboard: greets the user and surfaces rides, offers and quick actions. */
@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        HomeTopBar(userName = uiState.user?.fullName)

        if (uiState.isLoading) {
            FKLoadingIndicator()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item { QuickActionsRow(onNavigate) }
                item { FKSectionHeader("Popular Rides", "See all") { onNavigate(Destinations.RIDE_CATEGORIES) } }
                item { FeaturedRidesRow(uiState.featuredRides) { rideId -> onNavigate(Destinations.rideDetail(rideId)) } }
                item { FKSectionHeader("Events & Offers", "See all") { onNavigate(Destinations.EVENTS_OFFERS) } }
                item { OffersRow(uiState.currentOffers) }
            }
        }
    }
}

@Composable
private fun HomeTopBar(userName: String?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Welcome back,", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                userName?.ifBlank { "Explorer" } ?: "Explorer",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        IconButton(onClick = { /* opens notification center */ }) {
            Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
        }
    }
}

@Composable
private fun QuickActionsRow(onNavigate: (String) -> Unit) {
    val actions = listOf(
        Triple("Park Info", Icons.Filled.Info, Destinations.PARK_INFO),
        Triple("Food", Icons.Filled.Fastfood, Destinations.FOOD_LIST),
        Triple("Gallery", Icons.Filled.PhotoLibrary, Destinations.GALLERY),
        Triple("Events", Icons.Filled.CalendarMonth, Destinations.EVENTS_OFFERS),
        Triple("Offers", Icons.Filled.LocalOffer, Destinations.EVENTS_OFFERS),
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(actions) { (label, icon, route) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable { onNavigate(route) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                Text(label, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun FeaturedRidesRow(rides: List<Ride>, onRideClick: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(rides) { ride ->
            FKContentCard(modifier = Modifier.width(220.dp), onClick = { onRideClick(ride.id) }) {
                Column {
                    AsyncImage(
                        model = ride.imageUrl,
                        contentDescription = ride.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(16f / 10f)
                    )
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(ride.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        Row(modifier = Modifier.padding(top = 6.dp)) {
                            FKChip(
                                text = if (ride.isOpen) "Open · ${ride.waitTimeMinutes} min" else "Closed",
                                containerColor = if (ride.isOpen) Color(0xFFDFF5EC) else Color(0xFFFBE2E2),
                                contentColor = if (ride.isOpen) Color(0xFF1B7A57) else Color(0xFFA13A3A)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OffersRow(offers: List<EventOffer>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(offers) { offer ->
            FKContentCard(modifier = Modifier.width(260.dp)) {
                Box {
                    AsyncImage(
                        model = offer.imageUrl,
                        contentDescription = offer.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(120.dp)
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.45f))
                            .padding(10.dp)
                    ) {
                        Text(offer.title, color = Color.White, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        if (offer.discountPercent > 0) {
                            Text("${offer.discountPercent}% OFF", color = Color(0xFFF4B740), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}


