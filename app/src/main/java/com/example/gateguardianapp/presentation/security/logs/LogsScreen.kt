package com.example.gateguardianapp.presentation.security.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
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
import com.example.gateguardianapp.domain.model.security.VisitorLog

@Composable
fun LogsScreen(viewModel: LogsViewModel = hiltViewModel()) {
    val visitorLogsData = viewModel.state.collectAsState().value.visitorLogs

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
                text = "Logs",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            visitorLogsData?.let { visitorLogs ->
                items(items = visitorLogs) { visitorLog ->
                    VisitorLogCard(visitorLog)
                }
            }
        }
    }
}

@Composable
fun VisitorLogCard(visitorLog: VisitorLog) {
    val entryTime = getIstDateTime(visitorLog.entry).split(" ")

    Card(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7FAF8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = visitorLog.name,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Entry date icon"
                    )
                    Text(text = "Date: ${entryTime[0]}")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccessTime,
                        contentDescription = "Entry time icon"
                    )
                    Text(text = " Checked in: ${entryTime[1]}")
                }

            }

            Card(
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .width(100.dp)
                    .align(Alignment.CenterVertically),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDEF8F6)),
                elevation = CardDefaults.cardElevation(2.dp)

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(color = Color(0xFFB0F0F8)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Flat",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }

                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = visitorLog.hostBuilding,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${visitorLog.hostFlat}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
