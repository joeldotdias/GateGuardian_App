package com.example.gateguardianapp.presentation.security.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.DoorFront
import androidx.compose.material.icons.rounded.TransferWithinAStation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.domain.model.security.VisitorLog
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
            val visitorLogsData = viewModel.state.value.visitorLogs

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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(18.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Rounded.TransferWithinAStation,
                    contentDescription = "Person icon"
                )
                Text(
                    text = visitorLog.name,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Rounded.DoorFront,
                    contentDescription = "Door icon"
                )
                Text(text = "${visitorLog.hostFlat}, ${visitorLog.hostBuilding}")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = "Entry date icon"
                )
                Text(text = entryTime[0])
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccessTime,
                    contentDescription = "Entry time icon"
                )
                Text(text = entryTime[1])
            }
        }
    }
}

fun getIstDateTime(dateStr: String): String {
    val dateFmt = "yyyy-MM-dd HH:mm:ss"
    val dateTimeZone = TimeZone.getTimeZone("UTC")
    val parser = SimpleDateFormat(dateFmt, Locale.getDefault())
    parser.timeZone = dateTimeZone

    val parsedDate = parser.parse(dateStr.replace('T', ' ').replace("Z", ""))

    val currTimeZone = TimeZone.getDefault()
    val targetFmt = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    targetFmt.timeZone = currTimeZone

    return parsedDate?.let {
        targetFmt.format(it)
    } ?: ""
}