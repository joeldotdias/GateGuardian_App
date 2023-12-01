package com.example.gateguardianapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SecurityScreens(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Verify: SecurityScreens(
        title = "Verify",
        route = "verify",
        selectedIcon = Icons.Filled.Verified,
        unselectedIcon = Icons.Outlined.Verified
    )

    data object Notify: SecurityScreens(
        title = "Notify",
        route = "notify",
        selectedIcon = Icons.Filled.NotificationsActive,
        unselectedIcon = Icons.Outlined.NotificationsActive
    )

    data object Logs: SecurityScreens(
        title = "Logs",
        route = "logs",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook
    )
}