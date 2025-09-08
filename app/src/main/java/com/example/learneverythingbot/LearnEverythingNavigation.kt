package com.example.learneverythingbot

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learneverythingbot.presentation.screen.IntroScreen
import com.example.learneverythingbot.presentation.screen.QuizScreen
import com.example.learneverythingbot.presentation.screen.Topic
import com.example.learneverythingbot.presentation.screen.TopicScreen

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
        composable(route = "quizScreen") {

            /*data class Topic(
                val id: String,
                val name: String,
                val description: String,
                val difficulty: String,
                val questionCount: Int
            )*/
            val topic = Topic(
                id = "1",
                name = "",
                description = "",
                difficulty = "",
                questionCount = 1
            )
            QuizScreen(
                topic = topic,
                navController = navController,
                onFinishQuiz = {}
            )
        }
    }
}