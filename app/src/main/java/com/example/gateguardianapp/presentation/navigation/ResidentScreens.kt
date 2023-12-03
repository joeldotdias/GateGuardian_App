package com.example.gateguardianapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TransferWithinAStation
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ResidentScreens(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    data object Dashboard: ResidentScreens(
        title = "Dashboard",
        route = "dashboard",
        icon = Icons.Filled.Speed
    )

    data object Visitors: ResidentScreens(
        title = "Visitors",
        route = "visitors",
        icon = Icons.Filled.TransferWithinAStation
    )

    data object Regulars: ResidentScreens(
        title = "Regulars",
        route = "regulars",
        icon = Icons.Filled.WatchLater
    )

    data object Events: ResidentScreens(
        title = "Events",
        route = "events",
        icon = Icons.Filled.StarBorder
    )

    data object Notices: ResidentScreens(
        title = "Notices",
        route = "notices",
        icon = Icons.Filled.Campaign
    )

    data object Profile: ResidentScreens(
        title = "Profile",
        route = "profile",
        icon = Icons.Filled.AccountCircle
    )

    data object Admin: ResidentScreens(
        title = "Admin",
        route = "admin",
        icon = Icons.Filled.AdminPanelSettings
    )
}