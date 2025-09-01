package com.example.learneverythingbot

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learneverythingbot.presentation.screen.ChatHistoryDetailScreen
import com.example.learneverythingbot.presentation.screen.IntroScreen
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
                    defaultValue = "Geral"
                }
            )
        ) { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject").orEmpty()
            ChatScreen(subject = subject, navController = navController)
        }

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
    }
}