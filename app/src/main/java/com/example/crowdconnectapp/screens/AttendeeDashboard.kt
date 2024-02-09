package com.example.crowdconnectapp.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun AttendeeDashboard(navController: NavHostController) {
    Text(
        text = "Attendee Screen",
        style = MaterialTheme.typography.headlineMedium
    )
}