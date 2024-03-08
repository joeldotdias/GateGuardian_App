package com.example.gateguardianapp.presentation.resident.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.CrisisAlert
import androidx.compose.material.icons.rounded.CurrencyRupee
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.TransferWithinAStation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto
import com.example.gateguardianapp.presentation.resident.visitors.shareCode

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val profile = viewModel.state.collectAsState().value.dashDetails
    val visitorsData = viewModel.state.collectAsState().value.visitors
    val noticesData = viewModel.state.collectAsState().value.notices

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEAEAEA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            // Greeting row
            Row(
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = "Hi ${profile?.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                    Text(
                        text = "Building: ${profile?.building}",
                        modifier = Modifier.padding(top = 5.dp),
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "Flat no: ${profile?.flatNo}",
                        modifier = Modifier.padding(top = 5.dp),
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                    )
                }
                AsyncImage(
                    model = profile?.pfpUrl,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Pfp"
                )
            }

            Spacer(modifier = Modifier.height(23.dp))

            Text(
                text = "Upcoming Visitors",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5C73F3)
            )
            VisitorsCard(visitorsData)

            Spacer(modifier = Modifier.height(23.dp))

            Text(
                text = "Notice Board",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF569892)
            )
            NoticeBoard(noticesData)
        }
    }
}

@Composable
fun VisitorsCard(visitorsData: List<VisitorResidentDto>?) {
    val context = LocalContext.current

    Card(
        elevation = CardDefaults.cardElevation(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color(0xFFAFD6F6)),
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(6.dp)
        ) {
            visitorsData?.let { visitors ->
                items(visitors) { visitor ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(imageVector = Icons.Rounded.TransferWithinAStation, contentDescription = "Visitor icon")
                            Text(
                                text = visitor.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        }

                        Card(
                            modifier = Modifier
                                .padding(vertical = 5.dp, horizontal = 8.dp)
                                .clickable { context.shareCode(visitor.code) },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF59A7E5)),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(imageVector = Icons.Rounded.Share, contentDescription = "Share")

                                Text(
                                    text = "Code",
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoticeBoard(noticesData: List<NoticeDto>?) {
    val cutCornerSize = 30.dp
    val cornerRadius = 10.dp

    Box (
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
    ){
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(0xFFB0ECE6),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(0xB0ECE6, 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            LazyColumn {
                noticesData?.let { notices ->
                    items(notices) { notice ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(31.dp),
                                imageVector = iconFromCategory(notice.category),
                                contentDescription = "Notice Category icon"
                            )
                            Column {
                                Text(
                                    text = notice.title,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = notice.body,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                } ?: item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nothing's going on for you",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

fun iconFromCategory(category: String): ImageVector {
    return when(category.lowercase()) {
        "alert" -> Icons.Rounded.CrisisAlert
        "reminder" -> Icons.Rounded.Notifications
        "bill" -> Icons.Rounded.CurrencyRupee
        else -> Icons.Rounded.Campaign
    }
}