package com.example.gateguardianapp.presentation.resident.admin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val tabScreens = listOf(AdminScreens.People, AdminScreens.Notices)

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState( pageCount = { tabScreens.size } )

    LaunchedEffect(key1 = selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
        if(!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(selectedTabIndex) {
            tabScreens.forEachIndexed { index, adminScreen -> 
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(text = adminScreen.title)
                    },
                    icon = {
                        Icon(
                            imageVector = if(index == selectedTabIndex) adminScreen.selectedIcon else adminScreen.unselectedIcon,
                            contentDescription = "${adminScreen.title} icon"
                        )
                    }
                )
            }
        }
        
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.Top
        ) { index ->
            when(index) {
                0 -> AdminPeopleScreen(viewModel = viewModel, onPeopleChange = viewModel::getAdminScreenDetails)
                1 -> AdminNoticesScreen(viewModel, viewModel::getAdminScreenDetails)
            }
        }
    }
}