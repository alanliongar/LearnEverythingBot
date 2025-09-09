package com.example.learneverythingbot.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.learneverythingbot.domain.model.QuizQuestion
import com.example.learneverythingbot.domain.model.SubTopics

// utils/SubTopicParser.kt
object SubTopicParser {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun parseResponse(response: String, mainTopic: String): List<SubTopics> {
        val subTopics = mutableListOf<SubTopics>()
        val lines = response.split("\n")

        var currentLevel = 0
        var parentStack = mutableListOf<String>()

        lines.forEach { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("Topico/assunto")) {
                val level = calculateLevel(line)
                val title = extractCleanTitle(line)

                if (title.isNotBlank()) {
                    // Limpa a stack para o nível atual
                    while (parentStack.size > level) {
                        parentStack.removeLast()
                    }

                    subTopics.add(SubTopics(
                        parentTopic = mainTopic,
                        title = title,
                        level = level
                    ))
                }
            }
        }

        return subTopics
    }

    suspend fun parseQuizQuestionsFromAiResponse(response: String): List<QuizQuestion> {
        val questionBlocks = response.split(Regex("(?=Pergunta \\d+:)"))
        val questions = mutableListOf<QuizQuestion>()

        for ((index, block) in questionBlocks.withIndex()) {
            val lines = block.lines().map { it.trim() }.filter { it.isNotBlank() }

            val answerIndex = lines.indexOfLast { it.lowercase().startsWith("resposta:") }
            if (answerIndex < 6) continue

            val enunciadoRaw = lines.getOrNull(0)
                ?.replace(Regex("^Pergunta \\d+:\\s*"), "")
                ?.trim()
                ?: continue

            val alternativas = lines.subList(1, answerIndex).joinToString("\n")
            val stem = "$enunciadoRaw\n$alternativas".trim()

            val rightAnswer = lines[answerIndex]
                .substringAfter(":")
                .trim()
                .lowercase()
                .takeIf { it in listOf("a", "b", "c", "d", "e") }
                ?: continue

            questions.add(
                QuizQuestion(
                    stem = stem,
                    level = index, // nível de dificuldade com base na ordem
                    rightAnswer = rightAnswer
                )
            )
        }

        return questions
    }

    private fun calculateLevel(line: String): Int {
        val indentPattern = Regex("^([│\\s├└──]+)")
        val match = indentPattern.find(line)
        return match?.value?.count { it == '│' || it == '├' || it == '└' } ?: 0
    }

    private fun extractCleanTitle(line: String): String {
        return line.replace(Regex("^[│\\s├└──]+"), "").trim()
    }
}