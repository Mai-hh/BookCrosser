package com.huaihao.bookcrosser.model


data class Book(
    val id: Long,
    val ownerId: Long,
    val ownerUsername: String? = null,
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

data class BookProfileItem(
    val title: String,
    val author: String,
    val description: String,
    val status: String,
    var coverUrl: String?,
    val updatedAt: String,
    var ownerUsername: String? = null,
    var ownerId: Long,
    val uploaderId: Long,
)

data class BookSearchItem(
    val title: String,
    val author: String,
    val description: String,
    val status: String,
    val coverUrl: String?
)

data class BookRequest(
    val book: Book,
    val requesterId: Long,
    val requesterName: String,
    val requesterEmail: String,
    val requesterLatitude: Double,
    val requesterLongitude: Double,
)

data class BookRequestItem(
    val title: String,
    val author: String,
    val description: String,
    val coverUrl: String?
)

fun Book.toSearchItem() = BookSearchItem(
    title = title,
    author = author,
    description = description,
    status = status,
    coverUrl = coverUrl
)

fun Book.toProfileItem() = BookProfileItem(
    title = title,
    author = author,
    description = description,
    status = status,
    updatedAt = updatedAt,
    coverUrl = coverUrl,
    ownerId = ownerId,
    uploaderId = uploaderId,
    ownerUsername = ownerUsername
)


enum class BookStatus(val statusString: String) {
    AVAILABLE("available"),
    BORROWED("borrowed"),
    REQUESTED("requested"),
    UNAVAILABLE("unavailable")
}


