package com.example.crowdconnectapp.screens.attendee

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.RunningWithErrors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.crowdconnectapp.models.AuthViewModel
import com.example.crowdconnectapp.screens.host.BottomNavigationItem
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AttendeeScreen(authViewModel: AuthViewModel,navController: NavHostController,) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val items = listOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.Filled.HomeWork,
            unselectedIcon = Icons.Filled.HomeWork,
            hasNews = false,
            route = "attendeeDashboard"
        ),
        BottomNavigationItem(
            title = "Recents",
            selectedIcon = Icons.Filled.Assessment,
            unselectedIcon = Icons.Filled.Assessment,
            hasNews = false,
            badgeCount = 1,
            route = "recents"
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
                    modifier = Modifier.height(70.dp),
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
                                colors = NavigationBarItemDefaults
                                    .colors(
                                        indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                            LocalAbsoluteTonalElevation.current)
                                    ))
                        }
                    }
                )
            },
            content = {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val attendeeId = currentUser?.phoneNumber ?: ""
                when(selectedItemIndex) {
                    0 -> { AttendeeDashboard(navController,authViewModel) }
                    1 -> { RecentsSessions(attendeeId,authViewModel,navController) }
                    2 ->  { Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RunningWithErrors,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                        )
                        Text(text = "Feature Under Development")
                    } }
                }
            }
        )
    }
}