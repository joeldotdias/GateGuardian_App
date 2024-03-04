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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.rounded.AddAlert
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.ViewHeadline
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gateguardianapp.data.mapper.getIstDateTime
import com.example.gateguardianapp.data.remote.dto.NoticeDto
import com.example.gateguardianapp.presentation.components.InputForm
import com.example.gateguardianapp.util.Delays
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdminNoticesScreen(
    viewModel: AdminViewModel,
    onNoticeChange: () -> Unit
) {
    val noticesData = viewModel.state.collectAsState().value.notices

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    var isAddingNotice by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        contentPadding = PaddingValues(8.dp)
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
                        onClick = { isAddingNotice = !isAddingNotice }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddAlert,
                            contentDescription = "Add Notice icon"
                        )
                    }
                    IconButton(
                        onClick = {
                            // Todo show dropdown menu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FilterAlt,
                            contentDescription = "Filter Notices icon"
                        )
                    }
                }

                AnimatedVisibility(visible = isAddingNotice) {
                    AddNoticeForm(
                        title = title,
                        onTitleChange = { title = it },
                        body = body,
                        onBodyChange = { body = it },
                        onSaveClick = {
                            coroutineScope.launch {
                                viewModel.addNotice(title, body)
                                delay(Delays.CLOUD_UPLOAD_DELAY)
                                title = ""
                                body = ""
                                onNoticeChange()
                            }
                        },
                        onCancelClick = {
                            isAddingNotice = false
                            title = ""
                            body = ""
                        }
                    )
                }
            }
        }

        noticesData?.let { notices ->
            items(notices) { notice ->
                MinimalNoticeCard(notice)
            }
        }
    }
}

@Composable
fun AddNoticeForm(
    title: String,
    onTitleChange: (String) -> Unit,
    body: String,
    onBodyChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        InputForm(
            value = title,
            label = "Title",
            onValChange = onTitleChange,
            leadingIcon = Icons.Rounded.ViewHeadline,
            onImeAction = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        InputForm(
            value = body,
            label = "Body",
            onValChange = onBodyChange,
            leadingIcon = Icons.Rounded.Description,
            maxLines = 10,
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

@Composable
fun MinimalNoticeCard(notice: NoticeDto) {
    val postedOn = getIstDateTime(notice.posted).split(" ")[0]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Text(
                text = notice.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Text(
                text = "Posted on: $postedOn"
            )
        }
    }
}