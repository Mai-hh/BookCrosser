package com.huaihao.bookcrosser.model

data class DriftingRequest(
    val bookName: String,
    val author: String,
    val isbn: String? = null,
    val requirements: String? = null
)
