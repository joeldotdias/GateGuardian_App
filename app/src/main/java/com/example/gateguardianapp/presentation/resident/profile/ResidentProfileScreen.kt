package com.example.gateguardianapp.presentation.resident.profile

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Person
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.gateguardianapp.domain.model.resident.Resident
import com.example.gateguardianapp.presentation.resident.components.InputForm
import com.example.gateguardianapp.util.Constants
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
    viewModel: ResidentProfileViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    var isProfileEdited by remember { mutableStateOf(false) }
    var isPfpChanged by remember { mutableStateOf(false) }

    var name by rememberSaveable { mutableStateOf(resident.name) }
    var aboutMe by rememberSaveable { mutableStateOf(resident.aboutMe) }
    var phoneNo by rememberSaveable { mutableStateOf(resident.phoneNo) }
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
                    Text(text = "Cancel")
                }
                
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.uploadPfpToCloud(
                                uri = pfpUri.toUri(),
                                name = resident.name,
                                context = context
                            )
                            delay(Constants.CLOUD_UPLOAD_DELAY)
                            val storageRef = Firebase.storage.reference
                            storageRef.child("images/pfp/${resident.name}.jpg").downloadUrl
                                .addOnSuccessListener { imgUrl ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        viewModel.updateResidentPfpUrl(imgUrl)
                                        delay(Constants.ON_DB_CHANGE_DELAY)
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
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )
            Text(
                text = aboutMe,
                fontStyle = FontStyle.Italic
            )
            Text(text = "+91 $phoneNo")

            Button(
                onClick = { isProfileEdited = true }
            ) {
                Text(text = "Edit profile")
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
                    icon = Icons.Rounded.Person,
                    onImeAction = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                InputForm(
                    value = aboutMe,
                    label = "About Me",
                    onValChange = { aboutMe = it },
                    icon = Icons.Rounded.Description,
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
                    icon = Icons.Rounded.Person,
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
                                viewModel.updateResidentProfile(name, aboutMe, phoneNo)
                                delay(Constants.CLOUD_UPLOAD_DELAY)
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

        LazyColumn(
            modifier = Modifier.padding(top = 15.dp)
        ) {
            item {
                Text(
                    text = "Memories",
                    fontSize = 45.sp,
                    fontFamily = FontFamily.Cursive
                )
            }
        }
    }
}