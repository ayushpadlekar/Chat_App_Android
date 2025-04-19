package com.ayushxp.chatapp.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    // Private instance of Firebase Auth
    private val fireAuth = FirebaseAuth.getInstance()

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
    fun signupUser(email: String, pass: String, result: (Boolean, String?)->Unit) {

        fireAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result(true, null)
                } else {
                    result(false, it.exception?.message)
                }
            }
    }

    // Logout user
    fun logoutUser() {
        fireAuth.signOut()
    }
}