package com.example.crowdconnectapp.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.crowdconnectapp.components.qrcode.CardFace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class Question(val text: String, val options: List<String>, val correctAnswerIndex: Int)

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    fun addQuestion(question: Question) {
        _questions.value += question
    }

    fun removeQuestion(question: Question) {
        _questions.value = _questions.value - question
    }

    fun clearQuestions() {
        _questions.value = emptyList()
    }

    var title = ""
    var description = ""
    var sessioncode = ""
    var selectedDate = ""
    var selectedTime = ""
    var duration = 0
    var durationIn = ""
    var timeout = 0
    var timeoutIn = ""

    var isTimeoutEnabled: Boolean = false
    var isShuffleQuestionsEnabled: Boolean = false
    var isShuffleOptionsEnabled: Boolean = false
    var isEvaluateEnabled: Boolean = false
    var isKioskEnabled: Boolean = false

    var isScheduleEnabled: Boolean = false
    var isDurationEnabled: Boolean = false
}