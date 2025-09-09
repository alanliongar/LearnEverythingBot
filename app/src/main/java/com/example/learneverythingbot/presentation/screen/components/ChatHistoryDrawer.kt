package com.example.learneverythingbot.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.learneverythingbot.domain.model.ChatHistoryItem

@Composable
fun ChatHistoryDrawer(
    allChats: List<ChatHistoryItem>,
    onChatSelected: (ChatHistoryItem) -> Unit,
    onChatDeleted: (ChatHistoryItem) -> Unit,
    onClearAll: () -> Unit,
    onStartQuiz: (ChatHistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    ModalDrawerSheet(
        modifier = modifier.width(400.dp),
        drawerContainerColor = cs.surface,
        drawerContentColor = cs.onSurface,
        drawerTonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 24.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Histórico de Conversas",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = cs.onSurface
            )

            Divider(
                color = cs.outlineVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
            )

            if (allChats.isEmpty()) {
                Text(
                    text = "Nenhuma conversa salva",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant,
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { onChatSelected(chat) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = cs.onSurface
                                )
                            ) {
                                Text(
                                    text = chat.userMessage,
                                    maxLines = 1
                                )
                            }

                            TextButton(
                                onClick = { onStartQuiz(chat) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = cs.primary
                                )
                            ) {
                                Text("Quiz")
                            }

                            TextButton(
                                onClick = { onChatDeleted(chat) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = cs.error
                                )
                            ) {
                                Text("Excluir")
                            }
                        }
                    }
                }
            }

            Divider(
                color = cs.outlineVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 16.dp)
            )

            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Limpar histórico",
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.error
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Limpar histórico",
                        tint = cs.error
                    )
                },
                selected = false,
                onClick = onClearAll,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = cs.surface,
                    unselectedTextColor = cs.error,
                    unselectedIconColor = cs.error,
                    selectedContainerColor = cs.surfaceVariant,
                    selectedTextColor = cs.error,
                    selectedIconColor = cs.error
                )
            )
        }
    }
}
