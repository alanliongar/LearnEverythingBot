package com.example.learneverythingbot.presentation.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.learneverythingbot.presentation.QuizViewModel
import com.example.learneverythingbot.presentation.screen.components.ErrorComponent
import com.example.learneverythingbot.presentation.screen.components.LoadingComponent
import com.example.learneverythingbot.presentation.screen.ui.theme.Error
import com.example.learneverythingbot.presentation.screen.ui.theme.Success
import com.example.learneverythingbot.presentation.screen.ui.theme.Warning
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    topic: String,
    navController: NavController,
    viewModel: QuizViewModel = hiltViewModel<QuizViewModel>()
) {
    val coroutineScope = rememberCoroutineScope()
    viewModel.loadQuestions(topic = topic)
    val questions by viewModel.questions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }

    var score by remember { mutableStateOf(0) }

    LaunchedEffect(topic) {
        viewModel.loadQuestions(topic)   // carrega uma vez por tópico
        currentQuestionIndex = 0
        selectedAnswer = null
        score = 0
    }

    QuizScreenContent(
        questions = questions,
        isLoading = isLoading,
        error = error,
        currentQuestionIndex = currentQuestionIndex,
        onMoveForewardCurrentQuestion = { currentQuestionIndex++ },
        selectedAnswer = selectedAnswer,
        onSelectedAnswer = { selectedAnswer = it },
        onMatchAnswer = { score++ },
        onFinishQuiz = {
            viewModel.updateResults(score = score, topic = topic) { res ->
                coroutineScope.launch {
                    navController.navigate("resultScreen/${res.topic}/${res.score}/${res.totalQuestions}")
                }
            }
        },
        popBackStack = { navController.popBackStack() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizScreenContent(
    questions: List<Question>,
    isLoading: Boolean,
    error: Boolean?,
    currentQuestionIndex: Int,
    onMoveForewardCurrentQuestion: () -> Unit,
    selectedAnswer: Int?,
    onSelectedAnswer: (Int?) -> Unit,
    onMatchAnswer: () -> Unit,
    onFinishQuiz: () -> Unit,
    popBackStack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.height(52.dp), // altura menor que o padrão (56.dp)
                windowInsets = WindowInsets(0.dp) // remove insets extras
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    LoadingComponent()
                }

                error == true -> {
                    ErrorComponent(
                        error = "Something went wrong!",
                        onRetry = {
                            popBackStack.invoke()
                        }
                    )
                }

                questions.isNotEmpty() -> {
                    // Header con progresso
                    QuizProgressHeader(
                        currentQuestion = currentQuestionIndex,
                        totalQuestions = questions.size,
                        difficulty = when (questions[currentQuestionIndex].dificulty) {
                            1 -> "Fácil"
                            2 -> "Médio"
                            3 -> "Difícil"
                            else -> "Tanto faz"
                        }
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Pergunta
                    Text(
                        text = questions[currentQuestionIndex].text,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Opções de resposta
                    questions[currentQuestionIndex].options.forEachIndexed { index, option ->
                        AnswerOption(
                            option = option,
                            index = index,
                            isSelected = selectedAnswer == index,
                            onClick = {
                                // permite trocar e "desselecionar"
                                val next = if (selectedAnswer == index) null else index
                                onSelectedAnswer(next)
                                /*if (next != null && selectedAnswer == null &&
                                    next == questions[currentQuestionIndex].correctAnswer
                                ) {
                                    onMatchAnswer()
                                }*/
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }


                    // Botão de próximo
                    if (selectedAnswer != null) {
                        Spacer(modifier = Modifier.height(28.dp))
                        Button(
                            onClick = {
                                val chosenIndex = selectedAnswer

                                // verifica se bate com o índice da resposta correta
                                if (chosenIndex != null &&
                                    chosenIndex == questions[currentQuestionIndex].correctAnswer
                                ) {
                                    onMatchAnswer() // soma ponto
                                }
                                if (currentQuestionIndex + 1 < questions.size) {
                                    onMoveForewardCurrentQuestion.invoke()
                                    onSelectedAnswer.invoke(null)
                                } else {
                                    onFinishQuiz.invoke()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (currentQuestionIndex + 1 < questions.size) "Próxima Pergunta" else "Ver Resultados",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
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
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Teste seus conhecimentos e aprenda de forma divertida",
            color = MaterialTheme.colorScheme.secondary,
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
private fun TopicCard(topic: Topic, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.name,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = topic.description,
                    color = MaterialTheme.colorScheme.secondary,
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
                                    "Fácil" -> Success.copy(alpha = 0.2f)
                                    "Médio" -> Warning.copy(alpha = 0.2f)
                                    else -> Error.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = topic.difficulty,
                            color = when (topic.difficulty) {
                                "Fácil" -> Success
                                "Médio" -> Warning
                                else -> Error
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${topic.questionCount} perguntas",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp
                    )
                }
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
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = when (difficulty) {
                            "Fácil" -> Success.copy(alpha = 0.2f)
                            "Médio" -> Warning.copy(alpha = 0.2f)
                            else -> Error.copy(alpha = 0.2f)
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = difficulty,
                    color = when (difficulty) {
                        "Fácil" -> Success
                        "Médio" -> Warning
                        else -> Error
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
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surface,

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
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onSecondary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.outline
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
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${'A' + index}",
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.primary
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
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    score: Int,
    totalQuestions: Int,
    navController: NavController,
    onRetry: () -> Unit,
    onNewTopic: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.height(52.dp), // altura menor que o padrão (56.dp)
                windowInsets = WindowInsets(0.dp) // remove insets extras
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Resultado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = if (score >= totalQuestions * 0.7) "Excelente! 🎉" else "Bom trabalho! 💪",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$score/$totalQuestions",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            val percent = if (totalQuestions == 0) 0 else (score * 100 / totalQuestions)
            Text(
                text = "${percent}% de acerto",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (score >= totalQuestions * 0.7) {
                    "Você dominou este tópico! Continue assim!"
                } else {
                    "Continue praticando para melhorar seu desempenho!"
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
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

                OutlinedButton(
                    onClick = onNewTopic,
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
    val dificulty: Int,
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
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Escolha um tópico para praticar",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Teste seus conhecimentos e aprenda de forma divertida",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        color = MaterialTheme.colorScheme.background
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
                color = MaterialTheme.colorScheme.onPrimary,
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
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
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
        color = MaterialTheme.colorScheme.background
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
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Resultado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Excelente! 🎉",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "9/10",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "90% de acerto",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Você dominou este tópico! Continue assim!",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
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
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onPrimary
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
        color = MaterialTheme.colorScheme.background
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
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Resultado",
                    tint = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Bom trabalho! 💪",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "6/10",
                color = MaterialTheme.colorScheme.surfaceVariant,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "60% de acerto",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Continue praticando para melhorar seu desempenho!",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
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
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onPrimary
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
        color = MaterialTheme.colorScheme.background
    ) {
        val topic = Topic(
            id = "1",
            name = "",
            description = "",
            difficulty = "",
            questionCount = 1
        )
        val navController: NavController
        navController = NavController(context = LocalContext.current)
        QuizScreen(
            topic = topic.name,
            navController = navController,
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
        color = MaterialTheme.colorScheme.background
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
        color = MaterialTheme.colorScheme.background
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