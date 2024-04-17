package com.huaihao.bookcrosser.viewmodel.main

import com.huaihao.bookcrosser.model.ProfileNotification
import com.huaihao.bookcrosser.model.ProfileNotificationType
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.Destinations.AUTH_ROUTE
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.util.MMKVUtil
import com.huaihao.bookcrosser.util.USER_TOKEN


data class ProfileUiState(
    val user: User = User(
        username = "John Doe",
        email = "xxx@gmail.com",
        password = "123456",
        bio = "I am a developer",
    ),
    val notifications: List<ProfileNotification> = listOf(
        ProfileNotification(
            type = ProfileNotificationType.BookRequest,
            title = "Book Request",
            message = "Someone requested a book from you",
            time = "2 hours ago"
        ),
        ProfileNotification(
            type = ProfileNotificationType.BookReturn,
            title = "Book Return",
            message = "Someone returned a book to you",
            time = "1 day ago"
        ),
        ProfileNotification(
            type = ProfileNotificationType.BookRequest,
            title = "Book Request",
            message = "Someone requested a book from you",
            time = "2 hours ago"
        ),
        ProfileNotification(
            type = ProfileNotificationType.BookReturn,
            title = "Book Return",
            message = "Someone returned a book to you",
            time = "1 day ago"
        ),
        ProfileNotification(
            type = ProfileNotificationType.BookRequest,
            title = "Book Request",
            message = "Someone requested a book from you",
            time = "2 hours ago"
        ),
        ProfileNotification(
            type = ProfileNotificationType.BookReturn,
            title = "Book Return",
            message = "Someone returned a book to you",
            time = "1 day ago"
        ),
        ProfileNotification(
            type = ProfileNotificationType.BookRequest,
            title = "Book Request",
            message = "Someone requested a book from you",
            time = "2 hours ago"
        ),
        ProfileNotification(
            type = ProfileNotificationType.BookReturn,
            title = "Book Return",
            message = "Someone returned a book to you",
            time = "1 day ago"
        ),
    )
)

sealed interface ProfileEvent {
    data object Logout : ProfileEvent
}

class ProfileViewModel(private val authRepo: AuthRepo) :
    BaseViewModel<ProfileUiState, ProfileEvent>() {
    override fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.Logout -> {
                onLogout()
            }

            else -> {}
        }
    }

    private fun onLogout() {
        MMKVUtil.clear(USER_TOKEN)
        sendEvent(UiEvent.Finish)
    }

    override fun defaultState(): ProfileUiState = ProfileUiState()
}