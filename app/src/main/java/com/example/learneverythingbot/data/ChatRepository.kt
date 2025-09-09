package com.example.learneverythingbot.data

import android.util.Log
import com.example.learneverythingbot.data.local.LocalDataSource
import com.example.learneverythingbot.data.remote.RemoteDataSource
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import com.example.learneverythingbot.domain.model.QuizQuestion
import com.example.learneverythingbot.domain.model.SummaryItem
import com.example.learneverythingbot.utils.SubTopicParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) {
    suspend fun countByTopic(topic: String): Int {
        return local.countByTopic(topic = topic)
    }

    suspend fun getQuizQuestions(topic: String): List<QuizQuestion>? {
        val quizQuestions =
            local.getQuizQuestionsByTopic(topic = topic)
        if (!quizQuestions.isNullOrEmpty()) {
            return quizQuestions
        } else {
            return emptyList()
        }
    }

    private suspend fun generateQuizQuestions(
        topic: String,
        subTopic: String,
        resultSummary: String
    ): Result<String> {
        try {
            val result =
                remote.getQuizQuestions(topic = topic, subTopic = subTopic, summary = resultSummary)
            if (result.isSuccess) {
                val resultOrNull = result.getOrNull()
                if (resultOrNull != null && resultOrNull != "") {
                    return Result.success(resultOrNull)
                } else {
                    return Result.failure(Exception("Empty request"))
                }
            } else {
                return Result.failure(Exception("Something went wrong"))
            }

        } catch (ex: Exception) {
            return Result.failure(Exception(ex.message ?: "Something went wrong"))
        }
    }


    suspend fun generateSummary(topic: String, subTopic: String): Result<SummaryItem> {
        val localSummary = local.getSummaryByTopicAndSubTopic(topic = topic, subTopic = subTopic)
        if (localSummary != null && localSummary != "") {
            val summaryitem = SummaryItem(
                topic = topic, subTopic = subTopic, secondAiResponse = localSummary
            )
            return Result.success(summaryitem)
        } else {
            try {
                val result = remote.getSubTopicSummary(topic = topic, subTopic = subTopic)
                if (result.isSuccess) {
                    val resultRemote = result.getOrNull()
                    if (resultRemote != null) {
                        val successfulResultRemote = SummaryItem(
                            secondAiResponse = resultRemote,
                            topic = topic,
                            subTopic = subTopic
                        )
                        local.insertSummary(
                            topic = topic,
                            subTopic = subTopic,
                            secondAiResponse = resultRemote
                        )
                        val verifyIfQuestionsExist =
                            local.getQuizQuestionsByTopicAndSubTopic(
                                topic = topic,
                                subTopic = subTopic
                            )
                        if (verifyIfQuestionsExist == null || verifyIfQuestionsExist.isEmpty()) {
                            val generatedQuiz = generateQuizQuestions(
                                topic = topic,
                                subTopic = subTopic,
                                resultSummary = resultRemote
                            )
                            if (generatedQuiz.isSuccess && generatedQuiz.getOrNull() != null && generatedQuiz.getOrNull() != "") {
                                val quizQuestions: List<QuizQuestion> =
                                    SubTopicParser.parseQuizQuestionsFromAiResponse(generatedQuiz.getOrNull()!!)

                                local.insertQuizQuestions(
                                    topic = topic,
                                    subTopic = subTopic,
                                    questions = quizQuestions
                                )
                            } else {
                                Log.d(
                                    "Chat Repository",
                                    "Something went wrong while generating quiz at $topic and $subTopic."
                                )
                            }
                        }
                        return Result.success(
                            successfulResultRemote
                        )
                    } else {
                        return Result.failure(Exception("Empty Response"))
                    }
                } else {
                    return Result.failure(Exception("Something went wrong!"))
                }

            } catch (ex: Exception) {
                return Result.failure(Exception(ex.message ?: "Unknown Error"))
            }
        }
    }

    suspend fun getGptResponse(topic: String): Result<ChatHistoryItem> {
        return try {
            val result = remote.learnChatTopicGptResponse(topic = topic)
            if (result.isSuccess) {
                val remoteGptResponse = result.getOrNull() ?: ""
                if (remoteGptResponse.isNotEmpty()) {
                    val chatHistoryItem = ChatHistoryItem(
                        userMessage = topic,
                        aiResponse = remoteGptResponse
                    )
                    Result.success(chatHistoryItem)
                } else {
                    Result.failure(Exception("Resposta vazia da API"))
                }
            } else {
                Result.failure(Exception("Falha na chamada da API"))
            }
        } catch (ex: Exception) {
            Result.failure(Exception(ex.message ?: "Erro desconhecido"))
        }
    }

    suspend fun getAllTopicHistory(): Flow<List<ChatHistoryItem>> {
        val placeholder = ChatHistoryItem("", "", System.currentTimeMillis())
        return local.getAllChatHistory() //já vem do IO
            .map { list -> if (list.isEmpty()) listOf(placeholder) else list }
    }

    suspend fun deleteAllTopic() {
        local.deleteAllTopic()
    }

    suspend fun insertTopic(chatHistoryItem: ChatHistoryItem) {
        local.insertTopicHistory(chatHistoryItem = chatHistoryItem)
    }

    suspend fun insertChat(chatHistoryItem: ChatHistoryItem) {
        local.insertChatHistory(chatHistoryItem = chatHistoryItem)
    }

    suspend fun deleteChat(userMessage: String) {
        local.deleteChat(topic = userMessage)
    }

    suspend fun deleteAllChat() {
        local.deleteAllChat()
    }

    suspend fun getAllChatHistory(): Flow<List<ChatHistoryItem>> {
        return local.getAllChatHistory()
    }

    suspend fun getChatById(userMessage: String): ChatHistoryItem? {
        return local.getChatByTopic(userMessage)
    }
}