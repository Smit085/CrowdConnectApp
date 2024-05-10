package com.example.crowdconnectapp.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.crowdconnectapp.models.Question
import com.example.crowdconnectapp.models.QuizViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun addToDB(viewModel: ViewModel, collection: String, sessionId: String) {
    val firestore = FirebaseFirestore.getInstance()

    val dataMap = when (viewModel) {
        is QuizViewModel -> {
            val quizViewModel = viewModel
            val questionsMap = convertQuestionsToMap(quizViewModel.questions.value)

            if (quizViewModel.selectedDate.isEmpty()) quizViewModel.selectedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            if (quizViewModel.selectedTime.isEmpty()) quizViewModel.selectedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            mapOf(
                "title" to quizViewModel.title,
                "description" to quizViewModel.description,
                "selectedDate" to quizViewModel.selectedDate,
                "selectedTime" to quizViewModel.selectedTime,
                "duration" to quizViewModel.duration,
                "durationIn" to quizViewModel.durationIn,
                "timeout" to quizViewModel.timeout,
                "timeoutIn" to quizViewModel.timeoutIn,
                "isScheduleEnabled" to quizViewModel.isScheduleEnabled,
                "isDurationEnabled" to quizViewModel.isDurationEnabled,
                "isTimeoutEnabled" to quizViewModel.isTimeoutEnabled,
                "isShuffleQuestionsEnabled" to quizViewModel.isShuffleQuestionsEnabled,
                "isShuffleOptionsEnabled" to quizViewModel.isShuffleOptionsEnabled,
                "isEvaluateEnabled" to quizViewModel.isEvaluateEnabled,
                "isKioskEnabled" to quizViewModel.isKioskEnabled,
                "questions" to questionsMap
            )
        }
//        is PollViewModel -> {
//            val pollViewModel = viewModel as PollViewModel
//            mapOf(
//                "question" to pollViewModel.question,
//                // Add other properties specific to PollViewModel
//                "sessionType" to sessionType,
//                "sessionId" to sessionId
//            )
//        }
        else -> {
            emptyMap()
        }
    }

    firestore.collection(collection)
        .document(sessionId)
        .set(dataMap)
        .addOnSuccessListener {
            Log.d("DB", "Document added with ID: $sessionId")
        }
        .addOnFailureListener { e ->
            Log.w("DB", "Error adding document", e)
        }
}

fun readDB(collection: String, sessionId: String){
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection(collection)
        .document(sessionId)
        .get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val data = documentSnapshot.data
                // Access and process data
                Log.d("DB", "Data retrieved: $data")
            } else {
                Log.d("DB", "No document found")
            }
        }
        .addOnFailureListener { e ->
            Log.w("DB", "Error fetching document", e)
        }
}

fun convertQuestionsToMap(questions: List<Question>): List<Map<String, Any>> {
    return questions.map { question ->
        mapOf(
            "correctAnswerIndex" to question.correctAnswerIndex,
            "options" to question.options,
            "question" to question.text,
        )
    }
}