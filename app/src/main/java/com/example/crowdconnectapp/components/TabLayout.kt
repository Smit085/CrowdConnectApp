import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.screens.OrganizeQuizScreen
import com.example.crowdconnectapp.ui.theme.VividBlue
import kotlinx.coroutines.launch

data class TabItem(val title: String, val screen: @Composable () -> Unit)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TabLayout(navController: NavHostController) {
    val tabItems = listOf(TabItem("Configure") { OrganizeQuizScreen(navController) },
        TabItem("Questions") { QuestionScreen() },
        TabItem("Publish") { OrganizeVotingScreen(navController) })
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState(pageCount = { tabItems.size })
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Create Quiz", style = MaterialTheme.typography.titleLarge
                    )
                    if (selectedTabIndex == 1) {
                        TextButton(
                            onClick = { navController.navigate("OrganizeVotingScreen") },
                        ) {
                            Text(
                                text = "Add question", color = Color.Blue
                            )
                        }
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = VividBlue,
                titleContentColor = Color.White,
            )
        )

    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .padding(it)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex, divider = {
//                    Divider(color = Color.Yellow, thickness = 2.dp)
            }) {
                tabItems.forEachIndexed { index, item ->
                    Tab(modifier = Modifier.background(VividBlue),
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
                        })
                }
            }
            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) {
                tabItems[it].screen()
            }
        }
    }
}
