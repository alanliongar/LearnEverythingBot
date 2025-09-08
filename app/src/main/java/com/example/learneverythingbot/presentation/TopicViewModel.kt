package com.example.learneverythingbot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.ChatRepository
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import com.example.learneverythingbot.domain.model.Topic
import com.example.learneverythingbot.domain.model.TopicHistoryDrawerUiState
import com.example.learneverythingbot.domain.model.TopicItem
import com.example.learneverythingbot.domain.model.TopicScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val repository: ChatRepository,
    @DispatcherIO val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _drawerVisible = MutableStateFlow(false)
    val drawerVisible: StateFlow<Boolean> = _drawerVisible

    private val _topicScreenUiState = MutableStateFlow(TopicScreenUiState())
    val topicScreenUiState: StateFlow<TopicScreenUiState> = _topicScreenUiState

    private val _topicHistoryDrawerUiState =
        MutableStateFlow(TopicHistoryDrawerUiState())
    val topicHistoryDrawerUiState: StateFlow<TopicHistoryDrawerUiState> = _topicHistoryDrawerUiState

    private val _parsedTopics: MutableStateFlow<List<TopicItem>> =
        MutableStateFlow<List<TopicItem>>(emptyList())

    val parsedTopics: StateFlow<List<TopicItem>> = _parsedTopics

    init {
        viewModelScope.launch(context = dispatcher) {
            _topicHistoryDrawerUiState.value = TopicHistoryDrawerUiState(isLoading = true)
            repository.getAllTopicHistory().collect { history ->
                _topicHistoryDrawerUiState.value = TopicHistoryDrawerUiState(
                    topicChatHistoryItems = history
                )
            }
        }
    }

    fun toggleDrawer() {
        _drawerVisible.value = !_drawerVisible.value
    }

    fun showDrawer() {
        _drawerVisible.value = true
    }

    fun hideDrawer() {
        _drawerVisible.value = false
    }

    fun deleteChat(id: Int) {

    }

    fun deleteAllChat() {
        viewModelScope.launch(context = dispatcher) {
            repository.deleteAllTopic()
        }
    }

    fun parseHistoryItem(chatHistoryItem: ChatHistoryItem) {
        _parsedTopics.value = parseTopics(chatHistoryItem.aiResponse)
    }

    private fun parseTopics(response: String): List<TopicItem> {
        return if (response != "") response.lines()
            .filter { it.isNotBlank() }
            .map { line ->
                val trimmed = line.trim()
                val level = when {
                    Regex("^\\d+\\.\\d+").containsMatchIn(trimmed) -> 1
                    Regex("^\\d+\\.").containsMatchIn(trimmed) -> 0
                    else -> 0
                }
                val cleanedTitle = trimmed
                    .replaceFirst(Regex("^\\d+(\\.\\d+)?\\s*"), "") // remove prefixo "1." ou "1.2"
                    .replaceFirst(Regex("^\\.\\s*"), "") // remove "." isolado no início
                    .trim()
                TopicItem(title = cleanedTitle, level = level)
            } else emptyList()
    }

    fun getGptResponse(userMessage: String) {
        viewModelScope.launch(context = dispatcher) {
            _topicScreenUiState.value = TopicScreenUiState(isLoading = true)
            val result = repository.getGptResponse(topic = userMessage)
            if (result.isSuccess && result.getOrNull() != null) {
                val fullResponse = requireNotNull(result.getOrNull()!!)
                _topicScreenUiState.value = TopicScreenUiState(
                    chat = Topic(
                        subject = userMessage,
                        aiAnswer = fullResponse.aiResponse,
                        timeStamp = fullResponse.timestamp
                    )
                )
                _parsedTopics.value = parseTopics(fullResponse.aiResponse)
                saveTopic(
                    userMessage = userMessage,
                    aiResponse = fullResponse.aiResponse,
                    timeStamp = fullResponse.timestamp
                )
            } else {
                _topicScreenUiState.value = TopicScreenUiState(isError = true)
            }
        }
    }

    fun saveTopic(userMessage: String, aiResponse: String, timeStamp: Long) {
        viewModelScope.launch(context = dispatcher) {
            _topicScreenUiState.value = TopicScreenUiState(isLoading = true)
            val chatHistoryItem = ChatHistoryItem(
                userMessage = userMessage,
                aiResponse = aiResponse,
                timestamp = timeStamp
            )
            repository.insertTopic(chatHistoryItem = chatHistoryItem)
            _topicScreenUiState.value = TopicScreenUiState(
                chat = Topic(
                    subject = userMessage,
                    aiAnswer = aiResponse,
                    timeStamp = timeStamp
                )
            )
        }
    }
}