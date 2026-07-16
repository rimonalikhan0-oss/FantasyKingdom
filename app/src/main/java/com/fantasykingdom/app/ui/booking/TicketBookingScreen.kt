package com.fantasykingdom.app.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fantasykingdom.app.data.model.TicketType
import com.fantasykingdom.app.ui.components.FKContentCard
import com.fantasykingdom.app.ui.components.FKPrimaryButton
import com.fantasykingdom.app.ui.components.FKTopBar

/** Feature #6 — Ticket Booking screen: pick a pass, set guest count, review price, and book. */
@Composable
fun TicketBookingScreen(onBookingComplete: (String) -> Unit) {
    val viewModel: BookingViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.bookedTicketId) {
        uiState.bookedTicketId?.let { onBookingComplete(it) }
    }

    Scaffold(topBar = { FKTopBar("Book Tickets") }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Choose your pass", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Column(modifier = Modifier.padding(top = 12.dp, bottom = 20.dp)) {
                TicketType.entries.forEach { type ->
                    TicketOptionRow(
                        type = type,
                        selected = uiState.ticketType == type,
                        onSelect = { viewModel.selectTicketType(type) }
                    )
                }
            }

            Text("Guests", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { viewModel.setGuestCount(uiState.guestCount - 1) }) {
                    Icon(Icons.Filled.Remove, contentDescription = "Decrease guests")
                }
                Text("${uiState.guestCount}", style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = { viewModel.setGuestCount(uiState.guestCount + 1) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Increase guests")
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "$${"%.2f".format(uiState.totalPrice)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 12.dp))
            }

            FKPrimaryButton(text = "Confirm Booking", isLoading = uiState.isBooking) {
                viewModel.bookTicket()
            }
        }
    }
}

@Composable
private fun TicketOptionRow(type: TicketType, selected: Boolean, onSelect: () -> Unit) {
    val (label, price, description) = when (type) {
        TicketType.ONE_DAY -> Triple("One-Day Pass", "$79", "Full park access for a single day")
        TicketType.TWO_DAY -> Triple("Two-Day Pass", "$139", "Two consecutive days of access")
        TicketType.ANNUAL_PASS -> Triple("Annual Pass", "$299", "Unlimited visits for a full year")
        TicketType.VIP_EXPRESS -> Triple("VIP Express", "$189", "One-day pass + priority ride access")
    }
    FKContentCard(onClick = onSelect, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selected, onClick = onSelect)
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(price, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}
