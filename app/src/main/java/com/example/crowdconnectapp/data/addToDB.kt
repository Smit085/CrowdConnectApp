package com.example.crowdconnectapp.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.crowdconnectapp.models.QuizViewModel
import com.google.firebase.firestore.FirebaseFirestore


fun addToDB(viewModel: ViewModel, collection: String, sessionId: String) {
    val firestore = FirebaseFirestore.getInstance()

    val dataMap = when (viewModel) {
        is QuizViewModel -> {
            val quizViewModel = viewModel as QuizViewModel
            mapOf(
                "title" to quizViewModel.title,
                "description" to quizViewModel.description
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