package com.example.learneverythingbot.presentation
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.ChatRepository
import com.example.learneverythingbot.domain.model.SubTopics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubTopicViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _subTopicContent = mutableStateOf("")
    val subTopicContent: State<String> = _subTopicContent

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _currentSubTopic = mutableStateOf<SubTopics?>(null)
    val currentSubTopic: State<SubTopics?> = _currentSubTopic

    fun loadSubTopicContent(topic: String, subTopic: String, subTopicId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _currentSubTopic.value = SubTopics(
                id = subTopicId,
                parentTopic = topic,
                title = subTopic,
                level = 0
            )

            val result = chatRepository.getSubTopicSummary(topic, subTopic)
            _isLoading.value = false

            result.onSuccess { content ->
                _subTopicContent.value = content
            }.onFailure {
                _subTopicContent.value = "Erro ao carregar conteúdo: ${it.message}"
            }
        }
    }

    fun clearContent() {
        _subTopicContent.value = ""
        _currentSubTopic.value = null
    }
}