package com.example.learneverythingbot.data.remote

import android.util.Log
import com.example.learneverythingbot.data.model.Message
import com.example.learneverythingbot.data.model.OpenAiRequest
import com.example.learneverythingbot.data.remote.retrofit.OpenAiService
import javax.inject.Inject

class ChatRemoteDataSource @Inject constructor(
    private val openAiService: OpenAiService
) : RemoteDataSource {
    override suspend fun learnChatTopicGptResponse(topic: String): Result<String> {
        val prompt =
            """Você é um assistente educacional. Dado o assunto $topic, devolva **apenas** a estrutura de tópicos principais e subtópicos para estudar o tema.
                Formato da resposta:
                1. Tópico 1
                   1.1 Subtópico A
                   1.2 Subtópico B
                2. Tópico 2
                   2.1 Subtópico C
                   2.2 Subtópico D
            """.trimIndent()

        val request = OpenAiRequest(
            messages = listOf(
                Message("system", "Você é um especialista em aprendizado eficiente e direto."),
                Message("user", prompt)
            )
        )

        return try {
            val response = openAiService.getLearningTopics(request)
            if (response.isSuccessful) {
                val learningTopics = response.body()?.choices?.firstOrNull()?.message?.content
                    ?: "Erro ao gerar conteúdo."
                Result.success(learningTopics)
            } else {
                Result.failure(
                    Exception(
                        "❌ Requisition error: ${
                            response.errorBody()?.string() ?: "Unknown error."
                        }"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(Exception("🚨 Unexpected error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }

    override suspend fun getQuizQuestions(topic: String, subTopic: String, summary: String): Result<String> {
        val prompt = """
        Crie 3 questões de múltipla escolha sobre o subtema '$subTopic' dentro do assunto '$topic'.
        As perguntas devem ser objetivas, de nível introdutório, com 5 alternativas cada (a–e) e apenas uma correta, 
        a primeira pergunta deve ser de nivel facil, a segunda de nivel medio, a terceira de nivel dificil.
        Considere o seguinte conteudo que foi gerado anteriormente para as duas primeiras questoes: $summary
        .
        
        Formato da resposta (obrigatório):
        Pergunta 1:
        <enunciado>
        a) <alternativa A>
        b) <alternativa B>
        c) <alternativa C>
        d) <alternativa D>
        e) <alternativa E>
        Resposta: <letra correta>
        
        Pergunta 2:
        ...
        
        Pergunta 3:
        ...
        
        Não inclua explicações, comentários ou texto fora do padrão acima, lembrando que sao cinco alternativas pra cada questao,
        sendo elas de a a e.
        E a resposta deve conter apenas a letra, somente a letra.
        """.trimIndent()

        val request = OpenAiRequest(
            messages = listOf(
                Message("system", "Você é um especialista em criar perguntas inteligentes e diretas."),
                Message("user", prompt)
            )
        )

        return try {
            val response = openAiService.getLearningTopics(request)
            if (response.isSuccessful) {
                val content = response.body()?.choices?.firstOrNull()?.message?.content
                    ?: "Conteúdo não disponível."
                Result.success(content)
            } else {
                Result.failure(Exception("Erro na requisição do subtópico: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro inesperado: ${e.message}"))
        }
    }


    override suspend fun getSubTopicSummary(
        topic: String,
        subTopic: String
    ): Result<String> {
        val prompt = """
        Forneça um resumo conciso e direto sobre '$subTopic' no contexto de '$topic'.
        Inclua os conceitos principais, aplicações práticas e pontos-chave para compreensão.
        Seja objetivo e direto ao ponto. Máximo de 300 palavras.
        """.trimIndent()


        Log.d("ChatRemoteDataSource", "Prompt: $prompt  $topic  $subTopic")

        val request = OpenAiRequest(
            messages = listOf(
                Message("system", "Você é um especialista em fornecer resumos claros e objetivos."),
                Message("user", prompt)
            )
        )

        return try {
            val response = openAiService.getLearningTopics(request)
            if (response.isSuccessful) {
                val content = response.body()?.choices?.firstOrNull()?.message?.content
                    ?: "Conteúdo não disponível."
                Result.success(content)
            } else {
                Result.failure(Exception("Erro na requisição do subtópico: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro inesperado: ${e.message}"))
        }
    }
}