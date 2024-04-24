package com.huaihao.bookcrosser.model

data class Comment(
    val id: Long,
    val bookId: Long,
    val userId: Long,
    val content: String,
    val createdAt: String,
    val updatedAt: String
)

data class CommentDTO(
    val comment: Comment,
    val sender: User,
    val book: Book
)
