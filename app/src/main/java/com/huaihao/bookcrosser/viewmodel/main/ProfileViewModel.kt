package com.huaihao.bookcrosser.viewmodel.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.ProfileNotification
import com.huaihao.bookcrosser.model.ProfileNotificationType
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.model.UserProfile
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.Destinations.AUTH_ROUTE
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.util.MMKVUtil
import com.huaihao.bookcrosser.util.USER_TOKEN
import kotlinx.coroutines.Dispatchers
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
    ),
    var userProfile: UserProfile? = null
)

sealed interface ProfileEvent {
    data object Logout : ProfileEvent
    data object LoadUserProfile : ProfileEvent
}

class ProfileViewModel(private val authRepo: AuthRepo) :
    BaseViewModel<ProfileUiState, ProfileEvent>() {

        companion object {
            const val TAG = "ProfileViewModel"
        }
    override fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Logout -> {
                onLogout()
            }

            is ProfileEvent.LoadUserProfile -> {
                onLoadUserProfile()
            }
        }
    }

    private fun onLogout() {
        MMKVUtil.clear(USER_TOKEN)
        sendEvent(UiEvent.Finish)
    }

    private fun onLoadUserProfile() {

        viewModelScope.launch(Dispatchers.IO) {
            authRepo.loadUserProfile().collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            userProfile = (result.data as UserProfile)
                        )
                        Log.d(TAG, "onLoadUserProfile Success: ${result.data}")
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.Toast("个人信息加载失败"))
                        MMKVUtil.clear(USER_TOKEN)
                        sendEvent(UiEvent.Navigate(AUTH_ROUTE))
                    }

                    is ApiResult.Loading -> {}
                }
            }
        }

    }

    override fun defaultState(): ProfileUiState = ProfileUiState()
}