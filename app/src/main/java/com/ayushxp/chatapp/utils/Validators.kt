package com.ayushxp.chatapp.utils

import android.util.Patterns

object Validators {

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidUsername(username: String): Boolean {
        return username.length in 3..15 && username.all {
            //username should be in lowercase, should be a letter or digit & first character should always be a letter.
            it.isLowerCase() && it.isLetterOrDigit() && it != ' '
        } && username[0].isLetter()
    }
}
