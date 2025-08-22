package com.example.learneverythingbot.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
    if (message.isNotEmpty()) {
        onMessageSend(message)
        onMessageCleared(message)
    }
}

@Composable
fun MessageInput(
    onMessageSend: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = { message = it },
            placeholder = { Text(text = stringResource(id = R.string.messageinput)) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    handleSendMessage(
                        message,
                        onMessageSend = { newMessage ->
                            onMessageSend(newMessage)
                        },
                        onMessageCleared = {
                            message = ""
                        }
                    )
                }
            )
        )
        IconButton(
            onClick = {
                handleSendMessage(message, onMessageSend) { newMessage ->
                    message = newMessage
                }
            },
            modifier = Modifier
                .padding(start = 8.dp)
                .size(50.dp)
                .background(
                    color = colorResource(id = R.color.primary),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar",
                tint = colorResource(id = R.color.text_secondary),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MessageInputPreview() {
    MessageInput(onMessageSend = {  })
}