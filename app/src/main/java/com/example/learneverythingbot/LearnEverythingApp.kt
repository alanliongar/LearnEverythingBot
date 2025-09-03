package com.example.learneverythingbot

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learneverythingbot.presentation.screen.IntroScreen
import com.example.learneverythingbot.presentation.screen.QuizScreen
import com.example.learneverythingbot.screen.ChatScreen

@Composable
fun LearnEverythingApp(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "introScreen") {
        composable(route = "introScreen") {
            IntroScreen(innerPadding = innerPadding, navController = navController)
        }
        composable(
            route = "chatScreen?subject={subject}",
            arguments = listOf(
                navArgument("subject") {
                    type = NavType.StringType
                    defaultValue = "Geral"   // valor padrão
                }
            )
        ) { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject").orEmpty()
            ChatScreen(subject = subject)
        }
        composable(route = "quizScreen") {
            QuizScreen(navController = navController)
        }
    }
}