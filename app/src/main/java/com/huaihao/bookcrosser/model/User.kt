package com.huaihao.bookcrosser.model

import java.time.LocalDateTime


data class User (
    var username: String? = null,
    var email: String? = null,
    var password: String,
    var avatar: String = "",
    var bio: String = "",
    var location: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)