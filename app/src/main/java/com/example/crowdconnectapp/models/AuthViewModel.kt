package com.example.crowdconnectapp.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.crowdconnectapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _isAuthenticated = MutableStateFlow(auth.currentUser != null)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _isNewUser = MutableStateFlow(false)
    val isNewUser: StateFlow<Boolean> = _isNewUser

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userAvatar = MutableStateFlow(0)
    val userAvatar: StateFlow<Int> = _userAvatar

    private val _userGender = MutableStateFlow("")
    val userGender: StateFlow<String> = _userGender

    init {
        // Simulate a delay to check authentication status
        viewModelScope.launch {
            _isAuthenticated.value = auth.currentUser != null
            if (auth.currentUser != null) {
                fetchUserInfo()
            }
            _isLoading.value = false
        }
    }

    fun checkIfNewUser(onNewUserChecked: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.phoneNumber ?: ""
        if (uid.isNotBlank()) {
            val hostDocRef = firestore.collection("Host").document(uid)
            hostDocRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.i("AuthViewModel","User is not new")
                    _isNewUser.value = false
                    fetchUserInfo()
                    onNewUserChecked(false)
                } else {
                    val attendeeDocRef = firestore.collection("Attendee").document(uid)
                    attendeeDocRef.get().addOnSuccessListener { doc ->
                        val isNewAttendee = !doc.exists()
                        _isNewUser.value = isNewAttendee
                        onNewUserChecked(isNewAttendee)
                    }.addOnFailureListener {
                        Log.i("AuthViewModel","Failed to check if user is new")
                        onNewUserChecked(false)
                    }
                }
            }.addOnFailureListener {
                Log.i("AuthViewModel","Failed to check if user is new")
                onNewUserChecked(false)
            }
        } else {
            onNewUserChecked(false)
        }
    }

    private fun fetchUserInfo() {
        val uid = auth.currentUser?.phoneNumber ?: return
        firestore.collection("Host").document(uid).get().addOnSuccessListener { document ->
            if (document.exists()) {
                _userName.value = document.getString("name") ?: ""
                _userAvatar.value = document.getLong("avatar")?.toInt() ?: 0
                _userGender.value = document.getString("gender") ?: ""
            }
        }.addOnFailureListener { e ->
            Log.e("AuthViewModel", "Error fetching user info", e)
        }
    }

    fun updateProfile(
        userName: String,
        selectedAvatarIndex: Int,
        selectedGender: String,
        onSucess: () -> Unit,
    ) {
        if (userName.isNotEmpty()) {
            _userName.value = userName
            _userAvatar.value = selectedAvatarIndex
            _userGender.value = selectedGender
            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.phoneNumber ?: ""
            val firestore = FirebaseFirestore.getInstance()

            val userData = mapOf(
                "name" to userName,
                "avatar" to selectedAvatarIndex,
                "gender" to selectedGender
            )

            // Update Host profile
            firestore.collection("Host").document(uid).set(userData)
                .addOnSuccessListener {
                    // Host profile updated successfully, now update Attendee profile
                    firestore.collection("Attendee").document(uid).set(userData)
                        .addOnSuccessListener {
                            // Attendee profile updated successfully
                            onSucess()
                        }.addOnFailureListener { e ->
                            // Handle failure to update Attendee profile
                            Log.e("AuthViewModel", "Error updating Attendee profile", e)
                        }
                }.addOnFailureListener { e ->
                    // Handle failure to update Host profile
                    Log.e("AuthViewModel", "Error updating Host profile", e)
                }
        } else {
            // Handle case when user is not new or name is empty
        }
    }


    fun logout() {
        auth.signOut()
        _isAuthenticated.value = false
        _isNewUser.value = false
        _userName.value = ""
        _userAvatar.value = 0
        _userGender.value = ""
    }

}
fun getAvatarResource(avatarIndex: Int): Int {
    return when (avatarIndex) {
        0 -> R.drawable.avatar1
        1 -> R.drawable.avatar2
        2 -> R.drawable.avatar3
        3 -> R.drawable.avatar4
        4 -> R.drawable.avatar5
        else -> R.drawable.avatar1 // Default avatar resource
    }
}
