package com.example.crowdconnectapp.models

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class Question(val text: String, val options: List<String>, val correctAnswerIndex: Int)

class QuizViewModel @Inject constructor() : ViewModel() {
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> get() = _questions

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var sessioncode by mutableStateOf("")
    var selectedDate by mutableStateOf("")
    var selectedTime by mutableStateOf("")
    var duration by mutableStateOf(0)
    var durationIn by mutableStateOf("")
    var timeout by mutableStateOf(0)
    var timeoutIn by mutableStateOf("")

    var isTimeoutEnabled by mutableStateOf(false)
    var isShuffleQuestionsEnabled by mutableStateOf(false)
    var isShuffleOptionsEnabled by mutableStateOf(false)
    var isEvaluateEnabled by mutableStateOf(false)
    var isKioskEnabled by mutableStateOf(false)

    var isScheduleEnabled by mutableStateOf(false)
    var isDurationEnabled by mutableStateOf(false)

    private val firestore = FirebaseFirestore.getInstance()
    fun fetchSessionData(qrCode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val document = firestore.collection("Sessions").document(qrCode).get().await()
                if (document.exists()) {
                    title = document.getString("title") ?: ""
                    description = document.getString("description") ?: ""
                    sessioncode = document.getString("sessioncode") ?: ""
                    selectedDate = document.getString("selectedDate") ?: ""
                    selectedTime = document.getString("selectedTime") ?: ""
                    duration = document.getLong("duration")?.toInt() ?: 0
                    durationIn = document.getString("durationIn") ?: ""
                    timeout = document.getLong("timeout")?.toInt() ?: 0
                    timeoutIn = document.getString("timeoutIn") ?: ""

                    isTimeoutEnabled = document.getBoolean("isTimeoutEnabled") ?: false
                    isShuffleQuestionsEnabled = document.getBoolean("isShuffleQuestionsEnabled") ?: false
                    isShuffleOptionsEnabled = document.getBoolean("isShuffleOptionsEnabled") ?: false
                    isEvaluateEnabled = document.getBoolean("isEvaluateEnabled") ?: false
                    isKioskEnabled = document.getBoolean("isKioskEnabled") ?: false

                    isScheduleEnabled = document.getBoolean("isScheduleEnabled") ?: false
                    isDurationEnabled = document.getBoolean("isDurationEnabled") ?: false

                    val questionsList = document.get("questions") as? List<Map<String, Any>>
                    val fetchedQuestions = questionsList?.map { questionMap ->
                        val text = questionMap["question"] as? String ?: ""
                        val options = questionMap["options"] as? List<String> ?: emptyList()
                        val correctAnswerIndex = (questionMap["correctAnswerIndex"] as? Long)?.toInt() ?: 0

                        Log.d("QuizViewModel", "Question fetched: text=$text, options=$options, correctAnswerIndex=$correctAnswerIndex")

                        Question(
                            text = text,
                            options = options,
                            correctAnswerIndex = correctAnswerIndex
                        )
                    } ?: emptyList()

                    _questions.value = fetchedQuestions
                    Log.d("QuizViewModel", "Total Questions fetched: ${fetchedQuestions.size}")
                } else {
                    Log.d("QuizViewModel", "No document found")
                }
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error fetching data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun addQuestion(question: Question) {
    _questions.value = _questions.value.plus(question)
    }

    fun clearQuestions() {
        _questions.value = emptyList()
    }
}
