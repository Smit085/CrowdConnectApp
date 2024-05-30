package com.example.crowdconnectapp.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor() : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Simulate a delay to check authentication status
        viewModelScope.launch {
            _isAuthenticated.value = auth.currentUser != null
            _isLoading.value = false
        }
    }

    fun logout() {
        auth.signOut()
        _isAuthenticated.value = false
    }

    private fun onLoginSuccess() {
        _isAuthenticated.value = true
    }
}
