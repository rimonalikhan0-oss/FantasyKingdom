package com.fantasykingdom.app.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.ParkZone
import com.fantasykingdom.app.data.repository.ParkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val isLoading: Boolean = true,
    val zones: List<ParkZone> = emptyList(),
    val selectedZone: ParkZone? = null
)

/** Feature #9 — Park Map: interactive zone pins over a stylized map image. */
@HiltViewModel
class MapViewModel @Inject constructor(
    private val parkRepository: ParkRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val zones = parkRepository.getParkZones()
            _uiState.value = MapUiState(isLoading = false, zones = zones)
        }
    }

    fun selectZone(zone: ParkZone?) {
        _uiState.value = _uiState.value.copy(selectedZone = zone)
    }
}
