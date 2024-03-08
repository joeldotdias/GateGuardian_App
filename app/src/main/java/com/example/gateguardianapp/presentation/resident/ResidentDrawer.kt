package com.example.gateguardianapp.presentation.resident

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gateguardianapp.domain.model.User
import com.example.gateguardianapp.presentation.components.CustomerSupportButtons
import com.example.gateguardianapp.presentation.navigation.ResidentNavigation
import com.example.gateguardianapp.presentation.navigation.ResidentScreens
import com.example.gateguardianapp.presentation.resident.profile.ResidentProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResidentDrawer(
    user: User,
    onSignOut: () -> Unit,
    viewModel: ResidentProfileViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val residentData = viewModel.state.collectAsState().value.resident

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = currentBackStackEntry?.destination?.route ?: ResidentScreens.Dashboard

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val screens = listOf(
        ResidentScreens.Dashboard,
        ResidentScreens.Visitors,
        ResidentScreens.Regulars,
        ResidentScreens.Notices,
        ResidentScreens.Profile,
        ResidentScreens.Admin
    )

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                // Header
                Spacer(modifier = Modifier.height(20.dp))
                DrawerHeader(user.email, user.name, residentData?.pfpUrl)
                HorizontalDivider(thickness = 2.dp)
                Spacer(modifier = Modifier.height(20.dp))

                // Drawer items
                screens.forEach { screen ->
                    if(screen.title == "Admin" && user.category.lowercase() != "admin") return@forEach

                    NavigationDrawerItem(
                        label = { Text(text = screen.title) },
                        selected = currentRoute == screen.route,
                        icon = {
                            Icon(imageVector = screen.icon,
                                contentDescription = "${screen.title} icon"
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                            }
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                Text(
                    text = "GateGuardian v1.0",
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentRoute.toString().replaceFirstChar { it.uppercase() }) },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "menu icon"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                )
            },
            floatingActionButton = {
                CustomerSupportButtons()
            }
        ) { topBarPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topBarPadding.calculateTopPadding())
            ) {
                residentData?.let {
                    ResidentNavigation(
                        navController = navController,
                        resident = residentData,
                        onResidentChange = viewModel::getProfileDetails,
                        signOut = onSignOut
                    )
                }
            }
        }
    }
}


@Composable
fun DrawerHeader(email: String, name: String, pfpUrl: String?) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.36f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.padding(top = 30.dp, bottom = 17.dp)
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(width = 1.dp, color = Color.LightGray)
            ) {
                pfpUrl?.let {
                    AsyncImage(
                        model = it.toUri(),
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Pfp"
                    )
                } ?: Image(
                    imageVector = Icons.Rounded.AccountCircle,
                    modifier = Modifier.size(120.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF59EBDD)),
                    contentDescription = "Default Pfp icon"
                )
            }
        }
        Text(
            text = name,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = email)
    }
}