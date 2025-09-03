package com.example.learneverythingbot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.ChatRepository
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.HistoryItem
import com.example.learneverythingbot.domain.model.Topic
import com.example.learneverythingbot.domain.model.TopicHistoryDrawerUiState
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

    init {
        viewModelScope.launch(context = dispatcher) {
            _topicHistoryDrawerUiState.value = TopicHistoryDrawerUiState(isLoading = true)
            repository.getAllTopicHistory().collect { history ->
                _topicHistoryDrawerUiState.value = TopicHistoryDrawerUiState(
                    topicHistoryItems = history
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
            val historyItem = HistoryItem(
                userMessage = userMessage,
                aiResponse = aiResponse,
                timestamp = timeStamp
            )
            repository.insertTopic(historyItem = historyItem)
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