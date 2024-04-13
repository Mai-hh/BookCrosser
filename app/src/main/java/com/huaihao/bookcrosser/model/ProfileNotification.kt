package com.huaihao.bookcrosser.model


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Rocket
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ProfileNotificationType(val iconImageVector: ImageVector) {
    data object BookRequest : ProfileNotificationType(Icons.Rounded.Rocket)
    data object BookReturn : ProfileNotificationType(Icons.Rounded.Email)
}

data class ProfileNotification(
    val type: ProfileNotificationType,
    val title: String,
    val message: String,
    val time: String
)
