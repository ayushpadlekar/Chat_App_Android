package com.ayushxp.chatapp.data.model

import com.google.firebase.Timestamp

data class Message(
    val sender: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)