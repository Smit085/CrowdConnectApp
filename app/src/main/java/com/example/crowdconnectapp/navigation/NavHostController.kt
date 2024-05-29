package com.example.crowdconnectapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crowdconnectapp.models.QuizViewModel
import com.example.crowdconnectapp.screens.*
import com.example.crowdconnectapp.screens.attendee.AttendQuiz
import com.example.crowdconnectapp.screens.attendee.AttendeeScreen
import com.example.crowdconnectapp.screens.attendee.BottomSheet
import com.example.crowdconnectapp.screens.attendee.StartQuiz
import com.example.crowdconnectapp.screens.attendee.StartSession
import com.example.crowdconnectapp.screens.otp.LoginScreen
import com.example.crowdconnectapp.screens.otp.OtpVerificationScreen
import com.example.crowdconnectapp.screens.host.quiz.CreateQuizQuestions
import com.example.crowdconnectapp.screens.host.HostScreen
import com.example.crowdconnectapp.screens.host.quiz.ManageQuestions
import com.example.crowdconnectapp.screens.host.quiz.OrganizeQuizScreen
import com.example.crowdconnectapp.screens.host.OrganizeVotingScreen

@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostController() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "attendeeScreen") {
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
            AttendeeScreen(navController)
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
        composable(route = "startSession/{qrcode}") { backStackEntry ->
            val qrcode = backStackEntry.arguments?.getString("qrcode")
            StartSession(navController, qrcode)
        }
    }
}
