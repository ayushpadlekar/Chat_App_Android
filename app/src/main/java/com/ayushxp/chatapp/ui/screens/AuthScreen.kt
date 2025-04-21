package com.ayushxp.chatapp.ui.screens

import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayushxp.chatapp.ui.theme.ChatAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayushxp.chatapp.ui.components.CircularLoading
import com.ayushxp.chatapp.ui.components.EmailField
import com.ayushxp.chatapp.ui.components.PassField
import com.ayushxp.chatapp.utils.Validators.isValidEmail
import com.ayushxp.chatapp.utils.Validators.isValidPassword
import com.ayushxp.chatapp.utils.Validators.isValidUsername
import com.ayushxp.chatapp.viewmodel.AuthViewModel
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavHostController, viewModel: AuthViewModel = viewModel()) { // gets existing ViewModel without recomposition

    // Colors from MaterialTheme
    var primaryCol = MaterialTheme.colorScheme.primary
    var secondaryCol = MaterialTheme.colorScheme.secondary
    var tertiaryCol = MaterialTheme.colorScheme.tertiary

    // Toggle to switch between Login and SignUp ui
    var isLogin by remember { mutableStateOf(true) }

    // States for login input fields
    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    // States for signup input fields
    var signupName by remember { mutableStateOf("") }
    var signupEmail by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }

    // Error states for login input fields
    var loginEmailError by remember { mutableStateOf<String?>(null) }
    var loginPasswordError by remember { mutableStateOf<String?>(null) }

    // Error states for signup input fields
    var signupNameError by remember { mutableStateOf<String?>(null) }
    var signupEmailError by remember { mutableStateOf<String?>(null) }
    var signupPasswordError by remember { mutableStateOf<String?>(null) }


    // AuthViewModel's states - loading, success, error
    val authLoading = viewModel.authLoading.collectAsState().value // manually get value
    val authSuccess = viewModel.authSuccess.collectAsState().value // manually get value
    val authError by viewModel.authError.collectAsState() // using delegation 'by' to get value

    // State to track which button was clicked 'login' or 'signup' - for showing Toast
    var lastAction by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Scaffold for top-bar & content layout
    Scaffold(
        containerColor = Color.White,
        topBar = { // Simple Top Bar for App name
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

                        // Reusable Email TextField - 5 params
                        EmailField(value = loginEmail, onValueChange = { loginEmail = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol, error = loginEmailError)

                        // Reusable Password TextField - 5 params
                        PassField(value = loginPassword, onValueChange = { loginPassword = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol, error = loginPasswordError)

                        // Submit Button - Login
                        Button(
                            colors = ButtonDefaults.buttonColors(primaryCol),
                            enabled = !authLoading, // disable button while loading to prevent double clicks
                            onClick = {
                                // Clear previous errors
                                loginEmailError = null
                                loginPasswordError = null

                                // Validate inputs
                                if (!isValidEmail(loginEmail)) {
                                    loginEmailError = "Enter a valid email"
                                } else if (!isValidPassword(loginPassword)) {
                                    loginPasswordError = "Password must be at least 6 characters"
                                } else {
                                    viewModel.login(loginEmail, loginPassword) // call viewModel's login function
                                    lastAction = "login"
                                }
//                                if(loginEmail.isNotBlank() && loginPassword.isNotBlank()) {
//                                    viewModel.login(loginEmail, loginPassword)
//                                    lastAction = "login"
//                                }
                            }
                        ) {
                            if (authLoading) {
                                CircularLoading(55.dp)
                            } else {
                                Text("Login", color = Color.White, fontSize = 20.sp)
                            }
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
                            isError = signupNameError != null,
                            supportingText = { signupNameError?.let { Text(it) } },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = tertiaryCol,
                                focusedBorderColor = primaryCol
                            )
                        )

                        // Reusable Email TextField
                        EmailField(value = signupEmail, onValueChange = { signupEmail = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol, error = signupEmailError)

                        // Reusable Password TextField
                        PassField(value = signupPassword, onValueChange = { signupPassword = it }, primaryColor = primaryCol, tertiaryColor = tertiaryCol, error = signupPasswordError)

                        // Submit Button - Sign Up
                        Button(
                            colors = ButtonDefaults.buttonColors(primaryCol),
                            enabled = !authLoading, // disable button while loading to prevent double clicks
                            onClick = {
                                // Clear previous errors
                                signupNameError = null
                                signupEmailError = null
                                signupPasswordError = null

                                // Validate inputs
                                if (!isValidUsername(signupName.trim())) {
                                    signupNameError = "Username must be lowercase, between 3-15 letters, and start with letter."
                                } else if (!isValidEmail(signupEmail)) {
                                    signupEmailError = "Enter a valid email"
                                } else if (!isValidPassword(signupPassword)) {
                                    signupPasswordError = "Password must be at least 6 characters"
                                } else { // call viewModel's signup function
                                    viewModel.signup(signupEmail, signupPassword, signupName, Timestamp.now())
                                    lastAction = "signup"
                                }
                            }
                        ) {
                            if (authLoading) {
                                CircularLoading(65.dp)
                            } else {
                                Text("Sign Up", color = Color.White, fontSize = 20.sp)
                            }
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


            // Show toasts on authSuccess
            if (authSuccess) {
                if (lastAction == "login") { // login successful toast
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                } else if (lastAction == "signup") { // signup successful toast
                    Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
                }

                // Navigate to ChatList screen on AuthSuccess
                navController.navigate("chatlist") {
                    popUpTo("auth") { inclusive = true }
                }

                // Reset to prevent repeated toasts
                lastAction = null
                viewModel.resetAuthState()
            }


            // Show toasts on authError
            if (authError != null) {
                if (lastAction == "login") { // login unsuccessful toast
                    Toast.makeText(context, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
                } else if (lastAction == "signup") {
                    if(authError == "Email already exists") // email already exists toast
                        Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show()
                    else // signup unsuccessful toast
                        Toast.makeText(context, "Signup Unsuccessful", Toast.LENGTH_SHORT).show()
                }
                // Reset to prevent repeated toasts
                lastAction = null
                viewModel.resetAuthState()
            }
        }

    }
}



// Preview
@Preview
@Composable
fun AuthScreenPreview() {
    ChatAppTheme(darkTheme = false, dynamicColor = false) {
        AuthScreen(navController = rememberNavController())
    }
}