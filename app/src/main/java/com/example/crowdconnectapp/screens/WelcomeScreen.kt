package com.example.crowdconnectapp.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.R
import androidx.compose.ui.Alignment
import com.example.crowdconnectapp.models.AuthViewModel

@Composable
fun WelcomeScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var selectedOption by remember { mutableStateOf<Option?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val activity = LocalContext.current as? Activity

    BackHandler {
        showExitDialog = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_connect),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to CrowdConnect",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Join the session or host your own.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            OptionButton("Get started as a Host") {
                selectedOption = Option.Host
                navController.navigate("hostScreen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OptionButton("Continue as an Attendee") {
                selectedOption = Option.Attendee
                navController.navigate("attendeeScreen")
            }
        }

        IconButton(
            onClick = {
                showDialog = true
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (showDialog) {
            LogoutConfirmationDialog(
                onConfirm = {
                    showDialog = false
                    authViewModel.logout()
                    navController.navigate("loginScreen") {
                        popUpTo("welcomeScreen") { inclusive = true }
                    }
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }

        if (showExitDialog) {
            ExitConfirmationDialog(
                onConfirm = {
                    showExitDialog = false
                    activity?.finish()
                },
                onDismiss = {
                    showExitDialog = false
                }
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Logout")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(text = "Logout")
        },
        text = {
            Text("Are you sure you want to logout?")
        }
    )
}

@Composable
fun ExitConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Exit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text(text = "Exit")
        },
        text = {
            Text("Are you sure you want to exit the app?")
        }
    )
}

@Composable
fun OptionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .width(250.dp)
    ) {
        Text(
            text = text, style = MaterialTheme.typography.titleMedium
        )
    }
}

enum class Option {
    Host, Attendee
}