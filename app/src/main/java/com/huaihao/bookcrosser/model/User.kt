package com.huaihao.bookcrosser.model


data class User(
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var avatar: String? = "",
    var bio: String? = "",
    var location: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)

data class UserProfile(
    var username: String = "Book Crosser",
    var email: String? = null,
    var avatar: String? = null,
    var bio: String? = "简介空空如也~",
    var latitude: Double? = null,
    var longitude: Double? = null,
    var booksUploaded: List<Book>? = emptyList(),
    var booksBorrowed: List<Book>? = emptyList(),
    var booksInRequesting: List<Book>? = emptyList(),
)