package com.example.learneverythingbot.utils

import androidx.compose.ui.graphics.toArgb
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun ColorPreview() {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    // Print no Logcat também
    LaunchedEffect(Unit) {
        Log.d("Colors", "Tertiary: " + String.format("#%08X", tertiary.toArgb()))
        Log.d("Colors", "SurfaceVariant: " + String.format("#%08X", surfaceVariant.toArgb()))
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(tertiary)
                .padding(8.dp)
        ) {
            Text(
                text = "Tertiary: " + String.format(
                    Locale.getDefault(),
                    "#%08X",
                    tertiary.toArgb()
                ),
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(surfaceVariant)
                .padding(8.dp)
        ) {
            Text(
                text = "SurfaceVariant: " + String.format(
                    Locale.getDefault(),
                    "#%08X",
                    surfaceVariant.toArgb()
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorPreview() {
    MaterialTheme {
        ColorPreview()
    }
}
/*
import com.example.learneverythingbot.data.remote.ChatRemoteDataSource
import com.example.learneverythingbot.data.remote.retrofit.OpenAiService
import com.example.learneverythingbot.data.remote.retrofit.RetrofitClient
import com.example.learneverythingbot.domain.model.QuizQuestion
import com.example.learneverythingbot.domain.model.TopicItem
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

suspend fun main() {
    try {
        val service = RetrofitClient.retrofitInstance.create(OpenAiService::class.java)
        val remoteDataSource = ChatRemoteDataSource(service)

        val gptFirstResponse = """
            Introdução ao Kotlin
            Kotlin no contexto de sua linguagem nativa

            Kotlin é uma linguagem de programação moderna e concisa, criada pela JetBrains, totalmente interoperável com Java. É oficialmente suportada pelo Google como linguagem prioritária para desenvolvimento Android.

            Conceitos principais:
            Sintaxe concisa e segura: Reduz boilerplate, facilita leitura e evita erros comuns (como NullPointerException, graças ao sistema de tipos nulos).
            Interoperabilidade com Java: É possível usar bibliotecas Java diretamente, tornando a migração ou integração suave.
            Orientação a objetos + programação funcional: Suporte a lambdas, coleções imutáveis, funções de ordem superior, entre outros.
            Tipagem estática e inferência de tipo: Declarações como val nome = \"João\" já deduzem o tipo, mantendo segurança em tempo de compilação.
            Corrotinas: Permitem programação assíncrona e concorrente de forma leve e eficiente.

            Aplicações práticas:
            Android: Kotlin é a linguagem padrão para apps Android modernos.
            Backend: Usado com frameworks como Ktor, Spring Boot e Micronaut.
            Multiplataforma: Com Kotlin Multiplatform, é possível compartilhar código entre Android, iOS, web e desktop.

            Pontos-chave:
            val (imutável) e var (mutável) substituem final e var do Java.
            Funções podem ser declaradas fora de classes: fun somar(a: Int, b: Int) = a + b
            Classes data já vêm com equals(), hashCode() e toString() prontos.
            Suporte nativo a null safety: String? define variável que pode ser nula.
            Extensões permitem adicionar funções a classes existentes sem herdá-las.

            Kotlin se destaca pela clareza, robustez e produtividade, sendo ideal para iniciantes e experientes que buscam escrever código moderno e eficiente.
        """.trimIndent()

        val result = remoteDataSource.getQuizQuestions("Kotlin", "Introducao ao Kotlin", gptFirstResponse)
        if (result.isSuccess) {
            val value = result.getOrNull()
            if (value != null) {
                val quizList = parseQuizQuestionsFromAiResponse(value)
                println(value)
                quizList.forEach { println(it) }
            } else {
                println("Error: empty response from API")
            }
        } else {
            println("Erro: ${result.exceptionOrNull()?.message}")
        }
    } finally {
        RetrofitClient.cancelAll()
        RetrofitClient.shutdown()
        exitProcess(0)
    }
}





fun parsearTopicos(string: String) {
    val resultado = parseTopics(response = string)
    resultado.forEach { println(it) }
}

fun parseTopics(response: String): List<TopicItem> {
    return response.lines()
        .filter { it.isNotBlank() }
        .map { line ->
            val trimmed = line.trim()
            val level = when {
                Regex("^\\d+\\.\\d+").containsMatchIn(trimmed) -> 1
                Regex("^\\d+\\.").containsMatchIn(trimmed) -> 0
                else -> 0
            }
            val cleanedTitle = trimmed
                .replaceFirst(Regex("^\\d+(\\.\\d+)?\\s*"), "")
                .replaceFirst(Regex("^\\.\\s*"), "")
                .trim()
            TopicItem(title = cleanedTitle, level = level)
        }
}*/
