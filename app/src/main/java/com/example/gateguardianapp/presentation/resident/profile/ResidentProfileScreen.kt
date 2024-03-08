package com.example.gateguardianapp.presentation.resident.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Apartment
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PhoneInTalk
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.presentation.components.InputForm
import com.example.gateguardianapp.util.Delays
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ResidentProfileScreen(
    resident: Resident,
    onResidentDataChange: () -> Unit,
    signOut: () -> Unit,
    viewModel: ResidentProfileViewModel
) {

    val state = viewModel.state.collectAsState().value

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    var areHomeDetailsNotProvided by remember { mutableStateOf(resident.building == null) }
    var isProfileEdited by remember { mutableStateOf(false) }
    var isPfpChanged by remember { mutableStateOf(false) }

    var name by rememberSaveable { mutableStateOf(resident.name) }
    var aboutMe by rememberSaveable { mutableStateOf(resident.aboutMe ?: "") }
    var phoneNo by rememberSaveable { mutableStateOf(resident.phoneNo ?: "") }
    var pfpUri by rememberSaveable { mutableStateOf(resident.pfpUrl) }

    val pfpPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            pfpUri = uri.toString()
            isPfpChanged = true
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = areHomeDetailsNotProvided) {
            ProvideHomeDetailsForm(
                focusManager = focusManager,
                saveHomeDetails = { flatNo, building ->
                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.saveHomeDetails(flatNo, building)
                        areHomeDetailsNotProvided = false
                        delay(Delays.CLOUD_UPLOAD_DELAY)
                        onResidentDataChange()
                    }
                }
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp, bottom = 17.dp)
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(width = 1.dp, color = Color.LightGray)
            ) {
                // Do not remove this null check
                if (pfpUri != null) {
                    AsyncImage(
                        model = pfpUri.toUri(),
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Pfp"
                    )
                } else {
                    Image(
                        imageVector = Icons.Rounded.AccountCircle,
                        modifier = Modifier.size(140.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF59EBDD)),
                        contentDescription = "Default Pfp icon"
                    )
                }
            }

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF0588F1)),
                onClick = {
                    pfpPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.CameraAlt,
                    modifier = Modifier
                        .size(35.dp)
                        .padding(4.dp),
                    tint = Color.White,
                    contentDescription = "Photo picker icon"
                )
            }
        }
        AnimatedVisibility(visible = isPfpChanged) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = {
                        pfpUri = resident.pfpUrl
                        isPfpChanged = false
                    }
                ) {
                    Text(text = "Dismiss")
                }
                
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.uploadPfpToCloud(
                                uri = pfpUri.toUri(),
                                name = resident.name,
                                context = context
                            )
                            delay(Delays.CLOUD_UPLOAD_DELAY)
                            val storageRef = Firebase.storage.reference
                            storageRef.child("images/pfp/${resident.name}.jpg").downloadUrl
                                .addOnSuccessListener { imgUrl ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        viewModel.updateResidentPfpUrl(imgUrl)
                                        delay(Delays.ON_DB_CHANGE_DELAY)
                                        onResidentDataChange()
                                    }
                                }
                            isPfpChanged = false
                        }
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
        if(!isProfileEdited) {
            Card(
                elevation = CardDefaults.cardElevation(20.dp),
                colors = CardDefaults.cardColors(Color.White),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        modifier = Modifier.padding(end = 7.dp),
                        contentDescription = "Person icon"
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        modifier = Modifier.padding(end = 7.dp),
                        contentDescription = "Person icon"
                    )
                    Text(
                        text = "${resident.building} - ${resident.flatNo}",
                        fontSize = 20.sp
                    )
                }
                HorizontalDivider()
                if(phoneNo.length > 5) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .height(50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PhoneInTalk,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 7.dp),
                            tint = Color(0xFF62B065),
                            contentDescription = "Phone icon"
                        )
                        Text(text = "+91 ${phoneNo.substring(0,5)} ${phoneNo.substring(5)}")
                    }
                }
            }

            Card(
                modifier = Modifier.padding(15.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .padding(15.dp),
                    onClick = { isProfileEdited = true },
                    colors = ButtonDefaults.buttonColors(Color(0xFFCAE1F3))
                ) {
                    Text(
                        text = "Edit profile",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Blue
                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .padding(15.dp),
                    onClick = signOut,
                    colors = ButtonDefaults.buttonColors(Color(0xFFFCCFDE))
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 7.dp),
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        tint = Color.Black,
                        contentDescription = "Sign out"
                    )
                    Text(
                        text = "Sign out",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Red
                    )
                }
            }
        }
        AnimatedVisibility(visible = isProfileEdited) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    value = aboutMe,
                    label = "About Me",
                    onValChange = { aboutMe = it },
                    leadingIcon = Icons.Rounded.Description,
                    capitalization = KeyboardCapitalization.Sentences,
                    onImeAction = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                InputForm(
                    value = phoneNo,
                    label = "Phone Number",
                    onValChange = {
                        if(it.length <= 10) phoneNo = it
                    },
                    leadingIcon = Icons.Rounded.Phone,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    onImeAction = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            name = resident.name
                            aboutMe = resident.aboutMe
                            phoneNo = resident.phoneNo
                            isProfileEdited = false
                        }
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.updateResidentProfile(aboutMe, phoneNo)
                                delay(Delays.CLOUD_UPLOAD_DELAY)
                                onResidentDataChange()
                            }
                            isProfileEdited = false
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

@Composable
fun ProvideHomeDetailsForm(
    focusManager: FocusManager,
    saveHomeDetails: (String, String) -> Unit
) {

    var flatNo by remember { mutableStateOf("") }
    var building by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please provide us with a few details for a seamless experience",
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
            InputForm(
                value = flatNo,
                label = "Flat number",
                onValChange = { flatNo = it },
                leadingIcon = Icons.Rounded.Home,
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

            Button(
                onClick = { saveHomeDetails(flatNo, building) }
            ) {
                Text(text = "Save")
            }
        }
    }
}