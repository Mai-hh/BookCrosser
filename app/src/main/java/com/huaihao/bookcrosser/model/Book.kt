package com.huaihao.bookcrosser.model

data class Book(
    val id: Long,
    val ownerId: Long,
    val uploaderId: Long,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val title: String,
    val author: String,
    val isbn: String,
    val coverUrl: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)


