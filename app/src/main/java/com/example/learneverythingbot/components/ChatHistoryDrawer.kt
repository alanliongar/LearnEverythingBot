package com.example.learneverythingbot.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.domain.model.ChatHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatHistoryDrawer(
    allChats: List<ChatHistory>,
    onChatSelected: (ChatHistory) -> Unit,
    onChatDeleted: (ChatHistory) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {


    ModalDrawerSheet(
        modifier = modifier.width(320.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        drawerTonalElevation = 16.dp,
        drawerShape = MaterialTheme.shapes.extraLarge

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
                )

            )

            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (allChats.isEmpty()) {
                Text(
                    text = "Nenhuma conversa salva",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
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
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = chat.aiResponse,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis)

                                    Text(
                                        text = formatDate(chat.timestamp),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            },
                            selected = false,
                            onClick = { onChatSelected(chat) },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.Transparent,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                }

            }
            Divider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )


            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Limpar histórico",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Limpar histórico",
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                selected = false,
                onClick = onClearAll,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    unselectedTextColor = MaterialTheme.colorScheme.error,
                    unselectedIconColor = MaterialTheme.colorScheme.error
                ),
                shape = MaterialTheme.shapes.medium
            )
        }

    }
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
    return format.format(date)
}

