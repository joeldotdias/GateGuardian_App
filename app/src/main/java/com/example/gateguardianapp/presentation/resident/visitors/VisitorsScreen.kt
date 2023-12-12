package com.example.gateguardianapp.presentation.resident.visitors

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddIcCall
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto
import com.example.gateguardianapp.presentation.resident.components.InputForm
import com.example.gateguardianapp.util.Delays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VisitorsScreen(
    viewModel: VisitorsViewModel = hiltViewModel(),
    onVisitorsDataChange: () -> Unit = viewModel::getVisitorsByResident
) {

    val visitorsData = viewModel.state.value.visitors

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    var showPastVisitors by rememberSaveable { mutableStateOf(false) }
    var showVisitorOtp by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputForm(
            value = name,
            label = "Name",
            onValChange = { name = it },
            icon = Icons.Rounded.Person,
            onImeAction = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        InputForm(
            value = phoneNo,
            label = "Phone number",
            onValChange = { phoneNo = it },
            icon = Icons.Rounded.AddIcCall,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            onImeAction = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    name = ""
                    phoneNo = ""
                    focusManager.clearFocus()
                }
            ) {
                Text(text = "Clear")
            }

            Button(
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.saveVisitor(name, phoneNo)
                        focusManager.clearFocus()
                        delay(Delays.CLOUD_UPLOAD_DELAY)
                        onVisitorsDataChange()
                        generatedOtp = viewModel.getRecentVisitorOtp().toString()
                        delay(80L)
                        showVisitorOtp = true
                    }
                }
            ) {
                Text(text = "Add")
            }
        }

        AnimatedVisibility(visible = showVisitorOtp) {
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
                                    context.shareOtp(generatedOtp, phoneNo)
                                    delay(150L)
                                    name = ""
                                    phoneNo = ""
                                    showVisitorOtp = false
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


        Button(
            modifier = Modifier.padding(top = 15.dp, bottom = 12.dp),
            onClick = { showPastVisitors = !showPastVisitors }
        ) {
            Text(text = "My Visitors")
        }

        AnimatedVisibility(visible = showPastVisitors) {
            visitorsData?.let { visitors ->
                LazyColumn(
                    contentPadding = PaddingValues(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = visitors) { visitor ->
                        VisitorRow(
                            visitor = visitor,
                            shareVisitorOtp = { otp, phoneNo ->
                                context.shareOtp(otp, phoneNo)
                            }
                        )
                    }
                }
            }
        }
    }
}


fun Context.shareOtp(otp: String, phoneNo: String) {
    try {
        val shareOtpIntent = Intent(Intent.ACTION_SEND).apply {
            this.type = "text/plain"
            this.`package` = "com.whatsapp"
            this.putExtra(Intent.EXTRA_TEXT, "Hi! Here's your code to come in: $otp")
            this.putExtra(Intent.EXTRA_PHONE_NUMBER, "+91 $phoneNo")
        }
        startActivity(shareOtpIntent)
    } catch(e: Exception) {
        //
    }
}

@Composable
fun VisitorRow(
    visitor: VisitorResidentDto,
    shareVisitorOtp: (String, String) -> Unit
) {

    var isOtpVisible by remember { mutableStateOf(false) }

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
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Person icon"
                )
                Text(text = visitor.name)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Phone,
                    contentDescription = "Phone number icon"
                )
                Text(text = visitor.phoneNo)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        isOtpVisible = !isOtpVisible
                    }
                ) {
                    Text(text = if(!isOtpVisible) "Show OTP" else "Hide OTP")
                }
                AnimatedVisibility(visible = isOtpVisible) {
                    TextField(
                        value = visitor.otp,
                        readOnly = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 18.dp, end = 12.dp),
                        colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    shareVisitorOtp(visitor.otp, visitor.phoneNo)
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
}