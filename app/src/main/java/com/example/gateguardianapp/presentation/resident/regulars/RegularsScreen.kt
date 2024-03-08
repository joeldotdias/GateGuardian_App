package com.example.gateguardianapp.presentation.resident.regulars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.data.remote.schema.RegularsSchema
import com.example.gateguardianapp.presentation.components.InputForm
import com.example.gateguardianapp.presentation.resident.visitors.shareCode
import com.example.gateguardianapp.util.Delays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegularsScreen(
    viewModel: RegularsViewModel = hiltViewModel(),
    onRegularsDataChange: () -> Unit = viewModel::getRegularsByResident
) {
    val regularsData = viewModel.state.collectAsState().value.regulars

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    var showRegularOtp by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var entry by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }

    var showTimePicker by remember { mutableStateOf(false) }
    var pickedTime by remember { mutableStateOf("") }
    val timeState = rememberTimePickerState(is24Hour = false)
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

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
                    text = "Add a regular",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            InputForm(
                value = name,
                label = "Name",
                onValChange = { name = it },
                leadingIcon = Icons.Rounded.Person,
                onImeAction = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            InputForm(
                value = role,
                label = "Role",
                onValChange = { role = it },
                leadingIcon = Icons.Rounded.Category,
                onImeAction = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { showTimePicker = true }) {
                    Text(text = "Set Time")
                }
                Text(
                    text = pickedTime,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if(showTimePicker) {
                TimePickerDialog(
                    onCancel = { showTimePicker = false },
                    onConfirm = {
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, timeState.hour)
                        cal.set(Calendar.MINUTE, timeState.minute)
                        cal.isLenient = false
                        pickedTime = formatter.format(cal.time)
                        showTimePicker = false
                    }
                ) {
                    TimePicker(state = timeState)
                }
            }
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
                        name = ""
                        role = ""
                        entry = ""
                        generatedOtp = ""
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
                            viewModel.saveRegular(name, role, pickedTime)
                            focusManager.clearFocus()
                            delay(Delays.CLOUD_UPLOAD_DELAY)
                            onRegularsDataChange()
                            generatedOtp = viewModel.getRecentRegularOtp().toString()
                            delay(80L)
                            showRegularOtp = true
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Save",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }

            AnimatedVisibility(visible = showRegularOtp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Your ComeIn code")
                    TextField(
                        value = generatedOtp,
                        readOnly = true,
                        modifier = Modifier.padding(start = 18.dp),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        context.shareCode(generatedOtp)
                                        delay(150L)
                                        name = ""
                                        role = ""
                                        entry = ""
                                        showRegularOtp = false
                                        generatedOtp = ""
                                        focusManager.clearFocus()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Share,
                                    contentDescription = "Share otp icon"
                                )
                            }
                        },
                        onValueChange = {}
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(18.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.18f)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(color = Color(0xFF94F1E9)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My regulars",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                regularsData?.let { regulars ->
                    items(regulars) { regular ->
                        RegularRow(
                            regular = regular,
                            shareVisitorOtp = { otp ->
                                context.shareCode(otp)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun RegularRow(
    regular: RegularsSchema,
    shareVisitorOtp: (String) -> Unit
) {
    var isOtpVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
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

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(Color(0xFF9AE7F0)),
                onClick = { isOtpVisible = !isOtpVisible }
            ) {
                Text(text = if(!isOtpVisible) "View Code" else "Hide Code")
            }
            AnimatedVisibility(visible = isOtpVisible) {
                TextField(
                    value = regular.code,
                    readOnly = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 18.dp, end = 12.dp),
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                shareVisitorOtp(regular.code)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Share,
                                contentDescription = "Share otp icon"
                            )
                        }
                    },
                    onValueChange = {}
                )
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
