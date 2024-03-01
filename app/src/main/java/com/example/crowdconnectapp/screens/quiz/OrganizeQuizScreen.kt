package com.example.crowdconnectapp.screens.quiz
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.components.qrcode.FlipCard


data class TabItem(val title: String, val screen: @Composable () -> Unit)

val tabItems = listOf(
    TabItem("Configure") { ConfigureQuiz() },
    TabItem("Questions") { ManageQuestions() },
    TabItem("Publish") { PublishScreen() }
)
@Composable
fun OrganizeQuizScreen(navController: NavHostController, currentTabIndex: Int = 0) {
    TabLayout(navController, tabItems,currentTabIndex)
}

