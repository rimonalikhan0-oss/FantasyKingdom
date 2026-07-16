package com.fantasykingdom.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.EventOffer
import com.fantasykingdom.app.data.model.Ride
import com.fantasykingdom.app.data.model.User
import com.fantasykingdom.app.data.repository.AuthRepository
import com.fantasykingdom.app.data.repository.ParkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val featuredRides: List<Ride> = emptyList(),
    val currentOffers: List<EventOffer> = emptyList(),
    val errorMessage: String? = null
)

/** Powers feature #4 (Home Dashboard) — a snapshot of the park: featured rides + live offers. */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val parkRepository: ParkRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { loadDashboard() }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val user = authRepository.fetchCurrentUser()
                val rides = parkRepository.getRides().take(6)
                val offers = parkRepository.getEventsAndOffers().take(4)
                _uiState.value = HomeUiState(
                    isLoading = false,
                    user = user,
                    featuredRides = rides,
                    currentOffers = offers
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Couldn't load the dashboard. Pull to refresh to try again."
                )
            }
        }
    }
}
