package com.example.learneverythingbot.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.domain.model.ChatHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatHistoryItem(
    chatHistory: ChatHistory,
    onItemClick: (ChatHistory) -> Unit,
    onDeleteClick: (ChatHistory) -> Unit,
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(chatHistory) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ){
                Text(
                    text = chatHistory.userMessage,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = chatHistory.aiResponse.take(50) + if (chatHistory.aiResponse.length > 50) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formateDate(chatHistory.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            IconButton(
                onClick = { onDeleteClick(chatHistory) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Apagar Chat"
                )
            }
        }
    }
}

private fun formateDate(timestamp: Long): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun PreviewChatHistoryItem() {
    MaterialTheme {
        ChatHistoryItem(
            chatHistory = ChatHistory(
                id = 1,
                userMessage = "Olá, como você está?",
                aiResponse = "Estou bem, obrigado por perguntar! E como posso ajudá-lo hoje?",
                timestamp = System.currentTimeMillis()
            ),
            onItemClick = {},
            onDeleteClick = {},

        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatHistoryItemLongText() {
    MaterialTheme {
        ChatHistoryItem(
            chatHistory = ChatHistory(
                id = 2,
                userMessage = "Gostaria de aprender sobre programação em Kotlin e desenvolvimento Android",
                aiResponse = "Ótimo! Kotlin é uma linguagem moderna e expressiva que é amplamente utilizada no desenvolvimento Android. Vamos começar com os conceitos básicos: variáveis, funções, classes...",
                timestamp = System.currentTimeMillis() - 86400000
            ),
            onItemClick = {},
            onDeleteClick = {},
        )
    }
}