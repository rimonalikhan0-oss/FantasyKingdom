package com.fantasykingdom.app.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.Ticket
import com.fantasykingdom.app.data.model.TicketType
import com.fantasykingdom.app.data.repository.AuthRepository
import com.fantasykingdom.app.data.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookingUiState(
    val ticketType: TicketType = TicketType.ONE_DAY,
    val guestCount: Int = 1,
    val isBooking: Boolean = false,
    val errorMessage: String? = null,
    val bookedTicketId: String? = null
) {
    val pricePerGuest: Double
        get() = when (ticketType) {
            TicketType.ONE_DAY -> 79.0
            TicketType.TWO_DAY -> 139.0
            TicketType.ANNUAL_PASS -> 299.0
            TicketType.VIP_EXPRESS -> 189.0
        }
    val totalPrice: Double get() = pricePerGuest * guestCount
}

/** Feature #6 — Ticket Booking: choose a pass type + guest count, then book against Firestore. */
@HiltViewModel
class BookingViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    fun selectTicketType(type: TicketType) {
        _uiState.value = _uiState.value.copy(ticketType = type)
    }

    fun setGuestCount(count: Int) {
        _uiState.value = _uiState.value.copy(guestCount = count.coerceIn(1, 10))
    }

    fun bookTicket() {
        val uid = authRepository.currentUserId
        if (uid == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please log in to book tickets.")
            return
        }
        _uiState.value = _uiState.value.copy(isBooking = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val ticket = ticketRepository.bookTicket(
                    Ticket(
                        userId = uid,
                        type = _uiState.value.ticketType,
                        guestCount = _uiState.value.guestCount,
                        totalPrice = _uiState.value.totalPrice
                    )
                )
                _uiState.value = _uiState.value.copy(isBooking = false, bookedTicketId = ticket.id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isBooking = false, errorMessage = "Booking failed. Please try again.")
            }
        }
    }
}
