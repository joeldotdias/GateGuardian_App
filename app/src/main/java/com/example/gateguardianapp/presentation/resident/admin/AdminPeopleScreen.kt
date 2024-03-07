package com.example.gateguardianapp.presentation.resident.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddModerator
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gateguardianapp.data.remote.dto.ResidentDto
import com.example.gateguardianapp.data.remote.dto.SecurityDto
import com.example.gateguardianapp.presentation.components.InputForm
import com.example.gateguardianapp.util.Delays
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdminPeopleScreen(
    viewModel: AdminViewModel,
    adminEmail: String = Firebase.auth.currentUser?.email!!,
    onPeopleChange: () -> Unit
) {
    val residentsData = viewModel.state.collectAsState().value.residents
    val securitiesData = viewModel.state.collectAsState().value.securities

    var newResidentName by remember { mutableStateOf("") }
    var newResidentEmail by remember { mutableStateOf("") }
    var newSecurityName by remember { mutableStateOf("") }
    var newSecurityEmail by remember { mutableStateOf("") }

    var isAddingResident by remember { mutableStateOf(false) }
    var isAddingSecurity by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    isAddingResident = !isAddingResident
                    isAddingSecurity = false
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonAdd,
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Add Resident icon"
                )
            }

            IconButton(
                onClick = {
                    isAddingSecurity = !isAddingSecurity
                    isAddingResident = false
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddModerator,
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Add Security icon"
                )
            }
        }

        AnimatedVisibility(visible = isAddingResident) {
            AddPersonForm(
                name = newResidentName,
                onNameChanged = { newResidentName = it },
                email = newResidentEmail,
                onEmailChanged = { newResidentEmail = it },
                onCancelClick = {
                    isAddingResident = false
                    newResidentName = ""
                    newResidentEmail = ""
                },
                onSaveClick = {
                    isAddingResident = false

                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.addResident(newResidentName, newResidentEmail)
                        delay(Delays.CLOUD_UPLOAD_DELAY)
                        newResidentName = ""
                        newResidentEmail = ""
                        onPeopleChange()
                    }
                }
            )
        }

        AnimatedVisibility(visible = isAddingSecurity) {
            AddPersonForm(
                name = newSecurityName,
                onNameChanged = { newSecurityName = it },
                email = newSecurityEmail,
                onEmailChanged = { newSecurityEmail = it },
                onCancelClick = {
                    isAddingSecurity = false
                    newSecurityName = ""
                    newSecurityEmail = ""
                },
                onSaveClick = {
                    isAddingSecurity = false

                    coroutineScope.launch(Dispatchers.IO) {
                        viewModel.addSecurity(newSecurityName, newSecurityEmail)
                        delay(Delays.CLOUD_UPLOAD_DELAY)
                        newSecurityName = ""
                        newSecurityEmail = ""
                        onPeopleChange()
                    }
                }
            )
        }


        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(18.dp),
            modifier = Modifier.padding(top = 10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
        ) {
            LazyColumn(
                contentPadding = PaddingValues(7.dp)
            ) {
                residentsData?.let { residents ->
                    items(residents) { resident ->
                        ResidentDetailsCard(
                            resident = resident,
                            isAdmin = resident.email == adminEmail
                        )
                        HorizontalDivider()
                    }
                }

                securitiesData?.let { securities ->
                    items(items = securities) { security ->
                        SecurityDetailsCard(security)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun ResidentDetailsCard(
    resident: ResidentDto,
    isAdmin: Boolean
) {
    val roleColor = if(isAdmin) Color(0xFFEC653A) else Color(0xFF2196F3)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = resident.name + if (isAdmin) " (You)" else "",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Card(
                colors = CardDefaults.cardColors(roleColor)
            ) {
                Text(
                    text = if (isAdmin) "Admin" else "Resident",
                    modifier = Modifier.padding(6.dp),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Resident home icon"
                )
                if (resident.flatNo != 0) {
                    Text(text = "${resident.flatNo}, ")
                }
                resident.building?.let { building ->
                    Text(text = building)
                }
            }
        }
    }
}

@Composable
fun SecurityDetailsCard(security: SecurityDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = security.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Card(
                colors = CardDefaults.cardColors(Color(0xFF966DE0))
            ) {
                Text(
                    text = "Security",
                    modifier = Modifier.padding(6.dp),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Badge,
                    modifier = Modifier.size(19.dp),
                    contentDescription = "Security badge icon"
                )
                security.badgeId?.let { badgeId ->
                    Text(text = badgeId)
                }
            }
        }
    }
}

@Composable
fun AddPersonForm(
    name: String,
    onNameChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick:() -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        InputForm(
            value = name,
            label = "Name",
            onValChange = onNameChanged,
            leadingIcon = Icons.Rounded.Person,
            onImeAction = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        InputForm(
            value = email,
            label = "Email",
            onValChange = onEmailChanged,
            leadingIcon = Icons.Rounded.AlternateEmail,
            keyboardType = KeyboardType.Email,
            capitalization = KeyboardCapitalization.None,
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
                onClick = onCancelClick
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = onSaveClick
            ) {
                Text(text = "Save")
            }
        }
    }
}