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
    fun validateISBN(isbn: String): String? {
        val cleanIsbn = isbn.replace(" ", "")
        return when {
            cleanIsbn.length == 10 -> if (isValidISBN10(cleanIsbn)) null else "无效的 ISBN-10"
            cleanIsbn.length == 13 -> if (isValidISBN13(cleanIsbn)) null else "无效的 ISBN-13"
            else -> "ISBN 长度必须为 10 或 13 位"
        }
    }

    private fun isValidISBN10(isbn10: String): Boolean {
        if (!isbn10.matches(Regex("\\d{9}[\\dXx]"))) return false
        var sum = 0
        for (i in 0..8) {
            sum += (isbn10[i] - '0') * (10 - i)
        }
        val checkDigit = isbn10[9]
        sum += if (checkDigit == 'X' || checkDigit == 'x') 10 else checkDigit - '0'
        return sum % 11 == 0
    }

    private fun isValidISBN13(isbn13: String): Boolean {
        if (!isbn13.matches(Regex("\\d{13}"))) return false
        var sum = 0
        for (i in isbn13.indices) {
            val digit = isbn13[i] - '0'
            sum += if (i % 2 == 0) digit else digit * 3
        }
        return sum % 10 == 0
    }

}