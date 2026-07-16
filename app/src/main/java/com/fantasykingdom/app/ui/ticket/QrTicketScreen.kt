package com.fantasykingdom.app.ui.ticket

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fantasykingdom.app.ui.components.FKContentCard
import com.fantasykingdom.app.ui.components.FKLoadingIndicator
import com.fantasykingdom.app.ui.components.FKTopBar
import com.fantasykingdom.app.util.QrCodeGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Feature #7 — QR Code Ticket: renders the purchased ticket as a scannable
 * QR code (generated locally from [Ticket.qrPayload] via ZXing) that guests
 * present at the gate.
 */
@Composable
fun QrTicketScreen(ticketId: String, onBack: () -> Unit) {
    val viewModel: TicketDetailViewModel = hiltViewModel()
    val ticket by viewModel.ticket.collectAsState()

    LaunchedEffect(ticketId) { viewModel.load(ticketId) }

    Scaffold(topBar = { FKTopBar("Your Ticket", onBack) }) { padding ->
        val currentTicket = ticket
        if (currentTicket == null) {
            FKLoadingIndicator(modifier = Modifier.padding(padding))
        } else {
            val qrBitmap by produceState<android.graphics.Bitmap?>(initialValue = null, currentTicket.qrPayload) {
                value = QrCodeGenerator.generate(currentTicket.qrPayload)
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Present this code at the entrance gate",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                FKContentCard {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        qrBitmap?.let { bmp ->
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "Ticket QR code",
                                modifier = Modifier
                                    .size(220.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                            )
                        } ?: FKLoadingIndicator()

                        Text(
                            currentTicket.type.name.replace("_", " "),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                        Text(
                            "Visit date: ${formatDate(currentTicket.visitDate)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "${currentTicket.guestCount} guest(s) · $${"%.2f".format(currentTicket.totalPrice)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Text(
                    "Ticket ID: ${currentTicket.id}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(millis))
