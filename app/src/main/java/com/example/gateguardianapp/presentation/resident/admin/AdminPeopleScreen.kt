package com.example.gateguardianapp.presentation.resident.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.rounded.AddModerator
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.gateguardianapp.presentation.resident.components.InputForm
import com.example.gateguardianapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdminPeopleScreen(
    viewModel: AdminViewModel,
    adminEmail: String,
    onPeopleChange: () -> Unit
) {
    val residentsData = viewModel.state.value.residents

    val coroutineScope = rememberCoroutineScope()

    var isAddingResident by remember { mutableStateOf(false) }
    var newResidentName by remember { mutableStateOf("") }
    var newResidentEmail by remember { mutableStateOf("") }

    var isAddingSecurity by remember { mutableStateOf(false) }
    var newSecurityName by remember { mutableStateOf("") }
    var newSecurityEmail by remember { mutableStateOf("") }
    
    LazyColumn(
        contentPadding = PaddingValues(7.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 8.dp, end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            isAddingResident = !isAddingResident
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PersonAdd,
                            contentDescription = "Add Resident icon"
                        )
                    }

                    IconButton(
                        onClick = {
                            isAddingSecurity = !isAddingSecurity
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddModerator,
                            contentDescription = "Add Security icon"
                        )
                    }

                    IconButton(
                        onClick = {
                            // Todo show dropdown menu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "Filter Residents icon"
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
                                delay(Constants.CLOUD_UPLOAD_DELAY)
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
                                delay(Constants.CLOUD_UPLOAD_DELAY)
                                newSecurityName = ""
                                newSecurityEmail = ""
                                onPeopleChange()
                            }
                        }
                    )
                }
            }
        }

        residentsData?.let { residents ->
            items(items = residents) { resident ->
                ResidentDetailsCard(
                    resident = resident,
                    isAdmin = resident.email == adminEmail
                )
            }
        }
    }
}

@Composable
fun ResidentDetailsCard(
    resident: ResidentDto,
    isAdmin: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Text(
                text = resident.name + if(isAdmin) " (You)" else "",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Text(text = "${resident.flatNo}, ${resident.building}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if(isAdmin) Icons.Rounded.AdminPanelSettings else Icons.Rounded.Person,
                    modifier = Modifier.size(19.dp),
                    contentDescription = "Resident icon"
                )
                Text(text = if(isAdmin) "Admin" else "Resident")
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
            icon = Icons.Rounded.Person,
            onImeAction = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        InputForm(
            value = email,
            label = "Email",
            onValChange = onEmailChanged,
            icon = Icons.Rounded.AlternateEmail,
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