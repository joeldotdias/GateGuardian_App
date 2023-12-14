package com.example.gateguardianapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gateguardianapp.presentation.security.logs.LogsScreen
import com.example.gateguardianapp.presentation.security.notify.NotifyScreen
import com.example.gateguardianapp.presentation.security.profile.SecurityProfileScreen
import com.example.gateguardianapp.presentation.security.verify.VerifyScreen
import com.example.gateguardianapp.presentation.security.verify.VerifyViewModel

@Composable
fun SecurityNavigation(
    navController: NavHostController,
    signOut: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = SecurityScreens.Verify.route
    ) {

        composable(route = SecurityScreens.Verify.route) {
            val verifyViewModel: VerifyViewModel = hiltViewModel()
            VerifyScreen(verifyViewModel, verifyViewModel::getVisitors)
        }

        composable(route = SecurityScreens.Notify.route) {
            NotifyScreen()
        }

        composable(route = SecurityScreens.Logs.route) {
            LogsScreen()
        }

        composable(route = SecurityScreens.Profile.route) {
            SecurityProfileScreen(signOut)
        }
    }
}