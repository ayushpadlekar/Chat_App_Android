package com.ayushxp.chatapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayushxp.chatapp.ui.screens.AuthScreen
import com.ayushxp.chatapp.ui.screens.ChatListScreen
import com.ayushxp.chatapp.ui.screens.ChatScreen
import com.ayushxp.chatapp.ui.screens.SearchUserScreen

@Composable
fun Navigation(navController: NavHostController, startDest: String) {

    NavHost(navController = navController, startDestination = startDest) {
        composable("auth") {
            AuthScreen(navController)
        }
        composable("chatlist") {
            ChatListScreen(navController)
        }
        composable("newchat") {
            SearchUserScreen(navController)
        }
        composable("chat/{chatId}/{username}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: "Chat"
            ChatScreen(chatId = chatId, username = username, navController)
        }
    }
}
