package com.example.gateguardianapp.presentation.resident.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.CameraAlt
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Composable
fun ResidentProfileScreen(
    email: String,
    signOut:() -> Unit,
    viewModel: ResidentProfileViewModel = hiltViewModel(),
) {
    val residentData = viewModel.state.value.resident
    viewModel.getProfileDetails(email)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isPfpChanged by remember { mutableStateOf(false) }

    val pfpPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.uploadPfpToCloud(
                uri = uri!!,
                name = residentData!!.name,
                context = context
            )
            isPfpChanged = true
        }
    )

    residentData?.let {
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
                    .padding(top = 50.dp, bottom = 17.dp)
            ) {
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(width = 1.dp, color = Color.LightGray),
                ) {
                    if(residentData.pfpUrl != null) {
                        AsyncImage(
                            model = residentData.pfpUrl.toUri(),
                            modifier = Modifier.size(160.dp),
                            contentDescription = "Pfp"
                        )
                    }
                    else {
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
                Button(
                    onClick = {
                        val storageRef = Firebase.storage.reference
                        storageRef.child("images/pfp/${residentData.name}.jpg").downloadUrl
                            .addOnSuccessListener { imgUrl ->
                                viewModel.updateResidentPfpUrl(residentData.email, imgUrl)
                            }
                        isPfpChanged = false
                    }
                ) {
                    Text(text = "Save")
                }
            }
            Text(text = residentData.name, style = MaterialTheme.typography.titleMedium, fontSize = 25.sp)
        }
    }
}