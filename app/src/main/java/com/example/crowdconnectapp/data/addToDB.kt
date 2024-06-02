package com.example.crowdconnectapp.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.crowdconnectapp.models.Question
import com.example.crowdconnectapp.models.QuizViewModel
import com.example.crowdconnectapp.models.UserResponse
import com.example.crowdconnectapp.screens.attendee.AtendeeSession
import com.example.crowdconnectapp.screens.attendee.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun addToDB(viewModel: ViewModel, collection: String, sessionId: String) {
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val hostId = currentUser?.phoneNumber ?: ""

    val dataMap = when (viewModel) {
        is QuizViewModel -> {
            val questionsMap = viewModel.questions.value.let { convertQuestionsToMap(it) }

            if (viewModel.selectedDate.isEmpty()) viewModel.selectedDate =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            if (viewModel.selectedTime.isEmpty()) viewModel.selectedTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            if (viewModel.timeout == 0) viewModel.timeout = 10
            if (viewModel.timeoutIn == "") viewModel.timeoutIn = "min"
            if (viewModel.durationIn == "") viewModel.durationIn = "sec"

            mapOf(
                "hostId" to hostId,
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
fun convertQuestionsToMap(questions: List<Question>): List<Map<String, Any>> {
    return questions.map { question ->
        mapOf(
            "correctAnswerIndex" to question.correctAnswerIndex,
            "options" to question.options,
            "question" to question.question,
        )
    }
}
fun submitResponses(
    attendeeId: String,
    name: String,
    mobno: String,
    sessionId: String,
    responses: MutableList<UserResponse>,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()

    // Current date and time
    val currentDateTime = Calendar.getInstance().time
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormatter.format(currentDateTime)
    val formattedTime = timeFormatter.format(currentDateTime)

    // Create a list of maps for the responses
    val responsesMap = responses.map { response ->
        mapOf(
            "questionIndex" to response.questionIndex,
            "selectedAnswerIndex" to response.selectedOptionIndex
        )
    }
    val sessionData = mapOf(
        "sessionId" to sessionId,
        "responses" to responsesMap,
        "date" to formattedDate, // Store date of session response
        "time" to formattedTime  // Store time of session response
    )

    val attendeeDocRef = firestore.collection("Attendee").document(attendeeId)

    firestore.runTransaction { transaction ->
        // Check if attendee document exists
        val attendeeSnapshot = transaction.get(attendeeDocRef)
        if (!attendeeSnapshot.exists()) {
            // Create new attendee document if it doesn't exist
            val newAttendee = mapOf(
                "name" to name,
                "mobno" to mobno,
                "attendedSessionlist" to listOf(sessionData)
            )
            transaction.set(attendeeDocRef, newAttendee)
        } else {
            // Update existing attendee document
            val existingSessions = attendeeSnapshot.get("attendedSessionlist") as? List<Map<String, Any>> ?: listOf()
            val updatedSessions = existingSessions.toMutableList().apply {
                add(sessionData)
            }
            transaction.update(attendeeDocRef, "attendedSessionlist", updatedSessions)
        }
    }.addOnSuccessListener {
        onSuccess()
    }.addOnFailureListener { exception ->
        onFailure(exception)
    }
}

suspend fun fetchAttendedSessions(attendeeId: String): List<AtendeeSession> {
    val firestore = FirebaseFirestore.getInstance()
    val attendeeDocRef = firestore.collection("Attendee").document(attendeeId)

    return attendeeDocRef.get().await().let { documentSnapshot ->
        if (documentSnapshot.exists()) {
            val attendedSessions = documentSnapshot.get("attendedSessionlist") as? List<Map<String, Any>> ?: emptyList()
            attendedSessions.mapNotNull { sessionData ->
                val sessionId = sessionData["sessionId"] as? String ?: return@mapNotNull null
                val responsesMap = sessionData["responses"] as? List<Map<String, Any>> ?: emptyList()
                val responses = responsesMap.map { responseMap ->
                    Response(
                        questionIndex = (responseMap["questionIndex"] as? Long)?.toInt() ?: 0,
                        selectedAnswerIndex = (responseMap["selectedAnswerIndex"] as? Long)?.toInt() ?: 0
                    )
                }
                AtendeeSession(
                    sessionId = sessionId,
                    title = "", // Initially empty, fetched later
                    responses = responses,
                    date = sessionData["date"] as? String ?: "",
                    time = sessionData["time"] as? String ?: ""
                )
            }
        } else {
            emptyList()
        }
    }
}



suspend fun fetchSessionData(sessionId: String): Pair<String, List<Question>> {
    val firestore = FirebaseFirestore.getInstance()
    val sessionDocRef = firestore.collection("Sessions").document(sessionId)

    return sessionDocRef.get().await().let { documentSnapshot ->
        if (documentSnapshot.exists()) {
            val title = documentSnapshot.getString("title") ?: "Untitled"
            val questions =
                documentSnapshot.get("questions") as? List<Map<String, Any>> ?: emptyList()
            val questionsList = questions.mapNotNull { questionData ->
                val question = questionData["question"] as? String
                val options = questionData["options"] as? List<String>
                val correctAnswerIndex = (questionData["correctAnswerIndex"] as? Long)?.toInt()

                if (question != null && options != null && correctAnswerIndex != null) {
                    Question(
                        question = question,
                        options = options,
                        correctAnswerIndex = correctAnswerIndex
                    )
                } else {
                    Log.e("FetchSessionData", "Invalid question data: $questionData")
                    null
                }
            }
            title to questionsList
        } else {
            "Untitled" to emptyList()
        }
    }
}

//For Host
fun addSession(
    hostId: String,
    name: String,
    mobno: String,
    sessionId: String,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()

    val sessionData = mapOf(
        "sessionId" to sessionId
    )

    val hostDocRef = firestore.collection("Host").document(hostId)

    firestore.runTransaction { transaction ->
        // Check if host document exists
        val hostSnapshot = transaction.get(hostDocRef)
        if (!hostSnapshot.exists()) {
            // Create new host document if it doesn't exist
            val newHost = mapOf(
                "name" to name,
                "mobno" to mobno,
                "createdSessionlist" to listOf(sessionData)
            )
            transaction.set(hostDocRef, newHost)
        } else {
            // Update existing host document
            val existingSessions = hostSnapshot.get("createdSessionlist") as? List<Map<String, Any>> ?: listOf()
            val updatedSessions = existingSessions.toMutableList().apply {
                add(sessionData)
            }
            transaction.update(hostDocRef, "createdSessionlist", updatedSessions)
        }
    }.addOnSuccessListener {
        onSuccess()
    }.addOnFailureListener { exception ->
        onFailure(exception)
    }
}
