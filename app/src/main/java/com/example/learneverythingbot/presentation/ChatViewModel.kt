package com.example.learneverythingbot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.ChatRepository
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.Chat
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import com.example.learneverythingbot.domain.model.ChatHistoryDrawerUiState
import com.example.learneverythingbot.domain.model.ChatScreenUiState
import com.example.learneverythingbot.domain.model.SubTopics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    @DispatcherIO val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _chatScreenUiState = MutableStateFlow(ChatScreenUiState())
    val chatScreenUiState: StateFlow<ChatScreenUiState> = _chatScreenUiState.asStateFlow()

    private val _chatHistoryDrawerUiState = MutableStateFlow(ChatHistoryDrawerUiState())
    val chatHistoryDrawerUiState: StateFlow<ChatHistoryDrawerUiState> =
        _chatHistoryDrawerUiState.asStateFlow()

    private val _drawerVisible = MutableStateFlow(false)
    val drawerVisible: StateFlow<Boolean> = _drawerVisible.asStateFlow()

    private val _navigateToChatUsrMsg = MutableStateFlow<String?>(null)
    val navigateToChatUsrMsg: StateFlow<String?> = _navigateToChatUsrMsg.asStateFlow()

    private val _currentChat = MutableStateFlow<ChatHistoryItem?>(null)
    val currentChat: StateFlow<ChatHistoryItem?> = _currentChat.asStateFlow()

    private val _isLoadingChat = MutableStateFlow(false)
    val isLoadingChat: StateFlow<Boolean> = _isLoadingChat.asStateFlow()

    init {
        loadChatHistory()
    }

    fun getGptResponse(topic: String) {
        val topicUpdated = sanitize(topic)
        viewModelScope.launch(dispatcher) {
            _chatScreenUiState.value = ChatScreenUiState(
                isLoading = true,
                error = null
            )

            val result = chatRepository.getGptResponse(topicUpdated)

            if (result.isSuccess) {
                val chatHistory = result.getOrThrow()
                val userMessage = sanitizeResponse(chatHistory.userMessage)
                val updatedChatHistory = chatHistory.copy(
                    userMessage = userMessage
                )
                println("Alannn + " + userMessage)
                // Insere o chat no banco (isso gerará um ID único)
                chatRepository.insertChat(updatedChatHistory)

                // Recarrega o histórico para ter o ID correto
                loadChatHistory()

                _chatScreenUiState.value = ChatScreenUiState(
                    chat = Chat(
                        subject = updatedChatHistory.userMessage,
                        aiAnswer = updatedChatHistory.aiResponse,
                        timeStamp = updatedChatHistory.timestamp
                    ),
                    isLoading = false
                )
            } else {
                _chatScreenUiState.value = ChatScreenUiState(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Erro desconhecido"
                )
            }
        }
    }


    fun loadChatHistory() {
        viewModelScope.launch(dispatcher) {
            try {
                chatRepository.getAllChatHistory().collect { chatHistoryList ->
                    _chatHistoryDrawerUiState.value = ChatHistoryDrawerUiState(chatHistoryList)
                }
            } catch (e: Exception) {
                println("Erro ao carregar histórico: ${e.message}")
            }
        }
    }


    fun extractSubTopicsFromResponse(response: String, topic: String): List<SubTopics> {
        val subTopics = mutableListOf<SubTopics>()
        val lines = response.split("\n")

        lines.forEach { line ->
            if (line.trim().isNotEmpty() && !line.startsWith("Topico/assunto")) {
                val level = countIndentationLevel(line)
                val title = extractTitle(line)

                if (title.isNotBlank() && !title.contains("├──") && !title.contains("└──") && !title.contains(
                        "│"
                    )
                ) {
                    subTopics.add(
                        SubTopics(
                            parentTopic = topic,
                            title = title,
                            level = level
                        )
                    )
                }
            }
        }
        return subTopics
    }

    private fun countIndentationLevel(line: String): Int {
        return when {
            line.startsWith("        ") -> 3 // 8 espaços - nível 3
            line.startsWith("    ") -> 2 // 4 espaços - nível 2
            line.startsWith("  ") -> 1 // 2 espaços - nível 1
            else -> 0 // Sem indentação - nível 0
        }
    }

    private fun extractTitle(line: String): String {
        return line.trim().replace(Regex("^[├└│\\s─]*"), "").trim()
    }

    fun loadChatByUsrMsg(userMessage: String) {
        viewModelScope.launch(dispatcher) {
            _isLoadingChat.value = true
            try {
                val chat = chatRepository.getChatById(userMessage)
                _currentChat.value = chat
            } catch (e: Exception) {
                println("Erro ao carregar chat: ${e.message}")
            } finally {
                _isLoadingChat.value = false
            }
        }
    }

    fun showDrawer() {
        _drawerVisible.value = true
    }

    fun hideDrawer() {
        _drawerVisible.value = false
    }

    fun selectChat(chatHistoryItem: ChatHistoryItem) {
        _navigateToChatUsrMsg.value = chatHistoryItem.userMessage
    }

    fun resetNavigation() {
        _navigateToChatUsrMsg.value = null
        _currentChat.value = null
    }

    fun deleteChat(userMessage: String) {
        viewModelScope.launch(dispatcher) {
            chatRepository.deleteChat(userMessage)
            loadChatHistory()

            if (_currentChat.value?.userMessage == userMessage) {
                _currentChat.value = null
            }
        }
    }

    fun deleteAllChat() {
        viewModelScope.launch(dispatcher) {
            chatRepository.deleteAllChat()
            loadChatHistory()
            _currentChat.value = null
        }
    }
}

private fun sanitize(input: String): String {
    val forbidden = Regex("""[\\/:*?"<>|`$&;]""")   // banir
    val invisibles = Regex("""[\u0000\u00A0\u200B-\u200D\uFEFF\u202A-\u202E\u2066-\u2069]""")
    val smartQuotes = Regex("""[“”‘’]""")
    val longDashes = Regex("""[–—]""")
    return input
        .replace(forbidden, "_")
        .replace(invisibles, "")
        .replace(smartQuotes, "\"")
        .replace(longDashes, "-")
}

private fun sanitizeResponse(input: String): String {
    // Mantém quebras de linha e espaços exatamente como vieram.
    // Remove apenas marcadores invisíveis problemáticos e normaliza pontuações “bonitas”.
    // Substitui caracteres perigosos por "_".
    val forbidden = Regex("""[\\/:*?"<>|`$&;]""")
    val invisiblesExceptSpacesAndNewlines =
        Regex("""[\u200B-\u200D\uFEFF\u202A-\u202E\u2066-\u2069]""")
    val smartQuotes = Regex("""[“”‘’]""")
    val longDashes = Regex("""[–—]""")

    return input
        .replace(forbidden, "_")
        .replace(invisiblesExceptSpacesAndNewlines, "")
        .replace(smartQuotes, "\"")
        .replace(longDashes, "-")
}