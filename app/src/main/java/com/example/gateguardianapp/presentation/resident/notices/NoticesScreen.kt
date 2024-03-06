package com.example.gateguardianapp.presentation.resident.notices

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.data.remote.dto.NoticeDto

@Composable
fun NoticesScreen(
    viewModel: NoticesViewModel = hiltViewModel()
) {
    val noticesData = viewModel.state.collectAsState().value.notices

    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        noticesData?.let { notices ->
            items(notices) { notice ->
                NoticeCard(notice)
            }
        }
    }
}

@Composable
fun NoticeCard(notice: NoticeDto) {

}