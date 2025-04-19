package com.ayushxp.chatapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayushxp.chatapp.ui.screens.AuthScreen
import com.ayushxp.chatapp.ui.screens.ChatListScreen

@Composable
fun Navigation(navController: NavHostController, startDest: String) {

    NavHost(navController = navController, startDestination = startDest) {
        composable("auth") {
            AuthScreen(navController)
        }
        composable("chatlist") {
            ChatListScreen(navController)
        }
    }
}
