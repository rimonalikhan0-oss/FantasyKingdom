package com.fantasykingdom.app.ui.rides

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.Ride
import com.fantasykingdom.app.data.model.RideCategory
import com.fantasykingdom.app.data.repository.ParkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RidesUiState(
    val isLoading: Boolean = true,
    val allRides: List<Ride> = emptyList(),
    val selectedCategory: RideCategory? = null
) {
    val filteredRides: List<Ride>
        get() = selectedCategory?.let { cat -> allRides.filter { it.category == cat } } ?: allRides
}

/** Feature #5 — Ride Categories: browse/filter all park rides by category. */
@HiltViewModel
class RidesViewModel @Inject constructor(
    private val parkRepository: ParkRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RidesUiState())
    val uiState: StateFlow<RidesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val rides = parkRepository.getRides()
            _uiState.value = _uiState.value.copy(isLoading = false, allRides = rides)
        }
    }

    fun selectCategory(category: RideCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }
}
