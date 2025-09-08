package com.example.learneverythingbot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learneverythingbot.presentation.screen.ChatHistoryDetailScreen
import com.example.learneverythingbot.presentation.screen.IntroScreen
import com.example.learneverythingbot.presentation.screen.QuizScreen
import com.example.learneverythingbot.presentation.screen.SubTopicDetailScreen
import com.example.learneverythingbot.presentation.screen.Topic
import com.example.learneverythingbot.presentation.screen.TopicScreen
import com.example.learneverythingbot.screen.ChatScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun LearnEverythingNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "introScreen") {

        composable(route = "introScreen") {
            IntroScreen(navController = navController)
        }

        /*composable(
            route = "chatScreen?subject={subject}",
            arguments = listOf(
                navArgument("subject") {
                    type = NavType.StringType
                    defaultValue = "Geral"
                }
            )
        ) { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject").orEmpty()
            ChatScreen(initialSubject = subject, navController = navController)
        }*/

        composable(
            route = "chatHistoryDetail?chatId={chatId}",
            arguments = listOf(
                navArgument("chatId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getInt("chatId") ?: -1
            println("Navegando para detalhes do chat ID: $chatId")
            ChatHistoryDetailScreen(
                chatId = chatId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("subTopicDetail/{topic}/{subTopic}") { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic")
            val subTopic = backStackEntry.arguments?.getString("subTopic")
            SubTopicDetailScreen(
                topic = topic,
                subTopic = subTopic,
                navController = navController
            )
        }

        composable(route = "topicScreen") {
            TopicScreen(navController = navController)
        }
        composable(route = "quizScreen") {
            val topic = Topic(
                id = "1",
                name = "",
                description = "",
                difficulty = "",
                questionCount = 1
            )
            QuizScreen(topic = topic, navController = navController, onFinishQuiz = {})
        }


    }
}