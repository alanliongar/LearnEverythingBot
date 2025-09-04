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
            """
            Quero aprender os conhecimentos de $topic, 
            me devolva um plano de estudos efetivo e simples, organizado 
            em estrutura de pacotes, devolva somente a estrutura de pacotes.
            Atente-se a essa instrução, pois é importante: você deve devolver especificamente numa estrutura de tópicos.
            A seguir está um exemplo. Responda **SOMENTE** nesse formato, e somente com o conteúdo direto.
            Topico/assunto
            ├── subtopico-nivel1
            │   ├── subtopico-nivel2
            │   └── subtopico-nivel2
            ├── subtopico-nivel1
            │   ├── subtopico-nivel2
            │   ├── subtopico-nivel2
            │   ├── subtopico-nivel2
            │   ├── subtopico-nivel2
            │   └── subtopico-nivel2
            """.trimIndent()
        //Árvore de diretórios é diferente de estrutura de pacotes, tomar cuidado

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