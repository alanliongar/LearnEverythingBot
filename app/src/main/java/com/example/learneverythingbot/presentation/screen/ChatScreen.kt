package com.example.learneverythingbot.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learneverythingbot.components.ChatHistoryDrawer
import com.example.learneverythingbot.presentation.screen.components.MessageInputBar
import com.example.learneverythingbot.domain.model.ChatMessage
import com.example.learneverythingbot.domain.model.Role
import com.example.learneverythingbot.presentation.screen.ui.theme.Purple40
import com.example.learneverythingbot.presentation.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    subject: String,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val chatHistory by chatViewModel.chatHistoryDrawerUiState.collectAsState()
    val chatScreenUiState by chatViewModel.chatScreenUiState.collectAsState()
    val drawerVisible by chatViewModel.drawerVisible.collectAsState()
    val drawerState = rememberDrawerState(if (drawerVisible) DrawerValue.Open else DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }


    LaunchedEffect(chatScreenUiState.chat.aiAnswer) {
        val answer = chatScreenUiState.chat.aiAnswer
        if (answer.isNotBlank()) {
            // se a última é o placeholder, substitui; senão adiciona
            if (messages.isNotEmpty() && messages.last().role == Role.Assistant) {
                messages = messages.dropLast(1) + ChatMessage(Role.Assistant, answer)
            } else {
                messages += ChatMessage(Role.Assistant, answer)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatHistoryDrawer(
                allChats = chatHistory.chatHistory,
                onChatSelected = { chatHistory ->
                    chatViewModel.hideDrawer()
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onChatDeleted = { chatHistory ->
                    chatViewModel.deleteChat(chatHistory.id)
                },
                onClearAll = {
                    chatViewModel.deleteAllChat()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (subject.isNotEmpty())
                                "Assunto: $subject"
                            else
                                "Learn Everything Bot"
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                chatViewModel.showDrawer()
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir Menu")
                        }
                    }
                )
            },
            bottomBar = {
                if (subject.isNotEmpty()) {
                    MessageInputBar(
                        onMessageSend = { userText ->
                            messages += ChatMessage(Role.User, userText)
                            messages += ChatMessage(Role.Assistant, "Digitando...")
                            chatViewModel.getGptResponse(userText)
                        }
                    )

                }
            }
        ) { inner ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(inner)
            ) {
                if (messages.isEmpty()) {
                    Text(
                        text = stringResource(id = com.example.learneverythingbot.R.string.initial_prompt),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
                    ) {
                        items(messages) { msg ->
                            when (msg.role) {
                                Role.User -> UserBubble(msg.text)
                                Role.Assistant -> AssistantText(msg.text)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserBubble(text: String) {
    Row(Modifier.fillMaxWidth()) {
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .widthIn(max = 320.dp)
                .background(Purple40, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(text, color = Color.White, lineHeight = 20.sp)
        }
    }
}

@Composable
private fun AssistantText(text: String) {
    Row(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .weight(1f)
                .padding(end = 48.dp)
        ) {
            Text(text, color = MaterialTheme.colorScheme.onBackground, lineHeight = 20.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreviewEmpty() {
    MaterialTheme {
        ChatScreen(subject = "Kotlin")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreviewWithMessages() {
    MaterialTheme {
        ChatScreen(subject = "Kotlin")
    }
}