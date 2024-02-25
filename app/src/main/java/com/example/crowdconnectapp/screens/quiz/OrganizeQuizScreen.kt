package com.example.crowdconnectapp.screens.quiz
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.models.QuizViewModel


data class TabItem(val title: String, val screen: @Composable ((QuizViewModel) -> Unit)? = null)

val tabItems = listOf(
    TabItem("Configure") { _ -> ConfigureQuiz() },
    TabItem("Questions") { quizViewModel -> ManageQuestions(quizViewModel) },
    TabItem("Publish")
)
@Composable
fun OrganizeQuizScreen(navController: NavHostController, quizViewModel: QuizViewModel, currentTabIndex: Int = 0) {
    TabLayout(navController, tabItems, quizViewModel,currentTabIndex)
}

