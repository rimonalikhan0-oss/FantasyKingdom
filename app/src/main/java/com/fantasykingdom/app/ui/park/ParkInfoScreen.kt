package com.fantasykingdom.app.ui.park

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fantasykingdom.app.ui.components.FKContentCard
import com.fantasykingdom.app.ui.components.FKTopBar

private data class InfoRow(val icon: ImageVector, val title: String, val detail: String)

/** Feature #3 — Park Information: hours, rules, contact and general visitor info. */
@Composable
fun ParkInfoScreen(onBack: () -> Unit) {
    val infoRows = listOf(
        InfoRow(Icons.Filled.AccessTime, "Opening Hours", "9:00 AM – 9:00 PM, daily"),
        InfoRow(Icons.Filled.Place, "Address", "1 Fantasy Way, Kingdom City"),
        InfoRow(Icons.Filled.Phone, "Guest Services", "+1 (800) 555-0199"),
        InfoRow(Icons.Filled.LocalParking, "Parking", "General lot $25/day · Premium lot $40/day"),
        InfoRow(Icons.Filled.Info, "Park Rules", "Height and safety rules apply per ride. Outside food is allowed in picnic zones only."),
    )

    Scaffold(topBar = { FKTopBar("Park Information", onBack) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(infoRows) { row ->
                FKContentCard {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(row.icon, contentDescription = row.title, tint = MaterialTheme.colorScheme.primary)
                        Column(modifier = Modifier.padding(start = 14.dp)) {
                            Text(row.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            Text(row.detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
