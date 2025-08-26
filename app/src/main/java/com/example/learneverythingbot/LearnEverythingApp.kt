package com.example.learneverythingbot

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learneverythingbot.presentation.screen.ChatScreen
import com.example.learneverythingbot.presentation.screen.IntroScreen
import com.example.learneverythingbot.presentation.screen.QuizScreen

@Composable
fun LearnEverythingApp(innerPadding: PaddingValues, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "introScreen") {
        composable(route = "introScreen") {
            IntroScreen(innerPadding = innerPadding, navController = navController)
        }
        composable(route = "chatScreen") {
            ChatScreen()
        }

        composable("quizScreen") {
            QuizScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}