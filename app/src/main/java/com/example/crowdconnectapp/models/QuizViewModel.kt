package com.example.crowdconnectapp.models

import androidx.lifecycle.ViewModel
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
}