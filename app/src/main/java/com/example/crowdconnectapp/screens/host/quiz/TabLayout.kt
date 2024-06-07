package com.example.crowdconnectapp.screens.host.quiz

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.models.QuizViewModel
import com.example.crowdconnectapp.ui.theme.VividBlue

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TabLayout(
    navController: NavHostController,
    tabItems: List<TabItem>,
    currentTabIndex: Int = 0
) {
    val quizViewModel: QuizViewModel = hiltViewModel()
    var selectedTabIndex by remember { mutableStateOf(currentTabIndex) }
    var openAddQuestionScreen by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { tabItems.size }, initialPage = currentTabIndex)

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    var exitDialogOpen by remember { mutableStateOf(false) }
    val exitDialog: @Composable () -> Unit = {
        AlertDialog(
            onDismissRequest = { exitDialogOpen = false },
            title = { Text("Exit") },
            text = { Text("Are you sure you want to exit?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        exitDialogOpen = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Exit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { exitDialogOpen = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Corrected BackHandler implementation
    BackHandler {
        when (selectedTabIndex) {
            0 -> exitDialogOpen = true
            else -> selectedTabIndex -= 1
        }
    }

    // Scroll pager state based on selectedTabIndex changes
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    if (openAddQuestionScreen) {
        FullScreenAddQuestionScreen(
            onClose = { openAddQuestionScreen = false },
            quizViewModel = quizViewModel
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Create Quiz",
                                style = MaterialTheme.typography.titleLarge
                            )
                            if (selectedTabIndex == 1) {
                                TextButton(
                                    onClick = {
                                        openAddQuestionScreen = true
                                    },
                                ) {
                                    Text(text = "Add question", color = Color.Blue)
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = VividBlue,
                        titleContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    divider = {}
                ) {
                    tabItems.forEachIndexed { index, item ->
                        Tab(
                            modifier = Modifier.background(VividBlue),
                            selected = selectedTabIndex == index,
                            onClick = {
                                selectedTabIndex = index
                            },
                            text = {
                                Text(
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    text = item.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Pass the quizViewModel to the screen composable function
                    val temp = tabItems[it].screen()
                    when (temp) {
                        "ConfigureQuiz" -> ConfigureQuiz(quizViewModel)
                        "ManageQuestions" -> ManageQuestions(quizViewModel)
                        "PublishScreen" -> PublishScreen(navController, quizViewModel)
                    }
                }
            }
        }
    }

    if (exitDialogOpen) {
        exitDialog()
    }
}

@Composable
fun FullScreenAddQuestionScreen(onClose: () -> Unit, quizViewModel: QuizViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onClose) {
                    Text("Close", color = Color.Red)
                }
            }
            CreateQuizQuestions(
                onQuestionAdded = {
                    onClose()
                },
                quizViewModel = quizViewModel
            )
        }
    }
}