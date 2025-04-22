package com.ayushxp.chatapp.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    // Private instances of Firebase Auth & Firebase Firestore
    private val fireAuth = FirebaseAuth.getInstance()
    private val fireStore = FirebaseFirestore.getInstance()


    // Check if user is logged in (not null)
    fun isLoggedin(): Boolean {
        return fireAuth.currentUser != null
    }

    // Login user with email & password & return result- true/false
    fun loginUser(email: String, pass: String, result: (Boolean, String?)->Unit) {

        fireAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result(true, null)
                } else {
                    result(false, it.exception?.message)
                }
            }
    }

    // Create new user with email & password & return result- true/false
    fun registerUser(email: String, pass: String, username: String, timestamp: Timestamp, result: (Boolean, String?)->Unit) {

        // Check if username already exists or not in the whole firestore db
        fireStore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) { // Username is unique, proceed to create user

                    fireAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {
                            if(it.isSuccessful) {
                                val uid = fireAuth.currentUser?.uid ?: return@addOnCompleteListener

                                // Add user details to Firestore
                                fireStore.collection("users").document(uid)
                                    .set(
                                        hashMapOf(
                                            "uid" to uid,
                                            "email" to email,
                                            "username" to username,
                                            "timestamp" to timestamp,
                                        )
                                    ).addOnSuccessListener {
                                        result(true, null)
                                    }.addOnFailureListener {
                                        result(false, it.message)
                                    }

                            } else {
                                val errMsg = when(it.exception) {
                                    is FirebaseAuthUserCollisionException -> "Email already exists"
                                    else -> it.exception?.message
                                }
                                result(false, errMsg)
                            }
                        }
                } else {
                    result(false, "Username already exists")
                }
            }.addOnFailureListener {
                result(false, it.message)
            }

    }

    // Logout user
    fun logoutUser() {
        fireAuth.signOut()
    }
}