package com.example.gateguardianapp.presentation.security.logs

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val visitorLogsData = viewModel.state.value.visitorLogs

        visitorLogsData?.let { visitorLogs ->
            Log.d("Cloudfire", "LogsScreen: $visitorLogs")
            items(items = visitorLogs) { visitorLog ->
                Text(text = visitorLog.toString())
            }
        }
    }
}