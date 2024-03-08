package com.example.gateguardianapp.presentation.security

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gateguardianapp.domain.model.User
import com.example.gateguardianapp.presentation.navigation.SecurityNavigation
import com.example.gateguardianapp.presentation.navigation.SecurityScreens

@Composable
fun SecurityBottomBar(
    user: User,

    onSignOut: () -> Unit,
) {

    val navController = rememberNavController()

    val screens = listOf(
        SecurityScreens.Verify,
        SecurityScreens.Regulars,
        SecurityScreens.Notify,
        SecurityScreens.Logs,
        SecurityScreens.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination


    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                            }
                        },
                        label = { Text(text = screen.title) },
                        icon = {
                            Icon(
                                imageVector = if(currentDestination?.route == screen.route) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = "${screen.title} icon"
                            )
                        }
                    )
                }
            }
        }
    ) { bottomBarPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomBarPadding.calculateBottomPadding())
        ) {
            SecurityNavigation(
                navController = navController,
                signOut = onSignOut
            )
        }
    }
}