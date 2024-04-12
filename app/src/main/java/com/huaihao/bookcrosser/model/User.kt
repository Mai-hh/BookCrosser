package com.huaihao.bookcrosser.model

import java.time.LocalDateTime


data class User (
    var username: String,
    var email: String,
    var password: String,
    var avatar: String? = null,
    var bio: String? = null,
    var location: String? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)