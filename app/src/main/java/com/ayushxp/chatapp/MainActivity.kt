package com.ayushxp.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ayushxp.chatapp.data.repository.AuthRepository
import com.ayushxp.chatapp.ui.Navigation
import com.ayushxp.chatapp.ui.theme.ChatAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val authRepo = AuthRepository()
        val startDest = if(authRepo.isLoggedin()) "chatlist" else "auth"

        setContent {
            ChatAppTheme(darkTheme = false, dynamicColor = false) {
                val navController = rememberNavController()
                Navigation(navController, startDest)
            }
        }
    }
}
