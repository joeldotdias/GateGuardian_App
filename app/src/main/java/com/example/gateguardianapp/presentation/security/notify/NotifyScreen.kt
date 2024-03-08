package com.example.gateguardianapp.presentation.security.notify

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.Doorbell
import androidx.compose.material.icons.rounded.PhoneInTalk
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.presentation.components.InputForm
import com.example.gateguardianapp.presentation.components.dialCustomerSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NotifyScreen(
    viewModel: NotifyViewModel = hiltViewModel()
) {
    val callablesData = viewModel.state.collectAsState().value.callables
//    var callablesData: List<NotifyResidentsDto>? = null

    var flatNo by remember { mutableStateOf("") }
    var building by remember { mutableStateOf("") }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(18.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(color = Color(0xFF94F1E9)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notify",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            InputForm(
                value = flatNo,
                label = "Flat No",
                onValChange = { flatNo = it },
                leadingIcon = Icons.Rounded.Doorbell,
                keyboardType = KeyboardType.Number,
                onImeAction = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            InputForm(
                value = building,
                label = "Building",
                onValChange = { building = it },
                leadingIcon = Icons.Rounded.Apartment,
                imeAction = ImeAction.Done,
                onImeAction = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    colors = CardDefaults.cardColors(Color.Red),
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                    onClick = {
                        flatNo = ""
                        building = ""
                        viewModel.clearCallables()
                        focusManager.clearFocus()
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Clear",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(Color(0xFFA691CD)),
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            viewModel.getNumbersToNotify(flatNo, building)
                            focusManager.clearFocus()
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fetch",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = callablesData != null) {
            callablesData?.let { callables ->
                Card(
                    modifier = Modifier.padding(12.dp),
                    shape = RoundedCornerShape(22.dp),
                    elevation = CardDefaults.cardElevation(18.dp),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.26f)
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                            .background(color = Color(0xFF94F1E9)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Contacts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    LazyColumn {
                        items(callables) { callable ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(end = 6.dp),
                                    imageVector = Icons.Outlined.AccountCircle,
                                    contentDescription = "Person icon"
                                )
                                Text(
                                    text = callable.name,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Card(
                                    modifier = Modifier.padding(start = 10.dp),
                                    colors = CardDefaults.cardColors(Color(0xFFA3DCF6)),
                                    onClick = {
                                        context.dialCustomerSupport(callable.phoneNo)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(5.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.PhoneInTalk,
                                            contentDescription = "Phone icon"
                                        )
                                        Text(text = "Call")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}