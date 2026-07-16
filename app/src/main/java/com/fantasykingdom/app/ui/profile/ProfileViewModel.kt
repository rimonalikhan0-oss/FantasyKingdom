package com.fantasykingdom.app.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasykingdom.app.data.model.Ticket
import com.fantasykingdom.app.data.model.User
import com.fantasykingdom.app.data.repository.AuthRepository
import com.fantasykingdom.app.data.repository.StorageRepository
import com.fantasykingdom.app.data.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val upcomingTickets: List<Ticket> = emptyList(),
    val isUploadingPhoto: Boolean = false
)

/** Feature #12 — User Profile: shows account info, upcoming tickets, and a photo uploader. */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val ticketRepository: TicketRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            val user = authRepository.fetchCurrentUser()
            val tickets = authRepository.currentUserId?.let { ticketRepository.getTicketsForUser(it) } ?: emptyList()
            _uiState.value = ProfileUiState(isLoading = false, user = user, upcomingTickets = tickets)
        }
    }

    fun uploadProfilePhoto(uri: Uri) {
        val uid = authRepository.currentUserId ?: return
        _uiState.value = _uiState.value.copy(isUploadingPhoto = true)
        viewModelScope.launch {
            runCatching { storageRepository.uploadProfilePhoto(uid, uri) }
            loadProfile()
            _uiState.value = _uiState.value.copy(isUploadingPhoto = false)
        }
    }

    fun logout() = authRepository.signOut()
}
