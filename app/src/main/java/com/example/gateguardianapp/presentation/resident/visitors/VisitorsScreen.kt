package com.example.gateguardianapp.presentation.resident.visitors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddIcCall
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gateguardianapp.domain.model.resident.Visitor
import com.example.gateguardianapp.presentation.resident.components.InputForm
import com.example.gateguardianapp.util.Delays
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VisitorsScreen(
    viewModel: VisitorsViewModel,// = hiltViewModel(),
    onVisitorsDataChange: () -> Unit// = viewModel::getVisitorsByResident
) {

    val visitorsData = viewModel.state.value.visitors

    val coroutineScope = rememberCoroutineScope()

    var isAddingVisitor by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(top = 15.dp, bottom = 12.dp),
            onClick = { isAddingVisitor = !isAddingVisitor }
        ) {
            Text(text = "Add visitor")
        }

        AnimatedVisibility(visible = isAddingVisitor) {
            AddVisitorForm(
                hideForm = { isAddingVisitor = false }
            ) { name, phoneNo ->
                coroutineScope.launch(Dispatchers.IO) {
                    viewModel.saveVisitor(name, phoneNo)
                    isAddingVisitor = false
                    delay(Delays.CLOUD_UPLOAD_DELAY)
                    onVisitorsDataChange()
                }
            }
        }


        LazyColumn(
            contentPadding = PaddingValues(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            visitorsData?.let { visitors ->
                items(items = visitors) { visitor ->
                    EventRow(visitor)
                }
            }
        }
    }
}

@Composable
fun EventRow(visitor: Visitor) {
    Text(text = visitor.name)
}


@Composable
fun AddVisitorForm(
    hideForm: () -> Unit,
    saveVisitorDetails: (String, String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf("") }
    var phoneNo by remember { mutableStateOf("") }
    
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
                onClick = { hideForm() }
            ) {
                Text(text = "Cancel")
            }

            Button(
                onClick = { saveVisitorDetails(name, phoneNo) }
            ) {
                Text(text = "Save")
            }
        }
    }
}