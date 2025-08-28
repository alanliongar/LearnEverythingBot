package com.example.learneverythingbot.presentation.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learneverythingbot.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(onBackClick: () -> Unit) {
    var currentScreen by remember { mutableStateOf("topics") }
    var selectedTopic by remember { mutableStateOf<Topic?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentScreen) {
                            "topics" -> "Tópicos de Aprendizagem"
                            "quiz" -> selectedTopic?.name ?: "Quiz"
                            "results" -> "Resultados"
                            else -> "Quiz"
                        },
                        color = colorResource(id = R.color.text_on_primary),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        when (currentScreen) {
                            "topics" -> onBackClick()
                            "quiz" -> currentScreen = "topics"
                            "results" -> currentScreen = "topics"
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = colorResource(id = R.color.text_on_primary)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.primary)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorResource(id = R.color.background_dark))
        ) {
            when (currentScreen) {
                "topics" -> TopicSelectionScreen(
                    onTopicSelected = { topic ->
                        selectedTopic = topic
                        currentScreen = "quiz"
                    }
                )
                "quiz" -> QuestionScreen(
                    topic = selectedTopic!!,
                    onFinishQuiz = { currentScreen = "results" }
                )
                "results" -> ResultsScreen(
                    onRetry = { currentScreen = "quiz" },
                    onNewTopic = { currentScreen = "topics" }
                )
            }
        }
    }
}

@Composable
fun TopicSelectionScreen(onTopicSelected: (Topic) -> Unit) {
    val topics = listOf(
        Topic("1", "Matemática Básica", "Operações fundamentais", "Fácil", 10),
        Topic("2", "Programação Kotlin", "Conceitos essenciais", "Médio", 8),
        Topic("3", "Ciência de Dados", "Fundamentos de dados", "Difícil", 12),
        Topic("4", "Inteligência Artificial", "Conceitos de IA e ML", "Médio", 15),
        Topic("5", "Desenvolvimento Android", "Android com Compose", "Fácil", 12)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Escolha um tópico para praticar",
            color = colorResource(id = R.color.text_on_primary),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Teste seus conhecimentos e aprenda de forma divertida",
            color = colorResource(id = R.color.text_secondary),
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(topics) { topic ->
                TopicCard(topic = topic, onClick = { onTopicSelected(topic) })
            }
        }
    }
}

@Composable
fun TopicCard(topic: Topic, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface_dark)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = colorResource(id = R.color.secondary).copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = colorResource(id = R.color.secondary),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.name,
                    color = colorResource(id = R.color.text_on_primary),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = topic.description,
                    color = colorResource(id = R.color.text_secondary),
                    fontSize = 14.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Dificuldade
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                color = when (topic.difficulty) {
                                    "Fácil" -> colorResource(id = R.color.success).copy(alpha = 0.2f)
                                    "Médio" -> colorResource(id = R.color.warning).copy(alpha = 0.2f)
                                    else -> colorResource(id = R.color.error).copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = topic.difficulty,
                            color = when (topic.difficulty) {
                                "Fácil" -> colorResource(id = R.color.success)
                                "Médio" -> colorResource(id = R.color.warning)
                                else -> colorResource(id = R.color.error)
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Contagem de perguntas
                    Text(
                        text = "${topic.questionCount} perguntas",
                        color = colorResource(id = R.color.text_secondary),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionScreen(topic: Topic, onFinishQuiz: () -> Unit) {
    var currentQuestion by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }

    val questions = listOf(
        Question(
            "1",
            "Qual é a capital de Portugal?",
            listOf("Madrid", "Lisboa", "Paris", "Roma"),
            1
        ),
        Question(
            "2",
            "Quanto é 8 × 7?",
            listOf("54", "56", "64", "72"),
            1
        ),
        Question(
            "3",
            "Qual linguagem é usada para desenvolvimento Android?",
            listOf("Python", "Java", "Kotlin", "C#"),
            2
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header com progresso
        QuizProgressHeader(
            currentQuestion = currentQuestion + 1,
            totalQuestions = questions.size,
            difficulty = topic.difficulty
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Pergunta
        Text(
            text = questions[currentQuestion].text,
            color = colorResource(id = R.color.text_on_primary),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Opções de resposta
        questions[currentQuestion].options.forEachIndexed { index, option ->
            AnswerOption(
                option = option,
                index = index,
                isSelected = selectedAnswer == index,
                onClick = {
                    if (selectedAnswer == null) {
                        selectedAnswer = index
                        if (index == questions[currentQuestion].correctAnswer) {
                            score++
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Botão de próximo
        if (selectedAnswer != null) {
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = {
                    if (currentQuestion + 1 < questions.size) {
                        currentQuestion++
                        selectedAnswer = null
                    } else {
                        onFinishQuiz()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.secondary),
                    contentColor = colorResource(id = R.color.text_on_secondary)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (currentQuestion + 1 < questions.size) "Próxima Pergunta" else "Ver Resultados",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun QuizProgressHeader(currentQuestion: Int, totalQuestions: Int, difficulty: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Informações do topo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pergunta $currentQuestion/$totalQuestions",
                color = colorResource(id = R.color.text_secondary),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = when (difficulty) {
                            "Fácil" -> colorResource(id = R.color.success).copy(alpha = 0.2f)
                            "Médio" -> colorResource(id = R.color.warning).copy(alpha = 0.2f)
                            else -> colorResource(id = R.color.error).copy(alpha = 0.2f)
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = difficulty,
                    color = when (difficulty) {
                        "Fácil" -> colorResource(id = R.color.success)
                        "Médio" -> colorResource(id = R.color.warning)
                        else -> colorResource(id = R.color.error)
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Barra de progresso
        LinearProgressIndicator(
            progress = { currentQuestion.toFloat() / totalQuestions.toFloat() },
            color = colorResource(id = R.color.primary),
            trackColor = colorResource(id = R.color.surface_dark),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun AnswerOption(option: String, index: Int, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) {
        colorResource(id = R.color.secondary)
    } else {
        colorResource(id = R.color.surface_dark)
    }

    val textColor = if (isSelected) {
        colorResource(id = R.color.text_on_secondary)
    } else {
        colorResource(id = R.color.text_on_primary)
    }

    val borderColor = if (isSelected) {
        colorResource(id = R.color.secondary)
    } else {
        colorResource(id = R.color.border_dark)
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador da letra
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isSelected) {
                            colorResource(id = R.color.text_on_secondary)
                        } else {
                            colorResource(id = R.color.primary).copy(alpha = 0.1f)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${'A' + index}",
                    color = if (isSelected) {
                        colorResource(id = R.color.secondary)
                    } else {
                        colorResource(id = R.color.primary)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Texto da opção
            Text(
                text = option,
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                lineHeight = 24.sp
            )

            // Ícone de seleção
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                    tint = colorResource(id = R.color.text_on_secondary),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ResultsScreen(onRetry: () -> Unit, onNewTopic: () -> Unit) {
    var score by remember { mutableStateOf(8) } // Exemplo: 8/10 acertos
    val totalQuestions = 10

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Medalha/Ícone de resultado
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = colorResource(id = R.color.primary).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Resultado",
                tint = colorResource(id = R.color.primary),
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Título baseado no desempenho
        Text(
            text = if (score >= totalQuestions * 0.7) "Excelente! 🎉" else "Bom trabalho! 💪",
            color = colorResource(id = R.color.text_on_primary),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pontuação
        Text(
            text = "$score/$totalQuestions",
            color = colorResource(id = R.color.primary),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Percentual
        Text(
            text = "${(score * 100 / totalQuestions)}% de acerto",
            color = colorResource(id = R.color.text_secondary),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mensagem de feedback
        Text(
            text = if (score >= totalQuestions * 0.7) {
                "Você dominou este tópico! Continue assim!"
            } else {
                "Continue praticando para melhorar seu desempenho!"
            },
            color = colorResource(id = R.color.text_secondary),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Botões de ação
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.secondary),
                    contentColor = colorResource(id = R.color.text_on_secondary)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Tentar Novamente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = onNewTopic,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.surface_dark),
                    contentColor = colorResource(id = R.color.text_on_primary)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Escolher Outro Tópico",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// Data classes
data class Topic(
    val id: String,
    val name: String,
    val description: String,
    val difficulty: String,
    val questionCount: Int
)

data class Question(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctAnswer: Int
)

// Previews (mantenha sus previews existentes com as cores atualizadas)

// Previews para QuizScreen
@Preview(showBackground = true, name = "Seleção de Tópicos Preview")
@Composable
fun TopicSelectionPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.background_dark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Escolha um tópico para praticar",
                color = colorResource(id = R.color.text_on_primary),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Teste seus conhecimentos e aprenda de forma divertida",
                color = colorResource(id = R.color.text_secondary),
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Preview de cards de tópicos
            TopicCard(
                topic = Topic(
                    "1",
                    "Matemática Básica",
                    "Operações fundamentais e conceitos básicos",
                    "Fácil",
                    10
                ),
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            TopicCard(
                topic = Topic(
                    "2",
                    "Programação Kotlin",
                    "Conceitos essenciais de programação com Kotlin",
                    "Médio",
                    8
                ),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Tela de Pergunta Preview")
@Composable
fun QuestionScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.background_dark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header com progresso
            QuizProgressHeader(
                currentQuestion = 2,
                totalQuestions = 5,
                difficulty = "Médio"
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Pergunta
            Text(
                text = "Qual linguagem é oficial para desenvolvimento Android?",
                color = colorResource(id = R.color.text_on_primary),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Opções de resposta
            AnswerOption(
                option = "Python",
                index = 0,
                isSelected = false,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnswerOption(
                option = "Java",
                index = 1,
                isSelected = false,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnswerOption(
                option = "Kotlin",
                index = 2,
                isSelected = true,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnswerOption(
                option = "C#",
                index = 3,
                isSelected = false,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botão de próximo
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.secondary),
                    contentColor = colorResource(id = R.color.text_on_secondary)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Próxima Pergunta",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Tela de Resultados Preview - Sucesso")
@Composable
fun ResultsScreenSuccessPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.background_dark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Medalha/Ícone de resultado
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = colorResource(id = R.color.primary).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Resultado",
                    tint = colorResource(id = R.color.primary),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Excelente! 🎉",
                color = colorResource(id = R.color.text_on_primary),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "9/10",
                color = colorResource(id = R.color.primary),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "90% de acerto",
                color = colorResource(id = R.color.text_secondary),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Você dominou este tópico! Continue assim!",
                color = colorResource(id = R.color.text_secondary),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.secondary),
                        contentColor = colorResource(id = R.color.text_on_secondary)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Tentar Novamente", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.surface_dark),
                        contentColor = colorResource(id = R.color.text_on_primary)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Escolher Outro Tópico", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Tela de Resultados Preview - Melhorar")
@Composable
fun ResultsScreenImprovePreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.background_dark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = colorResource(id = R.color.warning).copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Resultado",
                    tint = colorResource(id = R.color.warning),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Bom trabalho! 💪",
                color = colorResource(id = R.color.text_on_primary),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "6/10",
                color = colorResource(id = R.color.warning),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "60% de acerto",
                color = colorResource(id = R.color.text_secondary),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Continue praticando para melhorar seu desempenho!",
                color = colorResource(id = R.color.text_secondary),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.secondary),
                        contentColor = colorResource(id = R.color.text_on_secondary)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Tentar Novamente", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.surface_dark),
                        contentColor = colorResource(id = R.color.text_on_primary)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Escolher Outro Tópico", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "QuizScreen Completo Preview")
@Composable
fun QuizScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.background_dark)
    ) {
        QuizScreen(
            onBackClick = {} // Função vazia para preview
        )
    }
}

@Preview(showBackground = true, name = "Header de Progresso Preview")
@Composable
fun QuizProgressHeaderPreview() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        color = colorResource(id = R.color.background_dark)
    ) {
        QuizProgressHeader(
            currentQuestion = 3,
            totalQuestions = 8,
            difficulty = "Difícil"
        )
    }
}

@Preview(showBackground = true, name = "Opção de Resposta Preview")
@Composable
fun AnswerOptionPreview() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        color = colorResource(id = R.color.background_dark)
    ) {
        Column {
            AnswerOption(
                option = "Opção selecionada corretamente",
                index = 0,
                isSelected = true,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnswerOption(
                option = "Opção não selecionada",
                index = 1,
                isSelected = false,
                onClick = {}
            )
        }
    }
}