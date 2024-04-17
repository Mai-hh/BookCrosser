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
        if (!password.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d).+$"))) {
            return "密码需要包含字母和数字"
        }
        return null
    }


}