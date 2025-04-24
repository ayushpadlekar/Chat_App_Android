package com.ayushxp.chatapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayushxp.chatapp.data.model.ChatPreview
import com.ayushxp.chatapp.ui.components.NewChatFab
import com.ayushxp.chatapp.ui.components.NoChatsUi
import com.ayushxp.chatapp.ui.theme.ChatAppTheme
import com.ayushxp.chatapp.viewmodel.AuthViewModel
import com.ayushxp.chatapp.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavHostController) {

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    // ChatViewModel instance & Chat Lists
    val chatVm: ChatViewModel = viewModel()
    val chatList by chatVm.chatList.collectAsState()

    // AuthViewModel instance
    val authVm: AuthViewModel = viewModel()
    val username by authVm.username.collectAsState()
    val email by authVm.email.collectAsState()
    val createdTime by authVm.createdTime.collectAsState()

    // Account info Dialog state
    var accountDialog by remember { mutableStateOf(false) }
//    var username by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var createdOn by remember { mutableStateOf("") }

    // Load chat list when screen opens
    LaunchedEffect(chatVm) {
        chatVm.loadChatsForCurrentUser()
    }

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
                    { // Account Button - for Account info & Sign Out
                        IconButton(
                            onClick = {
                                authVm.getUserAccountDetails()
                                accountDialog = !accountDialog
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                tint = primaryCol,
                                modifier = Modifier.size(30.dp),
                                contentDescription = "Account Info"
                            )
                        }
                    },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = secondaryCol)
            )
        },
        floatingActionButton = { NewChatFab(navController) }
    ) {

        if (chatList.isEmpty()) {
            NoChatsUi(it)
        } else {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 12.dp)
                ) {
                    items(chatList.size) { index ->
                        val chat = chatList[index]
                        ChatItem(chat, onClick = {
                            navController.navigate("chat/${chat.chatId}/${chat.otherUserId}/${chat.otherUsername}")
                        })
                    }
                }
            }
        }

        if(accountDialog) {
            Dialog(
                onDismissRequest = { accountDialog = false }
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .height(300.dp)
                        .clip(RoundedCornerShape(10))
                        .background(Color.White)
                        .padding(horizontal = 20.dp)
                ) {
                    Row {
                        Text(
                            text = "Username: ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = username,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryCol
                        )
                    }

                    Row {
                        Text(
                            text = "Email: ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = email,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryCol
                        )
                    }

                    Row {
                        Text(
                            text = "Created: ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = createdTime,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryCol
                        )
                    }

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            authVm.logout()
                            accountDialog = !accountDialog
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Sign Out", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: ChatPreview, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chat.otherUsername,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                        .format(chat.timestamp.toDate()),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = chat.lastMessage,
                color = Color.DarkGray
            )
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