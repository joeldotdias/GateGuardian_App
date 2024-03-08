package com.example.gateguardianapp.presentation.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gateguardianapp.util.Constants

@Composable
fun CustomerSupportButtons () {
    val context = LocalContext.current
    val supportClicked = remember { mutableStateOf(false) }

    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ){
        if(supportClicked.value) {
            FilledTonalButton(
                onClick = {
                    context.mailCustomerSupport()
                },
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape,
                modifier = Modifier.size(60.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Mail,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Mail customer support icon")
            }

            Spacer(modifier = Modifier.width(5.dp))

            FilledTonalButton(
                onClick = {
                    context.dialCustomerSupport()
                },
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape,
                modifier = Modifier.size(60.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Call,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Dial customer support icon"
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            FilledTonalButton(
                onClick = {
                    supportClicked.value = false
                },
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape,
                modifier = Modifier.size(60.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Cancel,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Hide customer support buttons icon"
                )
            }
        } else {
            FilledTonalButton(
                onClick = {
                    supportClicked.value = true
                },
                contentPadding = PaddingValues(0.dp),
                shape = CircleShape,
                modifier = Modifier.size(60.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.SupportAgent,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Show customer support buttons icon"
                )
            }
        }
    }
}


fun Context.mailCustomerSupport() {
    try {
        val mailCustomerSupportIntent = Intent(Intent.ACTION_SEND).apply {
            this.type = "vnd.android.cursor.item/email"
            this.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.CUSTOMER_SUPPORT_EMAIL))
            this.putExtra(Intent.EXTRA_SUBJECT, "Need assistance")
        }
        startActivity(mailCustomerSupportIntent)
    } catch (e: ActivityNotFoundException) {
        /* Todo */
    }
}


fun Context.dialCustomerSupport(phone: String? = null) {
    val toCall = phone ?: Constants.CUSTOMER_SUPPORT_NUMBER

    try {
        val dialCustomerSupportIntent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts("tel", toCall, null)
        )
        startActivity(dialCustomerSupportIntent)
    } catch (t: Throwable) {
        /* Todo */
    }
}