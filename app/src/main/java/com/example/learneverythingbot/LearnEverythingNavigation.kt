package com.example.learneverythingbot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learneverythingbot.presentation.screen.IntroScreen
import com.example.learneverythingbot.presentation.screen.QuizScreen
import com.example.learneverythingbot.presentation.screen.ResultsScreen
import com.example.learneverythingbot.presentation.screen.SubTopicDetailScreen
import com.example.learneverythingbot.presentation.screen.Topic
import com.example.learneverythingbot.presentation.screen.TopicScreen
import com.example.learneverythingbot.screen.ChatScreen

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun LearnEverythingNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "introScreen") {

        composable(route = "introScreen") {
            IntroScreen(navController = navController)
        }
        composable(route = "topicScreen") {
            TopicScreen(navController = navController)
        }

        composable(
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
        composable(
            route = "quizScreen/{topic}",
            arguments = listOf(
                navArgument(name = "topic") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic").orEmpty()
            println("Alannn + " + topic)
            QuizScreen(topic = topic, navController = navController)
        }

        composable(
            route = "resultScreen/{topic}/{score}/{totalQuestions}",
            arguments = listOf(
                navArgument("topic") { type = NavType.StringType },
                navArgument("score") { type = NavType.IntType },
                navArgument("totalQuestions") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic").orEmpty()
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("totalQuestions") ?: 0

            ResultsScreen(
                score = score,
                totalQuestions = total,
                navController = navController,
                onRetry = {
                    navController.navigate("quizScreen/$topic") {
                        popUpTo("quizScreen/$topic") { inclusive = true }
                    }
                },
                onNewTopic = {
                    navController.navigate("topicScreen") {
                        popUpTo("topicScreen") { inclusive = true }
                    }
                }
            )
        }

    }
}