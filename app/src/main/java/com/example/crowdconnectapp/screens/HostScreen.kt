package com.example.crowdconnectapp.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.ManageHistory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val route:String
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostScreen(navController: NavHostController) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val items = listOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.Filled.HomeWork,
            unselectedIcon = Icons.Filled.HomeWork,
            hasNews = false,
            route = "hostDashboard"
        ),
        BottomNavigationItem(
            title = "History",
            selectedIcon = Icons.Filled.ManageHistory,
            unselectedIcon = Icons.Filled.ManageHistory,
            hasNews = false,
            badgeCount = 1,
            route = "history"
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Filled.Settings,
            hasNews = true,
            route = "settings"
        ),
    )
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.height(70.dp), // Adjust the height as needed
                    content = {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItemIndex == index,
                                onClick = {
                                    selectedItemIndex = index
                                },
                                label = {
//                                    if (selectedItemIndex == index) {
//                                        Text(
//                                            text = item.title,
//                                            style = MaterialTheme.typography.labelSmall
//                                        )
//                                    }
                                },
                                alwaysShowLabel = false,
                                icon = {
                                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex){
                                            item.selectedIcon
                                        }else item.unselectedIcon,
                                        contentDescription = item.title,
                                        modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally) // Adjust the icon size as needed
                                    )
                                    if (selectedItemIndex == index) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    }
                                },
                                colors = androidx.compose.material3.NavigationBarItemDefaults
                                    .colors(
                                        indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(LocalAbsoluteTonalElevation.current)
                                    ))
                        }
                    }
                )
            },
            content = {
                // Use the NavHostController to navigate to the appropriate screen
                // based on the selected bottom navigation item.
                when(selectedItemIndex) {
                    0 -> { HostDashboard(navController) }
                    1 -> { Text(text = "History Screen") }
                    2 ->  { Text(text = "Settings Screen") }
                }
            }
        )
    }
}