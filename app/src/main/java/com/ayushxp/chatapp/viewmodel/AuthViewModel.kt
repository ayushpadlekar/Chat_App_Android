package com.ayushxp.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.ayushxp.chatapp.data.repository.AuthRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    // For user account details to show in popup dialog & signout
    // Private MutableStateFlows to observe Firestore- username, email, timestamp
    private val _username = MutableStateFlow<String>("")
    private val _email = MutableStateFlow<String>("")
    private val _createdTime = MutableStateFlow<String>("")
    // Public StateFlows to expose MutableStateFlows to the UI
    val username = _username.asStateFlow()
    val email = _email.asStateFlow()
    val createdTime = _createdTime.asStateFlow()


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


    // get details of user's account like- username, email, timestamp
    fun getUserAccountDetails() {
        authRepo.getCurrentUserAccountDetails { username, email, timestamp ->
            if(username != null && email != null && timestamp != null) {
                _username.value = username
                _email.value = email
                _createdTime.value = SimpleDateFormat("dd/MM/yyyy \n hh:mm a", Locale.getDefault()).format(timestamp)
            }
        }
    }

    // logout user
    fun logout() {
        authRepo.logoutUser()
    }
}