package com.ayushxp.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.ayushxp.chatapp.data.repository.AuthRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel: ViewModel() {

    // Instance of AuthRepository
    private val authRepo = AuthRepository()

    // Private MutableStateFlows to observe loading, success, error- statuses
    private val _authLoading = MutableStateFlow(false)
    private val _authSuccess = MutableStateFlow(false)
    private val _authError = MutableStateFlow<String?>(null)

    // Public StateFlows to expose MutableStateFlows to the UI
    val authLoading: StateFlow<Boolean> = _authLoading
    val authSuccess: StateFlow<Boolean> = _authSuccess
    val authError: StateFlow<String?> = _authError

    // Functions --------------------------------------------------------------
    // Login function
    fun login(email: String, pass: String) {
        _authLoading.value = true
        authRepo.loginUser(email, pass) { success, error ->
            _authLoading.value = false
            _authSuccess.value = success
            _authError.value = error
        }
    }

    // Signup function
    fun signup(email: String, pass: String, username: String, timestamp: Timestamp) {
        _authLoading.value = true

        authRepo.registerUser(email, pass, username, timestamp) { success, error ->
            _authLoading.value = false
            _authSuccess.value = success
            _authError.value = error
        }
    }

    // Reset auth states when login/signup successful
    fun resetAuthState() {
        _authSuccess.value = false
        _authError.value = null
    }
}