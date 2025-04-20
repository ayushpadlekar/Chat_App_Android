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

        // Find if chat already exists with same 2 user UIDs
        chatsRef.whereArrayContains("users", currentUid).get()
            .addOnSuccessListener { chat ->
                val existingChat = chat.documents.find { doc ->
                    val users = doc.get("users") as? List<String>
                    users?.contains(otherUid) == true
                }

                if (existingChat != null) {
                    onResult(existingChat.id) // Chat already exists
                } else {
                    // Create new chat
                    val newChatMap = hashMapOf(
                        "users" to listOf(currentUid, otherUid),
                        "lastMessage" to "",
                        "timestamp" to Timestamp.now()
                    )

                    chatsRef.add(newChatMap)
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
