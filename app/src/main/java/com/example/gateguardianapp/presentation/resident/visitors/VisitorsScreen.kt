package com.example.gateguardianapp.presentation.resident.visitors

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.gateguardianapp.domain.model.resident.VisitorResidentDto
import com.example.gateguardianapp.presentation.components.InputForm
import com.example.gateguardianapp.util.Delays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VisitorsScreen(
    viewModel: VisitorsViewModel = hiltViewModel(),
    onVisitorsDataChange: () -> Unit = viewModel::getVisitorsByResident
) {

    val visitorsData = viewModel.state.collectAsState().value.visitors

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    var showVisitorOtp by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }

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
                    text = "Add a visitor",
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
                value = phoneNo,
                label = "Phone number",
                onValChange = { phoneNo = it },
                leadingIcon = Icons.Rounded.AddIcCall,
                keyboardType = KeyboardType.Number,
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
                        .fillMaxWidth(0.5f)
                        .clickable {
                            name = ""
                            phoneNo = ""
                            focusManager.clearFocus()
                        },
                    colors = CardDefaults.cardColors(Color.Red),
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.saveVisitor(name, phoneNo)
                                focusManager.clearFocus()
                                delay(Delays.CLOUD_UPLOAD_DELAY)
                                onVisitorsDataChange()
                                generatedOtp = viewModel
                                    .getRecentVisitorOtp()
                                    .toString()
                                delay(80L)
                                showVisitorOtp = true
                            }
                        },
                    colors = CardDefaults.cardColors(Color(0xFFA691CD)),
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
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
                                        context.shareCode(generatedOtp, phoneNo)
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
                    text = "My visitors",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                visitorsData?.let { visitors ->
                    items(items = visitors) { visitor ->
                        VisitorRow(
                            visitor = visitor,
                            shareVisitorOtp = { otp, phoneNo ->
                                context.shareCode(otp, phoneNo)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}


fun Context.shareCode(code: String, phoneNo: String) {
    try {
        val shareOtpIntent = Intent(Intent.ACTION_SEND).apply {
            this.type = "text/plain"
            this.`package` = "com.whatsapp"
            this.putExtra(Intent.EXTRA_TEXT, "Hi! Here's your code to come in: $code")
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
            .clip(RoundedCornerShape(18.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Person icon"
                )
                Text(text = visitor.name)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Phone,
                    contentDescription = "Phone number icon"
                )
                Text(text = visitor.phoneNo)
            }
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
                    value = visitor.code,
                    readOnly = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 18.dp, end = 12.dp),
                    colors = TextFieldDefaults.colors(unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                shareVisitorOtp(visitor.code, visitor.phoneNo)
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