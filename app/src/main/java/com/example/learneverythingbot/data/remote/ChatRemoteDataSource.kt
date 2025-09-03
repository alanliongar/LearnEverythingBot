package com.example.learneverythingbot.data.remote

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
        /*Quero aprender os conhecimentos de $topic,
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
            │   └── subtopico-nivel2*/
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
}