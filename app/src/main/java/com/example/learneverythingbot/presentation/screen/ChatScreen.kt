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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learneverythingbot.components.ChatHistoryDrawer
import com.example.learneverythingbot.domain.model.Chat
import com.example.learneverythingbot.domain.model.ChatHistory
import com.example.learneverythingbot.domain.model.ChatHistoryDrawerUiState
import com.example.learneverythingbot.presentation.screen.components.MessageInputBar
import com.example.learneverythingbot.domain.model.ChatMessage
import com.example.learneverythingbot.domain.model.ChatScreenUiState
import com.example.learneverythingbot.domain.model.Role
import com.example.learneverythingbot.presentation.screen.ui.theme.Purple40
import com.example.learneverythingbot.presentation.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    subject: String,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val chatHistory by chatViewModel.chatHistoryDrawerUiState.collectAsState()
    val chatScreenUiState by chatViewModel.chatScreenUiState.collectAsState()
    val drawerVisible by chatViewModel.drawerVisible.collectAsState()
    val drawerState =
        rememberDrawerState(if (drawerVisible) DrawerValue.Open else DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ChatScreenContent(
        chatScreenUiState = chatScreenUiState,
        drawerState = drawerState,
        chatHistory = chatHistory,
        coroutineScope = coroutineScope,
        onHideDrawer = { chatViewModel.hideDrawer() },
        onShowDrawer = { chatViewModel.showDrawer() },
        onDeleteChat = { chatViewModel.deleteChat(it) },
        onDeleteAllChat = { chatViewModel.deleteAllChat() },
        onGetGptResponse = { chatViewModel.getGptResponse(it) },
        subject = subject
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenContent(
    chatScreenUiState: ChatScreenUiState,
    drawerState: DrawerState,
    chatHistory: ChatHistoryDrawerUiState,
    coroutineScope: CoroutineScope,
    onHideDrawer: () -> Unit,
    onShowDrawer: () -> Unit,
    onDeleteChat: (Int) -> Unit,
    onDeleteAllChat: () -> Unit,
    onGetGptResponse: (String) -> Unit,
    subject: String,
) {
    // 1) Deriva mensagens do histórico (fonte de verdade = Room)
    val historyMessages = remember(chatHistory.chatHistory) {
        chatHistory.chatHistory
            .sortedBy { it.timestamp }
            .flatMap { item ->
                listOf(
                    ChatMessage(Role.User, item.userMessage),
                    ChatMessage(Role.Assistant, item.aiResponse)
                )
            }
    }

    // 2) Apenas controla o placeholder "Digitando..."
    var isTyping by remember { mutableStateOf(false) }

    // Quando chega uma resposta (aiAnswer preenchido), removemos o "Digitando..."
    LaunchedEffect(chatScreenUiState.chat.aiAnswer) {
        if (chatScreenUiState.chat.aiAnswer.isNotBlank()) {
            isTyping = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatHistoryDrawer(
                allChats = chatHistory.chatHistory,
                onChatSelected = {
                    onHideDrawer()
                    coroutineScope.launch { drawerState.close() }
                },
                onChatDeleted = { onDeleteChat(it.id) },
                onClearAll = { onDeleteAllChat() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(if (subject.isNotEmpty()) "Assunto: $subject" else "Learn Everything Bot")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onShowDrawer()
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir Menu")
                        }
                    }
                )
            },
            bottomBar = {
                if (subject.isNotEmpty()) {
                    MessageInputBar(
                        onMessageSend = { userText ->
                            // não mexe no histórico local; deixa o Room atualizar
                            isTyping = true
                            onGetGptResponse(userText)
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
                val uiMessages = if (isTyping)
                    historyMessages + ChatMessage(Role.Assistant, "Digitando...")
                else historyMessages

                if (uiMessages.isEmpty()) {
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
                        items(uiMessages) { msg ->
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
    val chatScreenUiState = ChatScreenUiState(
        chat = Chat(
            subject = "",
            aiAnswer = "", // vazio = sem mensagens
            timeStamp = System.currentTimeMillis()
        )
    )

    MaterialTheme {
        ChatScreenContent(
            chatScreenUiState = chatScreenUiState,
            drawerState = rememberDrawerState(DrawerValue.Closed),
            chatHistory = ChatHistoryDrawerUiState(chatHistory = emptyList()),
            coroutineScope = rememberCoroutineScope(),
            onHideDrawer = {},
            onShowDrawer = {},
            onDeleteChat = {},
            onDeleteAllChat = {},
            onGetGptResponse = {},
            subject = ""
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreviewWithMessages() {
    val fakeHistory = listOf(
        ChatHistory(
            id = 1,
            userMessage = "Kotlin",
            aiResponse = "Esta é uma resposta simulada do Assistente.",
            timestamp = System.currentTimeMillis()
        )
    )

    MaterialTheme {
        ChatScreenContent(
            chatScreenUiState = ChatScreenUiState(),
            drawerState = rememberDrawerState(DrawerValue.Closed),
            chatHistory = ChatHistoryDrawerUiState(chatHistory = fakeHistory),
            coroutineScope = rememberCoroutineScope(),
            onHideDrawer = {},
            onShowDrawer = {},
            onDeleteChat = {},
            onDeleteAllChat = {},
            onGetGptResponse = {},
            subject = "Kotlin"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenDrawerPreview() {
    val fakeHistory = listOf(
        ChatHistory(
            id = 1,
            userMessage = "Kotlin",
            aiResponse = "Esta é uma resposta simulada do Assistente.",
            timestamp = System.currentTimeMillis()
        )
    )

    MaterialTheme {
        ChatScreenContent(
            chatScreenUiState = ChatScreenUiState(),
            drawerState = rememberDrawerState(DrawerValue.Open),
            chatHistory = ChatHistoryDrawerUiState(chatHistory = fakeHistory),
            coroutineScope = rememberCoroutineScope(),
            onHideDrawer = {},
            onShowDrawer = {},
            onDeleteChat = {},
            onDeleteAllChat = {},
            onGetGptResponse = {},
            subject = "Kotlin"
        )
    }
}

