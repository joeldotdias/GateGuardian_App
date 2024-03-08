package com.example.gateguardianapp.presentation.security.regular

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.data.remote.schema.RegularsSchema

@Composable
fun SecurityRegularsScreen(
    viewModel: RegularCheckViewModel = hiltViewModel()
) {
    val regularsData = viewModel.state.collectAsState().value.regulars

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(top = 8.dp, start = 12.dp, end = 12.dp)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .background(color = Color(0xFF94F1E9)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Regulars",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            regularsData?.let { regulars ->
                items(regulars) { regular ->
                    CheckRegularCard(regular)
                }
            }
        }
    }
}

@Composable
fun CheckRegularCard(regular: RegularsSchema) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = regular.name,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(10.dp),
        )
        Card(
            colors = CardDefaults.cardColors(Color(0xFF2196F3))
        ) {
            Text(
                text = regular.role,
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.WatchLater,
                contentDescription = "Time icon"
            )
            Text(
                text = "Check in time: ${regular.entry}",
                fontWeight = FontWeight.Light
            )
        }

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Code,
                contentDescription = "Entry Code icon"
            )
            Text(
                text = "Code: ${regular.code}",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}