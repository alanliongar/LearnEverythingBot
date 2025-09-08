package com.example.learneverythingbot.presentation.screen

import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavController
import com.example.learneverythingbot.components.ChatHistoryDrawer
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import com.example.learneverythingbot.domain.model.TopicHistoryDrawerUiState
import com.example.learneverythingbot.domain.model.TopicItem
import com.example.learneverythingbot.domain.model.TopicScreenUiState
import com.example.learneverythingbot.presentation.TopicViewModel
import com.example.learneverythingbot.presentation.screen.components.MessageInputBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopicScreen(
    navController: NavController,
    topicViewModel: TopicViewModel = hiltViewModel()
) {
    val chatHistory by topicViewModel.topicHistoryDrawerUiState.collectAsState()
    val chatScreenUiState by topicViewModel.topicScreenUiState.collectAsState()
    val drawerVisible by topicViewModel.drawerVisible.collectAsState()
    val parsedTopics by topicViewModel.parsedTopics.collectAsState()

    val drawerState =
        rememberDrawerState(if (drawerVisible) DrawerValue.Open else DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    TopicScreenContent(
        topicScreenUiState = chatScreenUiState,
        parsedTopics = parsedTopics,
        drawerState = drawerState,
        topicHistory = chatHistory,
        coroutineScope = coroutineScope,
        onDrawerItemSelected = {
            topicViewModel.parseHistoryItem(it)
        },
        onHideDrawer = { topicViewModel.hideDrawer() },
        onShowDrawer = { topicViewModel.showDrawer() },
        onDeleteTopic = { topicViewModel.deleteChat(it) },
        onDeleteAllTopic = { topicViewModel.deleteAllChat() },
        onGetGptResponse = { topicViewModel.getGptResponse(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicScreenContent(
    modifier: Modifier = Modifier,
    topicScreenUiState: TopicScreenUiState,
    parsedTopics: List<TopicItem>,
    drawerState: DrawerState,
    topicHistory: TopicHistoryDrawerUiState,
    coroutineScope: CoroutineScope,
    onDrawerItemSelected: (ChatHistoryItem) -> Unit,
    onHideDrawer: () -> Unit,
    onShowDrawer: () -> Unit,
    onDeleteTopic: (Int) -> Unit,
    onDeleteAllTopic: () -> Unit,
    onGetGptResponse: (String) -> Unit,
) {
    var currentSubject: String by remember { mutableStateOf("") }
    var isGenericSubject: Boolean by remember {
        mutableStateOf(
            currentSubject.isBlank() || currentSubject.equals(
                "Geral",
                ignoreCase = true
            )
        )
    }
    var isTyping by remember { mutableStateOf(false) }
    var messageSent by remember { mutableStateOf(false) }

    LaunchedEffect(topicScreenUiState.chat.aiAnswer) {
        val answer = topicScreenUiState.chat.aiAnswer
        if (answer.isNotBlank()) {
            isTyping = false
            messageSent = true
        }
    }

    LaunchedEffect(currentSubject, isGenericSubject) {
        isGenericSubject =
            currentSubject.isBlank() || currentSubject.equals("Geral", ignoreCase = true)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatHistoryDrawer(
                allChats = topicHistory.topicChatHistoryItems,
                onChatSelected = { selected ->
                    onHideDrawer()
                    coroutineScope.launch { drawerState.close() }
                    currentSubject = selected.userMessage
                    onDrawerItemSelected.invoke(selected)
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
                        Text(if (isGenericSubject) "Learn Everything Bot" else "Assunto: ${currentSubject.trim()}")
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
                if (!messageSent) {
                    MessageInputBar(
                        onMessageSend = { userText ->
                            isTyping = true
                            onGetGptResponse(userText)
                            currentSubject = userText
                        }
                    )
                }
            }
        ) { inner ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
            ) {
                when {
                    isTyping -> {
                        Text(
                            text = "Gerando estrutura de tópicos...",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    parsedTopics.isNotEmpty() -> {
                        TopicList(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 16.dp),
                            topics = parsedTopics,
                            onClick = { /* Ação de navegação pra tela de resumo */ }
                        )
                    }

                    else -> {
                        Text(
                            text = stringResource(id = com.example.learneverythingbot.R.string.initial_prompt),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopicList(
    modifier: Modifier,
    topics: List<TopicItem>,
    onClick: (TopicItem) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(topics) { topic ->
            Button(
                onClick = { onClick.invoke(topic) },
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
    val fakeChatHistoryItem = listOf(
        ChatHistoryItem(
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
    val parsedTopics: List<TopicItem> = listOf(
        TopicItem(title = "Kotlin", level = 0),
        TopicItem(title = "Sintaxe Básica", level = 1),
        TopicItem(title = "Funções", level = 1),
        TopicItem(title = "Lambdas", level = 2),
        TopicItem(title = "Coroutines", level = 1)
    )

    MaterialTheme {
        TopicScreenContent(
            topicScreenUiState = TopicScreenUiState(),
            parsedTopics = parsedTopics,
            drawerState = rememberDrawerState(DrawerValue.Closed),
            topicHistory = TopicHistoryDrawerUiState(fakeChatHistoryItem),
            coroutineScope = rememberCoroutineScope(),
            onDrawerItemSelected = {},
            onHideDrawer = {},
            onShowDrawer = {},
            onDeleteTopic = {},
            onDeleteAllTopic = {},
            onGetGptResponse = {},
        )
    }
}