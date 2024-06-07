package com.example.crowdconnectapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.screens.SplashScreen
import com.example.crowdconnectapp.screens.UpdateProfileScreen
import com.example.crowdconnectapp.screens.UserOnboardingScreen
import com.example.crowdconnectapp.screens.WelcomeScreen
import com.example.crowdconnectapp.screens.attendee.AttendeeScreen
import com.example.crowdconnectapp.screens.attendee.StartSession
import com.example.crowdconnectapp.screens.otp.LoginScreen
import com.example.crowdconnectapp.screens.otp.OtpVerificationScreen
import com.example.crowdconnectapp.screens.host.HostScreen
import com.example.crowdconnectapp.screens.host.quiz.OrganizeQuizScreen
import com.example.crowdconnectapp.screens.host.OrganizeVotingScreen
import com.example.crowdconnectapp.screens.host.RecentsSessions
import com.example.crowdconnectapp.screens.host.SessionResponses

@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splashScreen") {
        composable("splashScreen") {
            SplashScreen(navController, authViewModel)
        }
        composable("userOnboardingScreen") {
            UserOnboardingScreen(navController, authViewModel)
        }
        composable(route = "loginScreen") {
            LoginScreen(navController)
        }
        composable("updateProfileScreen") {
            UpdateProfileScreen(navController, authViewModel)
        }
        composable("welcomeScreen") {
            WelcomeScreen(navController, authViewModel)
        }
        composable(route = "hostScreen") {
            HostScreen(navController,authViewModel)
        }
        composable(
            route = "otpVerificationScreen/{verificationId}",
            arguments = listOf(navArgument("verificationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId")
            OtpVerificationScreen(navController, verificationId,authViewModel)
        }
        composable(route = "organizeQuizScreen") {
            OrganizeQuizScreen(navController)
        }
        composable(route = "organizeQuizScreenwithtab") {
            OrganizeQuizScreen(navController, 1)
        }
        composable(route = "organizeVotingScreen") {
            OrganizeVotingScreen(navController)
        }
        composable(route = "recentsSessions") {
            RecentsSessions(authViewModel,navController)
        }
        composable(route = "attendeeScreen") {
            AttendeeScreen(authViewModel,navController)
        }
        composable(
            route = "startSession/{qrcode}",
            arguments = listOf(navArgument("qrcode") { type = NavType.StringType })
        ) { backStackEntry ->
            val qrcode = backStackEntry.arguments?.getString("qrcode")
            if (qrcode != null) {
                StartSession(navController, qrcode)
            }
        }
        composable(
            route = "sessionResponses/{sessionId}",
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId")
            if (sessionId != null) {
                SessionResponses(navController,sessionId)
            }
        }
    }
}