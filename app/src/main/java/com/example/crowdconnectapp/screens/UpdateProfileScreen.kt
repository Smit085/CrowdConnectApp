package com.example.crowdconnectapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.R
import com.example.crowdconnectapp.models.AuthViewModel

@Composable
fun UpdateProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var name by remember { mutableStateOf("") }
    var selectedAvatarIndex by remember { mutableStateOf(authViewModel.userAvatar.value) }
    var selectedGender by remember { mutableStateOf(authViewModel.userGender.value) }
    val context = LocalContext.current

    val maxNameLength = 25
    val avatars = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2,
        R.drawable.avatar3,
        R.drawable.avatar4,
        R.drawable.avatar5
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(vertical = 20.dp),
                text = "Update Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Edit your profile details",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Input fields for name and gender
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
                    if (name.isNotEmpty() && selectedGender.isNotEmpty()) {
                        // Update profile info
                        authViewModel.updateProfile(name, selectedAvatarIndex, selectedGender,
                            onSucess = {
                                navController.navigate("hostScreen") {
                                    popUpTo("updateProfileScreen") { inclusive = true }
                                }
                                Toast.makeText(
                                    context,
                                    "Profile updated successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    } else {
                        // Handle form validation
                    }
                },
                enabled = name.isNotEmpty() && selectedAvatarIndex != null && selectedGender.isNotEmpty(),
            ) {
                Text(
                    text = "Update Profile",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(18.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }
    }
}