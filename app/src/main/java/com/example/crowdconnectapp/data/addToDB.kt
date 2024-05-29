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
            val questionsMap = viewModel.questions.value.let { convertQuestionsToMap(it) }

            if (viewModel.selectedDate.isEmpty()) viewModel.selectedDate =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            if (viewModel.selectedTime.isEmpty()) viewModel.selectedTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            if (viewModel.timeout == 0) viewModel.timeout = 10
            mapOf(
                "title" to viewModel.title,
                "description" to viewModel.description,
                "selectedDate" to viewModel.selectedDate,
                "selectedTime" to viewModel.selectedTime,
                "duration" to viewModel.duration,
                "durationIn" to viewModel.durationIn,
                "timeout" to viewModel.timeout,
                "timeoutIn" to viewModel.timeoutIn,
                "isScheduleEnabled" to viewModel.isScheduleEnabled,
                "isDurationEnabled" to viewModel.isDurationEnabled,
                "isTimeoutEnabled" to viewModel.isTimeoutEnabled,
                "isShuffleQuestionsEnabled" to viewModel.isShuffleQuestionsEnabled,
                "isShuffleOptionsEnabled" to viewModel.isShuffleOptionsEnabled,
                "isEvaluateEnabled" to viewModel.isEvaluateEnabled,
                "isKioskEnabled" to viewModel.isKioskEnabled,
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