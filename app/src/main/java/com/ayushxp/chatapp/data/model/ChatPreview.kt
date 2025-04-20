package com.ayushxp.chatapp.data.model

import com.google.firebase.Timestamp

data class ChatPreview(
    val chatId: String = "",
    val otherUserId: String = "",
    val otherUsername: String = "",
    val lastMessage: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
