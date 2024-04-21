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
    var coverUrl: String? = null,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)

data class BookSearchItem(
    val title: String,
    val author: String,
    val description: String,
    val status: String,
    val coverUrl: String?
)

fun Book.toSearchItem() = BookSearchItem(
    title = title,
    author = author,
    description = description,
    status = status,
    coverUrl = coverUrl
)

