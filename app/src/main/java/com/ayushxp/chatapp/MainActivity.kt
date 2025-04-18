package com.ayushxp.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ayushxp.chatapp.ui.screens.AuthScreen
import com.ayushxp.chatapp.ui.theme.ChatAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppTheme(darkTheme = false, dynamicColor = false) {
                AuthScreen()
            }
        }
    }
}
