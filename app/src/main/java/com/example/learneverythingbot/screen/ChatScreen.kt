@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.learneverythingbot.screen

import android.R.attr.value
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learneverythingbot.LearningTopicsRemoteDataSource
import com.example.learneverythingbot.OpenAiService
import com.example.learneverythingbot.RetrofitClient
import com.example.learneverythingbot.components.MessageInputBar
import com.example.learneverythingbot.model.ChatMessage
import com.example.learneverythingbot.model.Role
import com.example.learneverythingbot.ui.theme.Purple40
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun ChatScreen(
    //subject: String,
    onMenuClick: () -> Unit = {},
) {
    var input by rememberSaveable { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onMenuClick) { Icon(Icons.Default.Menu, null) }
                },
                title = {
                    Text(
                        text = stringResource(id= com.example.learneverythingbot.R.string.title_activity_chat, "formatArgs"),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        },
        bottomBar = {
            MessageInputBar(
                onMessageSend = { userText ->
                    messages += ChatMessage(Role.User, userText)
                    GlobalScope.launch(Dispatchers.IO){
                        val response = chatGpt(input = userText)
                        messages += ChatMessage(
                            Role.Assistant,
                            response
                        )
                    }
                }
            )
        }
    ) { inner ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            if (messages.isEmpty()) {
                Text(
                    text = stringResource(id= com.example.learneverythingbot.R.string.initial_prompt),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(start = 16.dp, end = 16.dp),
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

private suspend fun chatGpt(input: String): String {
    val service = RetrofitClient.retrofitInstance.create(OpenAiService::class.java)
    val remoteDataSource = LearningTopicsRemoteDataSource(service)
    val result = remoteDataSource.learningTopicsResponse(input)

    return if (result.isSuccess) {
        result.getOrNull() ?: "Error: empty response from API"
    } else {
        "Erro: ${result.exceptionOrNull()?.message}"
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
        ChatScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreviewWithMessages() {
    MaterialTheme {
        val mockMessages = listOf(
            ChatMessage(Role.User, "Sure thing, I'll have a look today."),
            ChatMessage(Role.Assistant, "Legal! Me diga o que deseja aprender hoje que eu monto um plano para você.")
        )

        CompositionLocalProvider {
            var input by remember { mutableStateOf("") }
            val messages = remember { mutableStateListOf<ChatMessage>().apply { addAll(mockMessages) } }

            ChatScreen()
        }
    }
}
