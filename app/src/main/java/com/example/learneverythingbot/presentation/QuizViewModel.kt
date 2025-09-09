package com.example.learneverythingbot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.ChatRepository
import com.example.learneverythingbot.data.model.Results
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.QuizQuestion
import com.example.learneverythingbot.presentation.screen.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: ChatRepository,
    @DispatcherIO val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<Boolean?>(null)
    val error: StateFlow<Boolean?> = _error.asStateFlow()

    private val _results = MutableStateFlow<Results>(Results())
    val results: StateFlow<Results> = _results.asStateFlow()

    fun updateResults(
        score: Int,
        topic: String,
        onDone: (Results) -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            val res = Results(
                score = score,
                totalQuestions = repository.countByTopic(topic),
                topic = topic
            )
            _results.value = res
            onDone(res)
        }
    }

    init {

    }

    fun loadQuestions(topic: String) {
        viewModelScope.launch(context = dispatcher) {
            _isLoading.value = true
            val quizQuestionsFromDB = repository.getQuizQuestions(topic = topic)
            println("Alannn + 3 + " + quizQuestionsFromDB.toString())
            if (!quizQuestionsFromDB.isNullOrEmpty()) {
                _questions.value =
                    convertQuizQuestionListToListOfQuestion(quizQuestionsFromDB)
                _error.value = false
                _isLoading.value = false
            } else {
                _isLoading.value = false
                _error.value = true
            }
        }
    }

    private fun convertQuizQuestionListToListOfQuestion(
        quizQuestions: List<QuizQuestion>
    ): List<Question> {
        return quizQuestions.map { quiz ->
            val regex = Regex(
                pattern = """^(.*?)(?=\s*[aA]\))\s*[aA]\)\s*(.*?)\s*[bB]\)\s*(.*?)\s*[cC]\)\s*(.*?)\s*[dD]\)\s*(.*?)\s*[eE]\)\s*(.*?)(?:\s*Resposta:\s*[a-eA-E])?\s*$""",
                options = setOf(RegexOption.DOT_MATCHES_ALL)
            )

            val match = regex.find(quiz.stem)
            val text = match?.groups?.get(1)?.value?.trim() ?: quiz.stem
            val options = listOf(
                match?.groups?.get(2)?.value?.trim().orEmpty(),
                match?.groups?.get(3)?.value?.trim().orEmpty(),
                match?.groups?.get(4)?.value?.trim().orEmpty(),
                match?.groups?.get(5)?.value?.trim().orEmpty(),
                match?.groups?.get(6)?.value?.trim().orEmpty()
            )

            Question(
                id = "0",
                text = text,
                dificulty = quiz.level,
                options = options,
                correctAnswer = when (quiz.rightAnswer.lowercase()) {
                    "a" -> 0
                    "b" -> 1
                    "c" -> 2
                    "d" -> 3
                    "e" -> 4
                    else -> -1
                }
            )
        }
    }
}