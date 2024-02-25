package com.example.crowdconnectapp.data

import com.example.crowdconnectapp.models.Question
import com.google.firebase.Firebase
import com.google.firebase.database.database

class QuestionRepository {
    private val database = Firebase.database
    private val questionsRef = database.getReference("questions")

    fun addQuestion(question: Question) {
        // Push a new question to the database
        val newQuestionRef = questionsRef.push()
        newQuestionRef.setValue(question)
    }
}