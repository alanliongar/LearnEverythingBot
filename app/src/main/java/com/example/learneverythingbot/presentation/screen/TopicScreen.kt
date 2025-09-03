package com.example.learneverythingbot.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.learneverythingbot.components.ChatHistoryDrawer
import com.example.learneverythingbot.domain.model.ChatHistoryDrawerUiState
import com.example.learneverythingbot.domain.model.ChatMessage
import com.example.learneverythingbot.domain.model.ChatScreenUiState
import com.example.learneverythingbot.domain.model.HistoryItem
import com.example.learneverythingbot.domain.model.Role
import com.example.learneverythingbot.domain.model.TopicHistoryDrawerUiState
import com.example.learneverythingbot.domain.model.TopicItem
import com.example.learneverythingbot.domain.model.TopicScreenUiState
import com.example.learneverythingbot.presentation.TopicViewModel
import com.example.learneverythingbot.presentation.screen.components.MessageInputBar
import com.example.learneverythingbot.screen.AssistantText
import com.example.learneverythingbot.screen.UserBubble
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopicScreen(
    subject: String,
    topicViewModel: TopicViewModel = hiltViewModel()
) {
    val chatHistory by topicViewModel.topicHistoryDrawerUiState.collectAsState()
    val chatScreenUiState by topicViewModel.topicScreenUiState.collectAsState()
    val drawerVisible by topicViewModel.drawerVisible.collectAsState()
    val drawerState =
        rememberDrawerState(if (drawerVisible) DrawerValue.Open else DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    TopicScreenContent(
        topicScreenUiState = chatScreenUiState,
        drawerState = drawerState,
        topicHistory = chatHistory,
        coroutineScope = coroutineScope,
        onHideDrawer = { topicViewModel.hideDrawer() },
        onShowDrawer = { topicViewModel.showDrawer() },
        onDeleteTopic = { topicViewModel.deleteChat(it) },
        onDeleteAllTopic = { topicViewModel.deleteAllChat() },
        onGetGptResponse = { topicViewModel.getGptResponse(it) },
        subject = subject
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicScreenContent(
    modifier: Modifier = Modifier,
    topicScreenUiState: TopicScreenUiState,
    drawerState: DrawerState,
    topicHistory: TopicHistoryDrawerUiState,
    coroutineScope: CoroutineScope,
    onHideDrawer: () -> Unit,
    onShowDrawer: () -> Unit,
    onDeleteTopic: (Int) -> Unit,
    onDeleteAllTopic: () -> Unit,
    onGetGptResponse: (String) -> Unit,
    subject: String,
) {
    val historyMessages = remember(topicHistory.topicHistoryItems) {
        topicHistory.topicHistoryItems
            .sortedBy { it.timestamp }
            .flatMap { item ->
                buildList {
                    if (item.userMessage.isNotBlank()) add(ChatMessage(Role.User, item.userMessage))
                    if (item.aiResponse.isNotBlank()) add(
                        ChatMessage(
                            Role.Assistant,
                            item.aiResponse
                        )
                    )
                }
            }
    }
    val isGenericSubject = subject.isBlank() || subject.equals("Geral", ignoreCase = true)
    var isTyping by remember { mutableStateOf(false) }

    LaunchedEffect(topicScreenUiState.chat.aiAnswer) {
        if (topicScreenUiState.chat.aiAnswer.isNotBlank()) {
            isTyping = false
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatHistoryDrawer(
                allChats = topicHistory.topicHistoryItems,
                onChatSelected = {
                    onHideDrawer()
                    coroutineScope.launch { drawerState.close() }
                },
                onChatDeleted = { onDeleteTopic(it.id) },
                onClearAll = { onDeleteAllTopic() }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(if (isGenericSubject) "Learn Everything Bot" else "Assunto: ${subject.trim()}")
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
                MessageInputBar(
                    onMessageSend = { userText ->
                        isTyping = true
                        onGetGptResponse(userText)
                    }
                )
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
fun TopicList(modifier: Modifier, topics: List<TopicItem>, onClick: (TopicItem) -> Unit) {
    Column(modifier = modifier) {
        topics.forEach { topic ->
            Button(
                onClick = { onClick(topic) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = (topic.level * 16).dp, top = 4.dp, bottom = 4.dp)
            ) {
                Text(text = topic.title)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TopicListPreview() {
    val topicList = listOf(
        TopicItem(title = "Kotlin", level = 0),
        TopicItem(title = "Sintaxe Básica", level = 1),
        TopicItem(title = "Funções", level = 1),
        TopicItem(title = "Lambdas", level = 2),
        TopicItem(title = "Coroutines", level = 1)
    )
    TopicList(modifier = Modifier, topics = topicList) {}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreviewWithMessages() {
    val fakeHistoryItem = listOf(
        HistoryItem(
            id = 1,
            userMessage = "Kotlin",
            aiResponse =
                "1. Introdução ao Kotlin\n" +
                        "   1.1 História e propósito\n" +
                        "   1.2 Características principais\n" +
                        "2. Sintaxe do Kotlin\n" +
                        "   2.1 Variáveis e tipos de dados\n" +
                        "   2.2 Estruturas de controle\n" +
                        "3. Funcionalidades avançadas do Kotlin\n" +
                        "   3.1 Funções de extensão\n" +
                        "   3.2 Null safety\n" +
                        "4. Programação Orientada a Objetos em Kotlin\n" +
                        "   4.1 Classes e objetos\n" +
                        "   4.2 Herança e interfaces\n" +
                        "5. Programação Funcional em Kotlin\n" +
                        "   5.1 Funções de alta ordem\n" +
                        "   5.2 Lambdas\n" +
                        "6. Kotlin para desenvolvimento Android\n" +
                        "   6.1 Vantagens do uso de Kotlin no desenvolvimento Android\n" +
                        "   6.2 Integração com o Android Studio",
            timestamp = System.currentTimeMillis()
        )
    )

    MaterialTheme {
        TopicScreenContent(
            topicScreenUiState = TopicScreenUiState(),
            drawerState = rememberDrawerState(DrawerValue.Closed),
            topicHistory = TopicHistoryDrawerUiState(fakeHistoryItem),
            coroutineScope = rememberCoroutineScope(),
            onHideDrawer = {},
            onShowDrawer = {},
            onDeleteTopic = {},
            onDeleteAllTopic = {},
            onGetGptResponse = {},
            subject = "Kotlin"
        )
    }
}