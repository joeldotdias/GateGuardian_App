package com.example.gateguardianapp.presentation.security.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddIcCall
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PhoneInTalk
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.gateguardianapp.presentation.resident.components.InputForm
import com.example.gateguardianapp.util.Delays
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SecurityProfileScreen(
    signOut: () -> Unit,
    viewModel: SecurityProfileViewModel = hiltViewModel(),
    onSecurityDataChange: () -> Unit = viewModel::getSecurityProfileDetails
) {
    //val state = viewModel.state.value
    val security = viewModel.state.value.security

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()


    var isProfileEdited by remember { mutableStateOf(false) }
    var isPfpChanged by remember { mutableStateOf(false) }



    security?.let {
        var areSecurityDetailsNotProvided by remember { mutableStateOf(security.badgeId == null) }

        var name by rememberSaveable { mutableStateOf(security.name) }
        var phoneNo by rememberSaveable { mutableStateOf(security.phoneNo ?: "") }
        var badgeId by rememberSaveable { mutableStateOf(security.badgeId ?: "") }
        var pfpUri by rememberSaveable { mutableStateOf(security.pfpUrl) }

        val pfpPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                pfpUri = uri.toString()
                isPfpChanged = true
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Sign out", fontSize = 20.sp)
            IconButton(
                onClick = { signOut() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Sign out icon"
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            item {
                AnimatedVisibility(visible = areSecurityDetailsNotProvided) {
                    ProvideSecurityDetailsForm(
                        focusManager = focusManager,
                        saveSecurityDetails = { phoneNo, badgeId ->
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.updateSecurityProfile(name, badgeId, phoneNo)
                                areSecurityDetailsNotProvided = false
                                delay(Delays.CLOUD_UPLOAD_DELAY)
                                onSecurityDataChange()
                            }
                        }
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(15.dp)),
                        shape = RoundedCornerShape(15.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 20.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = security.society,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 30.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier
                                    //.align(Alignment.CenterHorizontally)
                                    .padding(top = 30.dp, bottom = 30.dp)
                            ) {
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                    border = BorderStroke(width = 1.dp, color = Color.LightGray)
                                ) {
                                    // Do not remove this null check
                                    if (pfpUri != null) {
                                        AsyncImage(
                                            model = pfpUri.toUri(),
                                            modifier = Modifier
                                                .size(150.dp)
                                                .clip(RoundedCornerShape(10.dp)),
                                            contentScale = ContentScale.Crop,
                                            contentDescription = "Pfp"
                                        )
                                    } else {
                                        Image(
                                            imageVector = Icons.Rounded.AccountCircle,
                                            modifier = Modifier.size(150.dp),
                                            colorFilter = ColorFilter.tint(Color(0xFF59EBDD)),
                                            contentDescription = "Default Pfp icon"
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 3.dp),
                                        contentDescription = "Security badge icon"
                                    )
                                    Text(text = name)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Phone,
                                        modifier = Modifier
                                            .size(23.dp)
                                            .padding(end = 3.dp),
                                        tint = Color(0xFF62B065),
                                        contentDescription = "Security badge icon"
                                    )
                                    Text(text = "+91 ${phoneNo.substring(0, 5)} ${phoneNo.substring(5)}")
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Badge,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 3.dp),
                                        tint = Color(0xFF3F51B5),
                                        contentDescription = "Security badge icon"
                                    )
                                    Text(text = badgeId)
                                }
                            }
                        }
                    }
                }
            }


            item {
                Surface(
                    modifier = Modifier
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
                                    .size(160.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Pfp"
                            )
                        } else {
                            Image(
                                imageVector = Icons.Rounded.AccountCircle,
                                modifier = Modifier.size(160.dp),
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
            }

            item {
                AnimatedVisibility(visible = isPfpChanged) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                                pfpUri = security.pfpUrl
                                isPfpChanged = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.uploadPfpToCloud(
                                        uri = pfpUri.toUri(),
                                        name = security.name,
                                        context = context
                                    )
                                    delay(Delays.CLOUD_UPLOAD_DELAY)
                                    val storageRef = Firebase.storage.reference
                                    storageRef.child("images/pfp/${security.name}.jpg").downloadUrl
                                        .addOnSuccessListener { imgUrl ->
                                            coroutineScope.launch(Dispatchers.IO) {
                                                viewModel.updateSecurityPfpUrl(imgUrl)
                                                delay(Delays.ON_DB_CHANGE_DELAY)
                                                onSecurityDataChange()
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
                if (!isProfileEdited) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )
                    badgeId?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Badge,
                                modifier = Modifier
                                    .size(21.dp)
                                    .padding(end = 3.dp),
                                tint = Color(0xFF3F51B5),
                                contentDescription = "Security badge icon"
                            )
                            Text(text = badgeId)
                        }
                    }
                    if (phoneNo.length == 10) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PhoneInTalk,
                                modifier = Modifier
                                    .size(21.dp)
                                    .padding(end = 3.dp),
                                tint = Color(0xFF62B065),
                                contentDescription = "Phone icon"
                            )
                            Text(text = "+91 ${phoneNo.substring(0, 5)} ${phoneNo.substring(5)}")
                        }
                    }

                    Button(
                        onClick = { isProfileEdited = true }
                    ) {
                        Text(text = "Edit profile")
                    }
                }
            }

            item {
                AnimatedVisibility(visible = isProfileEdited) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
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
                            value = badgeId,
                            label = "About Me",
                            onValChange = { badgeId = it },
                            icon = Icons.Rounded.Description,
                            capitalization = KeyboardCapitalization.Sentences,
                            onImeAction = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            )
                        )
                        InputForm(
                            value = phoneNo,
                            label = "Phone Number",
                            onValChange = {
                                if (it.length <= 10) phoneNo = it
                            },
                            icon = Icons.Rounded.Phone,
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
                                    name = security.name
                                    badgeId = security.badgeId
                                    phoneNo = security.phoneNo
                                    isProfileEdited = false
                                }
                            ) {
                                Text(text = "Cancel")
                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        viewModel.updateSecurityProfile(name, badgeId, phoneNo)
                                        delay(Delays.CLOUD_UPLOAD_DELAY)
                                        onSecurityDataChange()
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
    }
}


@Composable
fun ProvideSecurityDetailsForm(
    focusManager: FocusManager,
    saveSecurityDetails: (String, String) -> Unit
) {

    var phoneNo by remember { mutableStateOf("") }
    var badgeId by remember { mutableStateOf("") }

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
                value = phoneNo,
                label = "Phone number",
                onValChange = { phoneNo = it },
                icon = Icons.Rounded.AddIcCall,
                keyboardType = KeyboardType.Number,
                onImeAction = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            InputForm(
                value = badgeId,
                label = "Badge ID",
                onValChange = { badgeId = it },
                icon = Icons.Rounded.Badge,
                imeAction = ImeAction.Done,
                onImeAction = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Button(
                onClick = { saveSecurityDetails(phoneNo, badgeId) }
            ) {
                Text(text = "Save")
            }
        }
    }
}