package com.example.gateguardianapp.presentation.security.verify

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.log

@Composable
fun VerifyScreen(
    viewModel: VerifyViewModel = hiltViewModel(),
    onVisitorsDataChange: () -> Unit = viewModel::getVisitors
) {
    val visitorsData = viewModel.state.value.visitors

    
    visitorsData?.let { visitors ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = visitors) { visitor ->
                Text(text = visitor.name)
            }
        }
    }
}