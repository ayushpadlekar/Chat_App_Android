package com.ayushxp.chatapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen() {

    // Toggle to switch between Login and SignUp UI
    var isLogin by remember { mutableStateOf(true) }

    // States for login input fields
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    // States for signup input fields
    var signupName by remember { mutableStateOf("") }
    var signupEmail by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    // Scaffold for top-bar & content layout
    Scaffold(
        containerColor = Color.White,
        topBar = {
            // Simple Top Bar for App name
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = secondaryCol)
            )
        }
    ) { innerPadding ->

        // Main Content
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(15.dp)
                .padding(top = 100.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Row with Login and Signup toggles
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Login",
                    color = if (isLogin) primaryCol else tertiaryCol,
                    fontSize = 22.sp,
                    fontWeight = if (isLogin) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.clickable(onClick = { isLogin = true })
                )

                Text(
                    text = "Signup",
                    color = if (!isLogin) primaryCol else tertiaryCol,
                    fontSize = 22.sp,
                    fontWeight = if (!isLogin) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.clickable(onClick = { isLogin = false })
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Card container for Form fields & buttons
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 15.dp),
                shape = CardDefaults.outlinedShape,
                border = BorderStroke(1.dp, primaryCol),
                colors = CardDefaults.outlinedCardColors(Color.White)
            ) {

                if (isLogin) {
                    // Login Form
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 25.dp),
                        verticalArrangement = Arrangement.spacedBy(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Reusable Email TextField
                        EmailField(value = loginEmail, onValueChange = { loginEmail = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol)

                        // Reusable Password TextField
                        PassField(value = loginPassword, onValueChange = { loginPassword = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol)

                        // Submit Button - Login
                        Button(
                            colors = ButtonDefaults.buttonColors(primaryCol),
                            onClick = {
                            }
                        ) {
                            Text("Login", color = Color.White, fontSize = 20.sp)
                        }

                    }
                } else {
                    // Signup Form
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 25.dp),
                        verticalArrangement = Arrangement.spacedBy(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // User-Name TextField
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = signupName,
                            onValueChange = { signupName = it },
                            label = { Text(text = "User name") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = tertiaryCol,
                                focusedBorderColor = primaryCol
                            )
                        )

                        // Reusable Email TextField
                        EmailField(value = signupEmail, onValueChange = { signupEmail = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol)

                        // Reusable Password TextField
                        PassField(value = signupPassword, onValueChange = { signupPassword = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol)

                        // Submit Button - Sign Up
                        Button(
                            colors = ButtonDefaults.buttonColors(primaryCol),
                            onClick = {
                            }
                        ) {
                            Text("Sign Up", color = Color.White, fontSize = 20.sp)
                        }

                    }
                }

            }

            Spacer(modifier = Modifier.height(15.dp))

            // Typical bottom prompt to toggle between Login & Signup Ui
            if(isLogin) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an account? ", color = Color.Black)
                    Text(
                        " Sign Up",
                        color = primaryCol,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable(onClick = { isLogin = false })
                    )
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Already have an account? ", color = Color.Black)
                    Text(
                        " Login",
                        color = primaryCol,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable(onClick = { isLogin = true })
                    )
                }
            }
        }

    }
}

// Reusable function for Email TextField
@Composable
fun EmailField(value: String, onValueChange: (String) -> Unit, primaryColor: Color, tertiaryColor: Color) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = "Email") },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = tertiaryColor,
            focusedBorderColor = primaryColor
        )
    )
}

// Reusable function for Password TextField
@Composable
fun PassField(value: String, onValueChange: (String) -> Unit, primaryColor: Color, tertiaryColor: Color) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = "Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = tertiaryColor,
            focusedBorderColor = primaryColor
        )
    )
}


// Preview
@Preview
@Composable
fun AuthScreenPreview() {
    AuthScreen()
}