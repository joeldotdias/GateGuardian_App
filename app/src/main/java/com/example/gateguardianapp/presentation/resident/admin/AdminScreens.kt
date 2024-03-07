package com.example.gateguardianapp.presentation.resident.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminScreens(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {

    data object People: AdminScreens(
        title = "People",
        unselectedIcon = Icons.Outlined.Groups,
        selectedIcon = Icons.Filled.Groups
    )

    data object Notices: AdminScreens(
        title = "Notices",
        unselectedIcon = Icons.Outlined.StarBorder,
        selectedIcon = Icons.Filled.Star
    )
}