package com.example.crowdconnectapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crowdconnectapp.screens.*
import com.example.crowdconnectapp.screens.otp.LoginScreen
import com.example.crowdconnectapp.screens.otp.OtpVerificationScreen
import com.example.crowdconnectapp.screens.quiz.CreateQuizQuestions
import com.example.crowdconnectapp.screens.quiz.ManageQuestions
import com.example.crowdconnectapp.screens.quiz.OrganizeQuizScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostController() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "hostScreen") {
        composable(route = "welcomeScreen") {
            WelcomeScreen(navController)
        }
        composable(route = "hostScreen") {
            HostScreen(navController)
        }
        composable(route = "loginscreen") {
            LoginScreen(navController)
        }
        composable(
            route = "otpVerificationScreen/{verificationId}",
            arguments = listOf(navArgument("verificationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId")
            OtpVerificationScreen(navController, verificationId)
        }
        composable(route = "attendeeScreen") {
            AttendeeDashboard(navController)
        }
        composable(route = "organizeQuizScreen") {
            OrganizeQuizScreen(navController)
        }
        composable(route = "organizeQuizScreenwithtab") {
            OrganizeQuizScreen(navController, 1)
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
