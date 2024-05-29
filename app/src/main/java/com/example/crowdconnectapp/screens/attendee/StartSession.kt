package com.example.crowdconnectapp.screens.attendee

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.crowdconnectapp.models.QuizViewModel

@Composable
fun StartSession(navController: NavController, qrcode: String?) {
    val quizViewModel: QuizViewModel = hiltViewModel()

    if (qrcode != null && qrcode.startsWith("QZ", true)) {
        StartQuiz(navController,qrcode)
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
