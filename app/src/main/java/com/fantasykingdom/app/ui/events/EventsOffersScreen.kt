package com.fantasykingdom.app.ui.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.fantasykingdom.app.data.model.EventOffer
import com.fantasykingdom.app.data.model.PromoKind
import com.fantasykingdom.app.ui.components.FKChip
import com.fantasykingdom.app.ui.components.FKContentCard
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKTopBar

/** Feature #10 — Events & Offers: seasonal events and limited-time ticket/food discounts. */
@Composable
fun EventsOffersScreen(onBack: () -> Unit) {
    val viewModel: EventsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { FKTopBar("Events & Offers", onBack) }) { padding ->
        if (uiState.isLoading) {
            FKLoadingIndicator(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.items) { item ->
                    EventOfferCard(item, modifier = Modifier.padding(bottom = 14.dp))
                }
            }
        }
    }
}

@Composable
private fun EventOfferCard(item: EventOffer, modifier: Modifier = Modifier) {
    FKContentCard(modifier = modifier.fillMaxWidth()) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)
            )
            Column(modifier = Modifier.padding(14.dp)) {
                FKChip(
                    text = if (item.kind == PromoKind.EVENT) "EVENT" else "OFFER",
                    containerColor = if (item.kind == PromoKind.EVENT) Color(0xFFE9E1FB) else Color(0xFFFDEBD3),
                    contentColor = if (item.kind == PromoKind.EVENT) Color(0xFF5B3EA6) else Color(0xFFA1671C)
                )
                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
                Text(item.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                if (item.discountPercent > 0) {
                    Text(
                        "${item.discountPercent}% OFF",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
