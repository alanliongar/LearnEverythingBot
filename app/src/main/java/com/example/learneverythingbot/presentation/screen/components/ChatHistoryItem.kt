package com.example.learneverythingbot.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatHistoryDrawerItem(
    chat: ChatHistoryItem,
    onChatSelected: () -> Unit,
    onChatDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Excluir conversa") },
            text = { Text("Tem certeza que deseja excluir esta conversa?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onChatDeleted()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    NavigationDrawerItem(
        label = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChatSelected() }
            ) {
                Text(
                    text = chat.userMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Text(
                    text = chat.aiResponse.take(80) + if (chat.aiResponse.length > 80) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFFD1D5DB),
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = formatDate(chat.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        },
        selected = false,
        onClick = { onChatSelected() },
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .fillMaxWidth(),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent,
            unselectedTextColor = Color.White,
            unselectedIconColor = Color(0xFFD1D5DB)
        ),
        badge = {
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir conversa",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    )
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault())
    return format.format(date)
}



@Preview(showBackground = true)
@Composable
fun PreviewChatHistoryItem() {
    ChatHistoryDrawerItem(
        chat = ChatHistoryItem(
            userMessage = "Olá, tudo bem?",
            aiResponse = "Olá! Como posso ajudar você hoje?",
            timestamp = System.currentTimeMillis()
        ),
        onChatSelected = {},
        onChatDeleted = { },
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewChatHistoryItemLongText() {
    ChatHistoryDrawerItem(
        chat = ChatHistoryItem(
            userMessage = "Olá, tudo bem?",
            aiResponse = "Olá! Como posso ajudar você hoje?",
            timestamp = System.currentTimeMillis()
        ),
        onChatSelected = {},
        onChatDeleted = { },
        modifier = Modifier
    )
}