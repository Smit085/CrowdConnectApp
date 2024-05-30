package com.example.crowdconnectapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.screens.SplashScreen
import com.example.crowdconnectapp.screens.WelcomeScreen
import com.example.crowdconnectapp.screens.attendee.AttendeeScreen
import com.example.crowdconnectapp.screens.attendee.StartSession
import com.example.crowdconnectapp.screens.otp.LoginScreen
import com.example.crowdconnectapp.screens.otp.OtpVerificationScreen
import com.example.crowdconnectapp.screens.host.HostScreen
import com.example.crowdconnectapp.screens.host.quiz.OrganizeQuizScreen
import com.example.crowdconnectapp.screens.host.OrganizeVotingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

//    LaunchedEffect(key1 = isAuthenticated) {
//        if (isAuthenticated) {
//            navController.navigate("welcomeScreen") {
//                popUpTo("loginScreen") { inclusive = true }
//            }
//        } else {
//            navController.navigate("loginScreen") {
//                popUpTo("welcomeScreen") { inclusive = true }
//            }
//        }
//    }
    NavHost(navController, startDestination = "splashScreen") {
        composable("splashScreen") {
            SplashScreen(navController, authViewModel)
        }
        composable(route = "welcomeScreen") {
            WelcomeScreen(navController,authViewModel)
        }
        composable(route = "hostScreen") {
            HostScreen(navController)
        }
        composable(route = "loginScreen") {
            LoginScreen(authViewModel, navController)
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
        composable(route = "startSession/{qrcode}", arguments = listOf(navArgument("qrcode") { type = NavType.StringType })) { backStackEntry ->
            val qrcode = backStackEntry.arguments?.getString("qrcode")
            StartSession(navController, qrcode)
        }
    }
}
