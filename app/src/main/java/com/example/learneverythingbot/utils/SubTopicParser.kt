package com.example.learneverythingbot.utils

import android.os.Build
import androidx.annotation.RequiresApi
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

    private fun calculateLevel(line: String): Int {
        val indentPattern = Regex("^([│\\s├└──]+)")
        val match = indentPattern.find(line)
        return match?.value?.count { it == '│' || it == '├' || it == '└' } ?: 0
    }

    private fun extractCleanTitle(line: String): String {
        return line.replace(Regex("^[│\\s├└──]+"), "").trim()
    }
}