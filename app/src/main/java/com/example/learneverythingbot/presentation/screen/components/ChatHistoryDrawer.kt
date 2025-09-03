package com.example.learneverythingbot.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.domain.model.HistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatHistoryDrawer(
    allChats: List<HistoryItem>,
    onChatSelected: (HistoryItem) -> Unit,
    onChatDeleted: (HistoryItem) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {

    val primaryColor = Color(0xFF10B981)
    val surfaceColor = Color(0xFFFFFFFF)
    val onSurfaceColor = Color(0xFF0F172A)
    val onSurfaceVariantColor = Color(0xFF374151)
    val outlineColor = Color(0xFFE5E7EB)
    val errorColor = Color(0xFFEF4444)

    ModalDrawerSheet(
        modifier = modifier.width(400.dp),
        drawerContainerColor = onSurfaceColor,
        drawerContentColor = surfaceColor,
        drawerTonalElevation = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 24.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Histórico de Conversas",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = surfaceColor
            )

            Divider(
                color = outlineColor,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
            )

            if (allChats.isEmpty()) {
                Text(
                    text = "Nenhuma conversa salva",
                    style = MaterialTheme.typography.bodyMedium,
                    color = onSurfaceVariantColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(allChats) { chat ->
                        NavigationDrawerItem(
                            label = {
                                Column {
                                    Text(
                                        text = chat.userMessage,
                                        style = MaterialTheme.typography.bodyLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = surfaceColor
                                    )
                                    Text(
                                        text = chat.aiResponse,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = surfaceColor
                                    )
                                    Text(
                                        text = formatDate(chat.timestamp),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = onSurfaceVariantColor,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            },
                            selected = false,
                            onClick = { onChatSelected(chat) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.Transparent,
                                unselectedTextColor = onSurfaceColor,
                                unselectedIconColor = onSurfaceVariantColor
                            )
                        )
                    }
                }
            }

            Divider(
                color = outlineColor,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Limpar histórico",
                        style = MaterialTheme.typography.bodyLarge,
                        color = errorColor
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Limpar histórico",
                        tint = errorColor
                    )
                },
                selected = false,
                onClick = onClearAll,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = errorColor,
                    unselectedIconColor = errorColor
                )
            )
        }
    }
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
    return format.format(date)
}