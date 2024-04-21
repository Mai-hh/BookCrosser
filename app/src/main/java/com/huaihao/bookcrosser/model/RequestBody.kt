package com.huaihao.bookcrosser.model

sealed interface RequestBody {
    data class Book(
        val title: String,
        val author: String,
        val isbn: String,
        val coverUrl: String,
        val description: String
    ) : RequestBody
}