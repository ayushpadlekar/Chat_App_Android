package com.ayushxp.chatapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun NewChatFab(navController: NavHostController) {

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    ExtendedFloatingActionButton(
        icon = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Chat",
                tint = primaryCol
            )
        },
        text = { Text("New Chat", fontWeight = FontWeight.SemiBold, color = primaryCol) },
        onClick = { navController.navigate("newchat") },
        containerColor = secondaryCol,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
        )
    )
}