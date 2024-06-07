package com.example.crowdconnectapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.R
import com.example.crowdconnectapp.models.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserOnboardingScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var name by remember { mutableStateOf("") }
    var selectedAvatarIndex by remember { mutableStateOf<Int?>(null) }
    var selectedGender by remember { mutableStateOf("") }
    val isNewUser by authViewModel.isNewUser.collectAsState()
    val maxNameLength = 25
    val avatars = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3,
        R.drawable.avatar4,
        R.drawable.avatar5
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 20.dp),
            text = "Hi there 😊",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Let's learn about you!",
            fontSize = 20.sp,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Complete your profile for a better app journey!"
        )
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "Name",
            maxLines = 1,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = name,
            onValueChange = {
                if (it.length <= maxNameLength) {
                    name = it
                }
            },
            placeholder = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = "Select your gender:",
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            GenderOption("Male", selectedGender == "Male") {
                selectedGender = "Male"
            }
            Spacer(modifier = Modifier.width(16.dp))
            GenderOption("Female", selectedGender == "Female") {
                selectedGender = "Female"
            }
            Spacer(modifier = Modifier.width(16.dp))
            GenderOption("Other", selectedGender == "Other") {
                selectedGender = "Other"
            }
        }
        Spacer(modifier = Modifier.height(26.dp))
        Text(
            text = "Select your avatar:",
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            avatars.forEachIndexed { index, avatarRes ->
                AvatarOption(
                    avatarRes = avatarRes,
                    isSelected = selectedAvatarIndex == index
                ) {
                    selectedAvatarIndex = index
                }
            }
        }
        Spacer(modifier = Modifier.height(120.dp))
        Button(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.width(350.dp),
            onClick = {
                if (name.isNotEmpty() && selectedAvatarIndex != null && selectedGender.isNotEmpty()) {
                    onSubmit(name, selectedAvatarIndex!!, selectedGender, navController, isNewUser)
                } else {
                    // Handle form validation
                }
            },
            enabled = name.isNotEmpty() && selectedAvatarIndex != null && selectedGender.isNotEmpty(),
        ) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun GenderOption(gender: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = gender)
    }
}

@Composable
fun AvatarOption(avatarRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Avatar",
            modifier = Modifier.size(58.dp)
        )
    }
}

fun onSubmit(
    userName: String,
    selectedAvatarIndex: Int,
    selectedGender: String,
    navController: NavHostController,
    isNewUser: Boolean
) {
    if (userName.isNotEmpty() && isNewUser) { // Check if user is new
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.phoneNumber ?: ""
        val firestore = FirebaseFirestore.getInstance()

        val userData = mapOf(
            "name" to userName,
            "avatar" to selectedAvatarIndex,
            "gender" to selectedGender,
            "mobno" to uid
        )

        firestore.collection("Host").document(uid).set(userData)
        firestore.collection("Attendee").document(uid).set(userData).addOnSuccessListener {
            navController.navigate("welcomeScreen") {
                popUpTo("userOnboardingScreen") { inclusive = true }
            }
        }.addOnFailureListener {
            // Handle failure
        }
    } else {
        // Prompt user to provide their name or handle other cases
    }
}
