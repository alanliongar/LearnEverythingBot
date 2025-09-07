package com.example.learneverythingbot.data.repository

import com.example.learneverythingbot.domain.model.Question
import com.example.learneverythingbot.domain.model.Topic
import kotlinx.coroutines.delay

class QuizRepository {

    // Simula la generación de preguntas por IA (luego se conectará a API real)
    suspend fun generateQuestions(topic: Topic): List<Question> {
        // Simular delay de red
        delay(2000)

        return when (topic.name) {
            "Matemática Básica" -> listOf(
                Question("1", "¿Cuánto es 2 + 2?", listOf("3", "4", "5", "6"), 1),
                Question("2", "¿Cuánto es 8 × 7?", listOf("54", "56", "64", "72"), 1),
                Question("3", "¿Cuánto es 15 ÷ 3?", listOf("3", "5", "6", "8"), 1),
                Question("4", "¿Cuál es el resultado de 9²?", listOf("18", "81", "72", "90"), 1),
                Question("5", "¿Cuánto es ¼ + ½?", listOf("¾", "½", "¼", "1"), 0)
            )
            "Programação Kotlin" -> listOf(
                Question("1", "¿Qué palabra clave declara variable inmutable?",
                    listOf("var", "val", "let", "const"), 1),
                Question("2", "¿Qué función imprime en consola?",
                    listOf("print()", "console()", "log()", "write()"), 0),
                Question("3", "¿Cómo se declara una función en Kotlin?",
                    listOf("function", "def", "fun", "fn"), 2)
            )
            "Inteligência Artificial" -> listOf(
                Question("1", "¿Qué significa IA?",
                    listOf("Inteligencia Artificial", "Interfaz Avanzada",
                        "Internet Acelerado", "Innovación Automatizada"), 0),
                Question("2", "¿Qué es el machine learning?",
                    listOf("Aprender máquinas", "Aprendizaje automático",
                        "Máquinas que aprenden", "Automatización inteligente"), 1)
            )
            else -> listOf(
                Question("1", "¿Cuál es la capital de Portugal?",
                    listOf("Madrid", "Lisboa", "Paris", "Roma"), 1),
                Question("2", "¿Quién pintó la Mona Lisa?",
                    listOf("Picasso", "Van Gogh", "Da Vinci", "Monet"), 2)
            )
        }.take(topic.questionCount) // Limitar al número de preguntas del topic
    }
}