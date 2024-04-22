package com.huaihao.bookcrosser.model


data class User (
    var username: String? = null,
    var email: String? = null,
    var password: String,
    var avatar: String = "",
    var bio: String = "",
    var location: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)

data class UserProfile(
    var username: String? = null,
    var email: String? = null,
    var avatar: String? = null,
    var bio: String? = null,
    var booksUploaded: List<Book>? = null,
    var booksBorrowed: List<Book>? = null,
    var booksInRequesting: List<Book>? = null
)