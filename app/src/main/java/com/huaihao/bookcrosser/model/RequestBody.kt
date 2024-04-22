package com.huaihao.bookcrosser.model

sealed interface RequestBody {
    data class Book(
        val title: String,
        val author: String,
        val isbn: String,
        val coverUrl: String,
        val description: String,
        val latitude: Double,
        val longitude: Double
    ) : RequestBody

    data class User(
        val username: String,
        val email: String,
        val password: String,
        val bio: String,
        val latitude: Double,
        val longitude: Double
    ) : RequestBody
}