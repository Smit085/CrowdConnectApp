package com.example.crowdconnectapp.screens.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.crowdconnectapp.components.qrcode.FlipCard
import com.example.crowdconnectapp.models.QuizViewModel

@Preview(showBackground = true)
@Composable
fun PublishScreen() {
    val quizViewModel: QuizViewModel = hiltViewModel()

    if (quizViewModel.sessioncode == "") {
        quizViewModel.sessioncode = generatecode("QUIZ")
    }

    Column(Modifier.fillMaxSize()) {
        FlipCard()
        Text(text = quizViewModel.sessioncode)
    }
}

fun generatecode(prefix: String): String {
    val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
    return prefix + (1..6)
        .map { kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
