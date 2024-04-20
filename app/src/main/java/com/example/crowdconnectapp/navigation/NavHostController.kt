package com.example.crowdconnectapp.navigation

import com.example.crowdconnectapp.screens.quiz.CreateQuizQuestions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crowdconnectapp.models.QuizViewModel
import com.example.crowdconnectapp.screens.*
import com.example.crowdconnectapp.screens.quiz.ManageQuestions
import com.example.crowdconnectapp.screens.quiz.OrganizeQuizScreen


@Composable
fun NavHostController() {
    val navController = rememberNavController()
    val quizViewModel = remember { QuizViewModel() }
    NavHost(navController = navController, startDestination = "hostScreen") {
        composable(route = "welcomeScreen") {
            WelcomeScreen(navController)
        }
        composable(route = "hostScreen") {
            HostScreen(navController)
        }
        composable(route = "attendeeScreen") {
            AttendeeDashboard(navController)
        }
        composable(route = "organizeQuizScreen") {
            OrganizeQuizScreen(navController)
        }
        composable(route = "organizeQuizScreenwithtab") {
            OrganizeQuizScreen(navController,1)
        }
        composable(route = "organizeVotingScreen") {
            OrganizeVotingScreen()
        }
        composable(route = "createQuizQuestions") {
                CreateQuizQuestions(
                    onQuestionAdded = {
                        navController.navigate("organizeQuizScreenwithtab")
                    })
        }
        composable(route = "manageQuestionsScreen") {
            ManageQuestions()
        }
    }
}