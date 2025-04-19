package com.ayushxp.chatapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayushxp.chatapp.ui.theme.ChatAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavHostController) {

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    Scaffold(
        containerColor = Color.White,
        topBar = { // Simple Top Bar for App name & User account
            TopAppBar(
                title =
                    {
                        Text(
                            "Chat App",
                            color = primaryCol,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                actions =
                    {
                        IconButton(
                            onClick = {  }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                tint = primaryCol,
                                modifier = Modifier.size(30.dp),
                                contentDescription = "Account"
                            )
                        }
                    },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = secondaryCol)
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ) {
            Text("Chat List Screen")
        }
    }
}

// Preview
 @Preview
 @Composable
 fun ChatListScreenPreview() {
     ChatAppTheme(darkTheme = false, dynamicColor = false) {
         ChatListScreen(rememberNavController())
     }
 }