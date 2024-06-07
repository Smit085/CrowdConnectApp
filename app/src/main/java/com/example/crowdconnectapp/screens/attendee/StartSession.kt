package com.example.crowdconnectapp.screens.attendee

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.crowdconnectapp.models.QuizViewModel

@Composable
fun StartSession(navController: NavController, qrcode: String?) {
    val context = LocalContext.current
    if (qrcode != null && qrcode.startsWith("QZ", true)) {
        StartQuiz(navController,qrcode)
    } else {
        Toast.makeText(context, "Incorrect session code try again!", Toast.LENGTH_SHORT).show()
        navController.navigate("attendeeScreen") {
            popUpTo("startSession") { inclusive = true }
        }
    }

//    LaunchedEffect(qrcode) {
//        if (qrcode != null && qrcode.startsWith("QZ", true)) {
//            quizViewModel.fetchSessionData(qrcode)
//        }
//    }

//    if (!isLoading) {
//        // Navigate to startQuiz destination
//        StartQuiz(navController)
//    } else {
//        // Show loading state or progress indicator
//        Text("Fetching session data...")
//    }
}
