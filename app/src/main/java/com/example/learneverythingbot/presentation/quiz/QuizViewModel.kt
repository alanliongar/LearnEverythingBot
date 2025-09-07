package com.example.learneverythingbot.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.repository.QuizRepository
import com.example.learneverythingbot.domain.model.Question
import com.example.learneverythingbot.domain.model.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    private val repository = QuizRepository()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun generateQuestions(topic: Topic) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val generatedQuestions = repository.generateQuestions(topic)
                _questions.value = generatedQuestions
            } catch (e: Exception) {
                _error.value = "Error al generar preguntas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearQuestions() {
        _questions.value = emptyList()
    }
}