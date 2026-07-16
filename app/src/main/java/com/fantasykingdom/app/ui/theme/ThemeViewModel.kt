package com.fantasykingdom.app.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.remote.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ThemeState(val isDarkMode: Boolean = false, val useSystemDefault: Boolean = true)

private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")
private val USE_SYSTEM_KEY = booleanPreferencesKey("use_system_default")

/**
 * Backs the Dark/Light mode switch in Settings (feature #14). Preference is
 * persisted with Jetpack DataStore so it's remembered across app restarts.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val store = appContext.dataStore

    val themeState: StateFlow<ThemeState> = store.data
        .map { prefs ->
            ThemeState(
                isDarkMode = prefs[DARK_MODE_KEY] ?: false,
                useSystemDefault = prefs[USE_SYSTEM_KEY] ?: true
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeState())

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            store.edit {
                it[DARK_MODE_KEY] = enabled
                it[USE_SYSTEM_KEY] = false
            }
        }
    }

    fun useSystemDefault() {
        viewModelScope.launch {
            store.edit { it[USE_SYSTEM_KEY] = true }
        }
    }

    fun setUseSystemDefault(enabled: Boolean) {
        viewModelScope.launch {
            store.edit { it[USE_SYSTEM_KEY] = enabled }
        }
    }
}
