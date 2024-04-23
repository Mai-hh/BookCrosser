package com.huaihao.bookcrosser.model

data class DriftingRequest(
    val id: Long,
    val requester: User,
    val book: Book
)
