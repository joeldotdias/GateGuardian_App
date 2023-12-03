package com.example.gateguardianapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gateguardianapp.presentation.resident.admin.AdminScreen
import com.example.gateguardianapp.presentation.resident.dashboard.DashboardScreen
import com.example.gateguardianapp.presentation.resident.events.EventsScreen
import com.example.gateguardianapp.presentation.resident.notices.NoticesScreen
import com.example.gateguardianapp.presentation.resident.profile.ResidentProfileScreen
import com.example.gateguardianapp.presentation.resident.regulars.RegularsScreen
import com.example.gateguardianapp.presentation.resident.visitors.VisitorsScreen

@Composable
fun ResidentNavigation(
    navController: NavHostController,
    email: String,
    signOut: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = ResidentScreens.Dashboard.route
    ) {
        composable(route = ResidentScreens.Dashboard.route) {
            DashboardScreen()
        }

        composable(route = ResidentScreens.Visitors.route) {
            VisitorsScreen()
        }

        composable(route = ResidentScreens.Regulars.route) {
            RegularsScreen()
        }

        composable(route = ResidentScreens.Events.route) {
            EventsScreen()
        }

        composable(route = ResidentScreens.Notices.route) {
            NoticesScreen()
        }

        composable(route = ResidentScreens.Profile.route) {
            ResidentProfileScreen(email, signOut)
        }

        composable(route = ResidentScreens.Admin.route) {
            AdminScreen()
        }
    }
}