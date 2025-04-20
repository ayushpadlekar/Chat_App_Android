package com.ayushxp.chatapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// Reusable function for Email TextField
@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    primaryColor: Color,
    tertiaryColor: Color,
    error: String? = null
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email") },
        singleLine = true,
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = tertiaryColor,
            focusedBorderColor = primaryColor
        )
    )
}