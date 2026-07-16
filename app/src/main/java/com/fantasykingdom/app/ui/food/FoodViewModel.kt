package com.fantasykingdom.app.ui.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.Restaurant
import com.fantasykingdom.app.data.repository.ParkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FoodUiState(val isLoading: Boolean = true, val restaurants: List<Restaurant> = emptyList())

/** Feature #8 — Food & Restaurant List. */
@HiltViewModel
class FoodViewModel @Inject constructor(
    private val parkRepository: ParkRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val restaurants = parkRepository.getRestaurants()
            _uiState.value = FoodUiState(isLoading = false, restaurants = restaurants)
        }
    }
}
