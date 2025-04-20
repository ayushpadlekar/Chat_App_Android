package com.ayushxp.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import com.ayushxp.chatapp.data.model.ChatPreview
import com.ayushxp.chatapp.data.model.Message
import com.ayushxp.chatapp.data.model.User
import com.ayushxp.chatapp.data.repository.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {

    // ChatRepository Instance
    private val chatRepo = ChatRepository()

    // Private MutableStateFlow to hold search results
    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults // then store in normal StateFlow.

    // Function to pass query to the repository to search users.
    fun searchUser(query: String) {
        chatRepo.searchUsers(query) { result ->
            _searchResults.value = result // update the MutableStateFlow with the result
        }
    }

    // Function to get or create a chat between two users- when clicked upon user
    fun getOrCreateChat(otherUserId: String, onResult: (chatId: String?) -> Unit) {

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid == null) {
            onResult(null)  // not signed in
            return
        }
        chatRepo.getOrCreateChat(currentUserUid, otherUserId, onResult)
    }



    // Firestore instance
    private val firestoreRef = FirebaseFirestore.getInstance()

    // Private MutableStateFlow to hold messages list.
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages // then store in normal StateFlow.

    // Function to load messages from chatId
    fun loadMessages(chatId: String) {
        firestoreRef.collection("messages")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    _messages.value = emptyList()
                    return@addSnapshotListener
                }
                val msgs = snapshot.documents.mapNotNull { document ->
                    document.toObject(Message::class.java)
                }
                _messages.value = msgs
            }
    }

    // Function to send a message
    fun sendMessage(chatId: String, text: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val msg = Message(
            sender = currentUserId,
            text = text,
            timestamp = Timestamp.now()
        )

        firestoreRef.collection("messages")
            .document(chatId)
            .collection("messages")
            .add(msg)
    }



    private val _chatList = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chatList: StateFlow<List<ChatPreview>> = _chatList

    fun loadChatsForCurrentUser() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestoreRef.collection("chats")
            .whereArrayContains("users", currentUserId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    _chatList.value = emptyList()
                    return@addSnapshotListener
                }

                val chatSummaries = mutableListOf<ChatPreview>()

                for (doc in snapshot.documents) {
                    val chatId = doc.id
                    val users = doc.get("users") as? List<String> ?: continue
                    val lastMessage = doc.getString("lastMessage") ?: ""
                    val timestamp = doc.getTimestamp("timestamp") ?: Timestamp.now()

                    // Get the other user
                    val otherUserId = users.firstOrNull { it != currentUserId } ?: continue

                    // Fetch other user's name
                    firestoreRef.collection("users").document(otherUserId).get()
                        .addOnSuccessListener { userDoc ->
                            val username = userDoc.getString("username") ?: "User"
                            val chatSummary = ChatPreview(
                                chatId = chatId,
                                otherUserId = otherUserId,
                                otherUsername = username,
                                lastMessage = lastMessage,
                                timestamp = timestamp
                            )
                            chatSummaries.add(chatSummary)
                            _chatList.value = chatSummaries.sortedByDescending { it.timestamp }
                        }
                }
            }
    }
}