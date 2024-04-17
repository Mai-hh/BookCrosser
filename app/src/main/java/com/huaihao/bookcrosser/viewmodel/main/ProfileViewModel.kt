package com.huaihao.bookcrosser.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.ProfileNotification
import com.huaihao.bookcrosser.model.ProfileNotificationType
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import kotlinx.coroutines.launch


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
        }
    }

    private fun onLogout() {

        viewModelScope.launch {
            authRepo.logout("").collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {

                    }

                    is ApiResult.Error -> {

                    }

                    is ApiResult.Loading -> {

                    }
                }

            }
        }
    }

    override fun defaultState(): ProfileUiState = ProfileUiState()
}