package com.example.learneverythingbot.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.domain.model.HistoryItem

@Composable
fun ChatHistoryItem(
    historyItem: HistoryItem,
    onItemClick: (HistoryItem) -> Unit,
    onDeleteClick: (HistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(historyItem) },
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = historyItem.userMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = historyItem.aiResponse.take(50) + if (historyItem.aiResponse.length > 50) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = formatDate(historyItem.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            IconButton(
                onClick = { onDeleteClick(historyItem) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Apagar Chat",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewChatHistoryItem() {
    MaterialTheme {
        ChatHistoryItem(
            historyItem = HistoryItem(
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
            historyItem = HistoryItem(
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