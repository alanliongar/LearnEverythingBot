@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.learneverythingbot.presentation.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.R

private fun handleSendMessage(
    message: String,
    onMessageSend: (String) -> Unit,
    onMessageCleared: (String) -> Unit
) {
    val trimmed = message.trim()
    if (trimmed.isNotEmpty()) {
        onMessageSend(trimmed)
        onMessageCleared(trimmed)
    }
}
@Composable
fun MessageInputBar(
    onMessageSend: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var message by remember { mutableStateOf("") }

    Surface(
        modifier = modifier,
        shadowElevation = 8.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                enabled = true,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 52.dp),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.hint_message),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        handleSendMessage(
                            message,
                            onMessageSend = { newMessage -> onMessageSend(newMessage) },
                            onMessageCleared = { message = "" }
                        )
                    }
                )
            )
            Spacer(Modifier.width(8.dp))
            FilledIconButton(
                onClick = {
                    handleSendMessage(
                        message,
                        onMessageSend = { newMessage -> onMessageSend(newMessage) },
                        onMessageCleared = { message = "" }
                    )
                },
                enabled = true,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MessageInputBarPreview() {
    MaterialTheme {
        MessageInputBar(onMessageSend = { /* no-op */ })
    }
}
