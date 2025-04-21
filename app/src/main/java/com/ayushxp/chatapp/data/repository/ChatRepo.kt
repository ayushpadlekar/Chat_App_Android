package com.ayushxp.chatapp.data.repository

import com.ayushxp.chatapp.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository {

    // Firestore reference to "users" collection
    private val firestoreCollection = FirebaseFirestore.getInstance().collection("users")

    // Search User Function
    fun searchUsers(query: String, onResult: (List<User>) -> Unit) {
        firestoreCollection.get()
            .addOnSuccessListener { snapshot ->
                val allUsers = snapshot.documents.mapNotNull { docFields ->
                    docFields.toObject(User::class.java)
                }

                // Filter by username or email contains query
                val filtered = allUsers.filter {
                    it.username.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true)
                }

                onResult(filtered)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }


    // Firestore reference to "chats" collection
    val chatsRef = FirebaseFirestore.getInstance().collection("chats")

    // Finds a chat between currentUserId and otherUserId.
    // If found, returns existing chatId, otherwise creates a new chat.
    fun getOrCreateChat(
        currentUid: String,
        otherUid: String,
        onResult: (chatId: String?) -> Unit
    ) {
        val chatKey = listOf(currentUid, otherUid).sorted().joinToString("_")

        // Find if chat already exists
        chatsRef.whereEqualTo("chatKey", chatKey).get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val existingChat = result.documents[0]
                    onResult(existingChat.id)
                } else {
                    val newChat = hashMapOf(
                        "users" to listOf(currentUid, otherUid),
                        "chatKey" to chatKey,
                        "lastMessage" to "",
                        "timestamp" to Timestamp.now()
                    )

                    chatsRef.add(newChat)
                        .addOnSuccessListener { newDoc ->
                            onResult(newDoc.id)
                        }
                        .addOnFailureListener {
                            onResult(null)
                        }
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

}
