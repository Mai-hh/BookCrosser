package com.huaihao.bookcrosser.util

object AuthUtil {
    fun validateUsername(username: String): String? {
        if (username.length < 3) {
            return "Username must be at least 3 characters"
        }
        if (username.length > 20) {
            return "Username must not exceed 20 characters"
        }
        if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            return "Username can only contain letters, numbers, and underscores"
        }
        return null
    }

    fun validateEmail(email: String): String? {
        if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
            return "Invalid email format"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.length < 8) {
            return "Password must be at least 8 characters"
        }
        if (!password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$"))) {
            return "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
        }
        return null
    }
}