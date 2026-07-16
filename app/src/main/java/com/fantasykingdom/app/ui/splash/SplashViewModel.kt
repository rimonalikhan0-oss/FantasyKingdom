package com.fantasykingdom.app.ui.splash

import androidx.lifecycle.ViewModel
import com.fantasykingdom.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun isUserLoggedIn(): Boolean = authRepository.isLoggedIn
}
