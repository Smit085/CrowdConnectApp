package com.example.crowdconnectapp.screens.quiz
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController


data class TabItem(val title: String, val screen: () -> String)

@RequiresApi(Build.VERSION_CODES.O)
val tabItems = listOf(
    TabItem("Configure") { "ConfigureQuiz" },
    TabItem("Questions") { "ManageQuestions" },
    TabItem("Publish") { "PublishScreen" }
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrganizeQuizScreen(navController: NavHostController, currentTabIndex: Int = 0) {
    TabLayout(navController, tabItems,currentTabIndex)
}

