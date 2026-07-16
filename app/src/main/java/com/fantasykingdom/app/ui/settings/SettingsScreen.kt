package com.fantasykingdom.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fantasykingdom.app.ui.components.FKTopBar
import com.fantasykingdom.app.ui.theme.LocalThemeState

/** Feature #14 — Settings: Dark/Light mode toggle (persisted via DataStore) plus app info. */
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val themeViewModel = LocalThemeState.current
    val themeState by themeViewModel.themeState.collectAsState()

    Scaffold(topBar = { FKTopBar("Settings", onBack) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            Text("Appearance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            SettingsToggleRow(
                title = "Use system default",
                subtitle = "Match your device's light/dark setting",
                checked = themeState.useSystemDefault,
                onCheckedChange = { themeViewModel.setUseSystemDefault(it) }
            )
            SettingsToggleRow(
                title = "Dark mode",
                subtitle = "Override with a manual dark theme",
                checked = themeState.isDarkMode,
                enabled = !themeState.useSystemDefault,
                onCheckedChange = { themeViewModel.setDarkMode(it) }
            )

            Text(
                "About",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                "Fantasy Kingdom v1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
    }
}
