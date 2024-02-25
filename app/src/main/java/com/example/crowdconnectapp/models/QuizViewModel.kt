package com.example.crowdconnectapp.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Question(val text: String, val options: List<String>, val correctAnswerIndex: Int)
class QuizViewModel : ViewModel() {
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
}