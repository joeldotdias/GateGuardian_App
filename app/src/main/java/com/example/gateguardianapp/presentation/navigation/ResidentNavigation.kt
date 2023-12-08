package com.example.gateguardianapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.presentation.resident.admin.AdminScreen
import com.example.gateguardianapp.presentation.resident.dashboard.DashboardScreen
import com.example.gateguardianapp.presentation.resident.events.EventsScreen
import com.example.gateguardianapp.presentation.resident.notices.NoticesScreen
import com.example.gateguardianapp.presentation.resident.profile.ResidentProfileScreen
import com.example.gateguardianapp.presentation.resident.profile.ResidentProfileViewModel
import com.example.gateguardianapp.presentation.resident.regulars.RegularsScreen
import com.example.gateguardianapp.presentation.resident.visitors.VisitorsScreen
import com.example.gateguardianapp.presentation.resident.visitors.VisitorsViewModel

@Composable
fun ResidentNavigation(
    navController: NavHostController,
    resident: Resident,
    onResidentChange: () -> Unit,
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
            val visitorsViewModel:VisitorsViewModel = hiltViewModel()
            VisitorsScreen(visitorsViewModel, visitorsViewModel::getVisitorsByResident)
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
            val residentProfileViewModel: ResidentProfileViewModel = hiltViewModel()
            ResidentProfileScreen(resident, onResidentChange, signOut, residentProfileViewModel)
        }

        composable(route = ResidentScreens.Admin.route) {
            AdminScreen(resident)
        }
    }
}