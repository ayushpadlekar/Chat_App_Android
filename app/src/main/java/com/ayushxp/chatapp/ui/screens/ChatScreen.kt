package com.ayushxp.chatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.West
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.ayushxp.chatapp.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId: String, otherUserId: String, username: String, navController: NavHostController) {

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    val viewModel: ChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    var messageInput by remember { mutableStateOf(TextFieldValue("")) }

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Start loading messages when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadMessages(chatId)
    }

    Scaffold(
        containerColor = secondaryCol, // color for top & bottom bar- both.
        topBar = {
            TopAppBar(
                title = { Text(username) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.West, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(secondaryCol)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField( // Text Input Field
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(corner = CornerSize(30.dp))),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = primaryCol,
                        unfocusedIndicatorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton( // Send Button
                    onClick = {
                        val text = messageInput.text.trim()
                        if (text.isNotEmpty()) {
                            viewModel.sendMessage(chatId, text, otherUserId)
                            messageInput = TextFieldValue("")
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = primaryCol),
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.padding(5.dp))
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color.White)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.size) { index ->
                val message = messages[index]
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (message.sender == currentUserId) {
                        Alignment.End
                    } else {
                        Alignment.Start
                    }
                ) {
                    Text(
                        text = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                            .format(message.timestamp.toDate()), // Convert Timestamp to Date
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    // Message Card Ui
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if(message.sender == currentUserId) secondaryCol else Color.White
                        ),
                        modifier = Modifier.padding(
                            start = if(message.sender == currentUserId) 30.dp else 0.dp,
                            end = if(message.sender != currentUserId) 30.dp else 0.dp,
                        ),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Text(
                            text = message.text,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}