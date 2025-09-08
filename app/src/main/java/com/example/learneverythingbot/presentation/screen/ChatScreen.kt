package com.example.learneverythingbot.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.learneverythingbot.components.ChatHistoryDrawer
import com.example.learneverythingbot.presentation.screen.components.MessageInputBar
import com.example.learneverythingbot.domain.model.ChatMessage
import com.example.learneverythingbot.domain.model.Role
import com.example.learneverythingbot.presentation.screen.ui.theme.Purple40
import com.example.learneverythingbot.presentation.ChatViewModel
import com.example.learneverythingbot.presentation.screen.components.AssistantText
import com.example.learneverythingbot.presentation.screen.components.SubTopicButton
import com.example.learneverythingbot.presentation.screen.components.UserBubble
import com.example.learneverythingbot.utils.SubTopicParser
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    initialSubject: String,
    navController: NavHostController,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // ADICIONE ESTA LINHA
    var currentSubject by remember { mutableStateOf(initialSubject) }

    val chatHistory by chatViewModel.chatHistoryDrawerUiState.collectAsState()
    val chatScreenUiState by chatViewModel.chatScreenUiState.collectAsState()
    val drawerVisible by chatViewModel.drawerVisible.collectAsState()
    val navigateToChatId by chatViewModel.navigateToChatId.collectAsState()

    val drawerState =
        rememberDrawerState(if (drawerVisible) DrawerValue.Open else DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }

    val shouldNavigate by remember { mutableStateOf(false) }

    LaunchedEffect(chatScreenUiState.chat.aiAnswer) {
        val answer = chatScreenUiState.chat.aiAnswer
        if (answer.isNotBlank()) {
            val subTopics = SubTopicParser.parseResponse(answer, currentSubject) // MUDEI AQUI

            val newMessage = if (messages.isNotEmpty() && messages.last().role == Role.Assistant) {
                messages.dropLast(1) + ChatMessage(
                    Role.Assistant,
                    answer,
                    subTopics
                )
            } else {
                messages + ChatMessage(Role.Assistant, answer, subTopics)
            }

            messages = newMessage
        }
    }

    LaunchedEffect(navigateToChatId) {
        navigateToChatId?.let { chatId ->
            if (chatId > 0) {
                println("Navegando para chat ID: $chatId")
                navController.navigate("chatHistoryDetail?chatId=$chatId")
                chatViewModel.resetNavigation()
                chatViewModel.hideDrawer()
                coroutineScope.launch {
                    drawerState.close()
                }
            }
        }
    }

    if (!chatScreenUiState.error.isNullOrEmpty()) {
        LaunchedEffect(chatScreenUiState.error) {
            println("Erro: ${chatScreenUiState.error}")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatHistoryDrawer(
                allChats = chatHistory.chatHistoryItems,
                onChatSelected = { chatHistory ->
                    chatViewModel.selectChat(chatHistory)
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
                            text = if (currentSubject.isNotEmpty()) // MUDEI AQUI
                                "Assunto: $currentSubject" // MUDEI AQUI
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
                if (currentSubject.isNotEmpty()) { // MUDEI AQUI
                    MessageInputBar(
                        onMessageSend = { userText ->
                            // ADICIONE ESTAS LINHAS
                            if (messages.isEmpty() && currentSubject.isEmpty()) {
                                currentSubject = userText.take(30)
                            }

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
                                Role.Assistant -> {
                                    Column {
                                        if (msg.subTopics.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(12.dp))
                                            msg.subTopics.forEach { subTopic ->
                                                SubTopicButton(
                                                    subTopic = subTopic,
                                                    onClick = {
                                                        navController.navigate("subTopicDetail/${currentSubject}/${subTopic.title}") // MUDEI AQUI
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (chatScreenUiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(48.dp)
                        )
                    }
                }
            }
        }
    }
}