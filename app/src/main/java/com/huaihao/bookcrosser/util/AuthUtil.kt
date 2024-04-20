package com.huaihao.bookcrosser.util

object AuthUtil {
    fun validateUsername(username: String): String? {
        if (username.length < 3) {
            return "用户名至少需要3个字符"
        }
        if (username.length > 20) {
            return "用户名不能超过20个字符"
        }
        if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            return "用户名只能包含字母、数字和下划线"
        }
        return null
    }

    fun validateEmail(email: String): String? {
        if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
            return "请输入有效的邮箱地址"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.length < 8) {
            return "密码至少需要8个字符"
        }
        if (!password.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d).+$"))) {
            return "密码需要包含字母和数字"
        }
        return null
    }
}