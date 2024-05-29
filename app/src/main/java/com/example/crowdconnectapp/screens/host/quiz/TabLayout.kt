package com.example.crowdconnectapp.screens.host.quiz

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var openAddQuestionScreen by remember { mutableStateOf(false) }
    val pagerState =
        rememberPagerState(pageCount = { tabItems.size }, initialPage = currentTabIndex)

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
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
}

@Composable
fun FullScreenAddQuestionScreen(onClose: () -> Unit, quizViewModel: QuizViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
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
