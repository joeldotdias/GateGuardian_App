package com.example.gateguardianapp.presentation.resident.notices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.gateguardianapp.data.mapper.getIstDateTime
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.presentation.resident.dashboard.iconFromCategory

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
    val posted = getIstDateTime(notice.posted).split(" ")

    Card(
        modifier = Modifier.padding(vertical = 8.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(color = colorFromCategory(notice.category)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 10.dp),
                imageVector = iconFromCategory(notice.category),
                contentDescription = "Notice icon"
            )
            Text(
                text = notice.category,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = notice.title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = posted[0],
                    fontSize = 13.sp
                )
                Text(
                    text = " at ${posted[1]}",
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp
                )
            }
            Text(
                text = notice.body,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

private fun colorFromCategory(category: String): Color {
    return when(category.lowercase()) {
        "alert" -> Color(0xFFF68F8F)
        "bill" -> Color(0xFFF0DEA8)
        "reminder" -> Color(0xFFA3F4A3)
        else -> Color(0xFFC3ADE9)
    }
}