package com.example.gateguardianapp.presentation.security.verify

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.DoorFront
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.TransferWithinAStation
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gateguardianapp.domain.model.security.VisitorSecurityDto
import com.example.gateguardianapp.presentation.resident.components.InputForm
import com.example.gateguardianapp.util.Delays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun VerifyScreen(
    viewModel: VerifyViewModel = hiltViewModel(),
    onVisitorsDataChange: () -> Unit = viewModel::getVisitors
) {

    val refreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val visitorsData = viewModel.visitors.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = onVisitorsDataChange)
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchedVisitorId by remember { mutableIntStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(state = pullRefreshState)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            visitorsData.value?.let { visitors ->

                var visitorSearchResults by remember { mutableStateOf(visitorsData.value!!) }

                DockedSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        coroutineScope.launch(Dispatchers.Main) {
                            delay(500L)
                            visitorSearchResults = visitors.filter { visitor ->
                                visitor.name.startsWith(prefix = searchQuery, ignoreCase = true)
                            }
                        }
                    },
                    onSearch = {},
                    active = isSearchActive,
                    onActiveChange = { isSearchActive = it },
                    placeholder = { Text(text = "Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search visitor icon"
                        )
                    },
                    trailingIcon = {
                        if(isSearchActive) {
                            IconButton(
                                onClick = {
                                    visitorSearchResults = visitors
                                    if(searchQuery.isNotEmpty()) searchQuery = ""
                                    else isSearchActive = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cancel search / Close search bar icon"
                                )
                            }
                        }
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.72f)
                    ) {
                        items(items = visitorSearchResults) { visitor ->
                            VisitorSearchResult(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .clickable {
                                        coroutineScope.launch(Dispatchers.Main) {
                                            isSearchActive = false
                                            searchedVisitorId = visitor.visitorId
                                            scrollState.animateScrollToItem(
                                                getSearchResultIndex(
                                                    visitorIds = visitors.map { it.visitorId },
                                                    target = visitor.visitorId
                                                )
                                            )
                                        }
                                    },
                                name = visitor.name,
                                flatNo = visitor.hostFlat,
                                building = visitor.hostBuilding
                            )
                        }
                    }
                }

                LazyColumn(
                    state = scrollState
                ) {

                    items(items = visitors) { visitor ->
                        SecurityVisitorRow(
                            visitor = visitor,
                            isHighlighted = searchedVisitorId == visitor.visitorId,
                            stopHighlighting = { searchedVisitorId = -1 },
                            verifyCode = { codeToVerify ->
                                var isVisitorVerified = false
                                if(codeToVerify == visitor.otp) {
                                    isVisitorVerified = true
                                    searchedVisitorId = -1
                                }
                                return@SecurityVisitorRow isVisitorVerified
                            },
                            moveVerifiedVisitorToLogs = {
                                coroutineScope.launch {
                                    viewModel.moveVerifiedVisitorToLogs(visitor.visitorId)
                                    delay(Delays.CLOUD_UPLOAD_DELAY)
                                }
                            }
                        )
                    }
//                    itemsIndexed(items = visitors) { index, visitor ->
//                        SecurityVisitorRow(
//                            visitor = visitor,
//                            isHighlighted = searchedVisitorId == visitor.visitorId,
//                            stopHighlighting = { searchedVisitorId = -1 },
//                            verifyCode = { codeToVerify ->
//                                var isVisitorVerified = false
//                                if(codeToVerify == visitor.otp) {
//                                    isVisitorVerified = true
//                                    searchedVisitorId = -1
//                                }
//                                return@SecurityVisitorRow isVisitorVerified
//                            },
//                            moveVerifiedVisitorToLogs = {
//                                coroutineScope.launch {
//                                    viewModel.moveVerifiedVisitorToLogs(visitor.visitorId)
//                                    delay(Delays.CLOUD_UPLOAD_DELAY)
//                                }
//                            }
//                        )
//                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun SecurityVisitorRow(
    visitor: VisitorSecurityDto,
    isHighlighted: Boolean,
    stopHighlighting: () -> Unit,
    verifyCode: (String) -> Boolean,
    moveVerifiedVisitorToLogs: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var codeToVerify by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(18.dp))
            .background(if (isHighlighted) Color(0xFFB4F7B7) else Color.Transparent)
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
                    text = visitor.name,
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
                Text(text = "${visitor.hostFlat}, ${visitor.hostBuilding}")
            }

            AnimatedVisibility(visible = !isExpanded && visitor.isVerified == null) {
                Button(
                    onClick = { isExpanded = true }
                ) {
                    Text(text = "Verify")
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InputForm(
                        value = codeToVerify,
                        label = "ComeIn code",
                        onValChange = { codeToVerify = it },
                        leadingIcon = Icons.Rounded.Password,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        onImeAction = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                                if(codeToVerify.isNotEmpty()) {
                                    codeToVerify = ""
                                } else {
                                    stopHighlighting()
                                    isExpanded = false
                                }
                            }
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            onClick = {
                                visitor.isVerified = codeToVerify.trim() == visitor.otp
                                if(visitor.isVerified!!) {
                                    moveVerifiedVisitorToLogs()
                                }
                                isExpanded = false
                            }
                        ) {
                            Text(text = "Verify")
                        }
                    }
                }
            }

            visitor.isVerified?.let { isVisitorVerified ->
                AnimatedVisibility(visible = isVisitorVerified != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if(isVisitorVerified) "Verified!" else "Oopsie daisy",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 35.sp,
                            color = if(isVisitorVerified!!) Color.Green else Color.Red
                        )

                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = stopHighlighting
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        modifier = Modifier.size(30.dp),
                        contentDescription = "Hide visitor verification form icon"
                    )
                }
            }
        }
    }
}

@Composable
fun VisitorSearchResult(
    modifier: Modifier = Modifier,
    name: String,
    flatNo: Int,
    building: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.TransferWithinAStation,
                    contentDescription = "Visitor icon"
                )
                Text(
                    text = name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.DoorFront,
                    contentDescription = "Door icon"
                )
                Text(
                    text = "$flatNo, $building",
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

fun getSearchResultIndex(
    visitorIds: List<Int>,
    target: Int
): Int {
    var low = 0
    var high = visitorIds.size - 1

    do {
        val mid = low + (high - low) / 2
        val midVal = visitorIds[mid]

        if(midVal == target) {
            return mid
        } else if(midVal > target) {
            high = mid - 1
        } else {
            low = mid + 1
        }
    } while(low <= high)

    return -1
}