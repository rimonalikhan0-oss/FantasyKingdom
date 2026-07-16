package com.fantasykingdom.app.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.EventOffer
import com.fantasykingdom.app.data.repository.ParkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EventsUiState(val isLoading: Boolean = true, val items: List<EventOffer> = emptyList())

/** Feature #10 — Events & Offers. */
@HiltViewModel
class EventsViewModel @Inject constructor(
    private val parkRepository: ParkRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val items = parkRepository.getEventsAndOffers()
            _uiState.value = EventsUiState(isLoading = false, items = items)
        }
    }
}
