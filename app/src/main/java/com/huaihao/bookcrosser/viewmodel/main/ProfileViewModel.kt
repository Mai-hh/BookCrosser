package com.huaihao.bookcrosser.viewmodel.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.UserProfile
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.repo.BookRepo
import com.huaihao.bookcrosser.service.ILocationService
import com.huaihao.bookcrosser.ui.Destinations.AUTH_ROUTE
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.ui.main.Destinations
import com.huaihao.bookcrosser.util.MMKVUtil
import com.huaihao.bookcrosser.util.USER_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class ProfileUiState(
    var userProfile: UserProfile = UserProfile(),
    var isSaving: Boolean = false,
    var isUpdatingBook: Boolean = false,
    var showUpdateBookDialog: Boolean = false,
    var showDriftingFinishDialog: Boolean = false
)

sealed interface ProfileEvent {
    data object Logout : ProfileEvent
    data object LoadUserProfile : ProfileEvent
    data class LocatedBook(val book: Book) : ProfileEvent
    data object NavToSettings : ProfileEvent

    data object ResetState : ProfileEvent
    data object NavBack : ProfileEvent
    data class SendToast(val message: String) : ProfileEvent
    data object GetCurrentLocation : ProfileEvent
    data class UpdateProfile(
        val username: String,
        val bio: String?,
        val latitude: Double?,
        val longitude: Double?
    ) : ProfileEvent

    data class DriftingFinish(val book: Book) : ProfileEvent

    data class UpdateBook(
        val bookId: Long,
        val title: String,
        val author: String,
        val description: String
    ) : ProfileEvent

    data object DismissUpdateBookDialog : ProfileEvent
    data object DismissFinishDriftingDialog : ProfileEvent

    data object ShowUpdateBookDialog : ProfileEvent
    data object ShowFinishDriftingDialog : ProfileEvent
}

class ProfileViewModel(
    private val authRepo: AuthRepo,
    private val bookRepo: BookRepo,
    private val locationService: ILocationService
) : BaseViewModel<ProfileUiState, ProfileEvent>() {

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

            is ProfileEvent.LocatedBook -> {
                sendEvent(UiEvent.Navigate("${Destinations.MAP_ROUTE}/${event.book.latitude}/${event.book.longitude}/${event.book.title}/${event.book.author}"))
            }

            ProfileEvent.NavToSettings -> {
                sendEvent(UiEvent.Navigate(Destinations.PROFILE_SETTINGS_ROUTE))
            }

            ProfileEvent.NavBack -> {
                sendEvent(UiEvent.NavBack)
            }

            is ProfileEvent.SendToast -> {
                sendEvent(UiEvent.SnackbarToast(event.message))
            }

            ProfileEvent.GetCurrentLocation -> {
                getCurrentLocation()
            }

            is ProfileEvent.UpdateProfile -> {
                onUpdateProfile(event.username, event.bio, event.latitude, event.longitude)
            }

            is ProfileEvent.DriftingFinish -> {
                onDriftingFinish(event.book)
            }

            is ProfileEvent.UpdateBook -> {
                updateBook(event.bookId, event.title, event.author, event.description)
            }

            ProfileEvent.DismissUpdateBookDialog -> {
                state = state.copy(showUpdateBookDialog = false)
            }

            ProfileEvent.ShowUpdateBookDialog -> {
                state = state.copy(showUpdateBookDialog = true)
            }

            ProfileEvent.DismissFinishDriftingDialog -> {
                state = state.copy(showDriftingFinishDialog = false)
            }
            ProfileEvent.ShowFinishDriftingDialog -> {
                state = state.copy(showDriftingFinishDialog = true)
            }

            ProfileEvent.ResetState -> state = state.copy(isSaving = false)
        }
    }

    private fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            locationService.requestCurrentLocation().collect { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    state = state.copy(
                        userProfile = state.userProfile.copy(
                            latitude = currentLocation.latitude,
                            longitude = currentLocation.longitude
                        )
                    )
                    Log.d(TAG, "onEvent: $currentLocation")
                    sendEvent(UiEvent.SnackbarToast("获取位置成功"))
                } else {
                    sendEvent(UiEvent.SnackbarToast("位置获取异常，请确认是否开启定位权限"))
                }
            }
        }
    }

    private fun updateBook(bookId: Long, title: String, author: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.updateBook(bookId, title, author, description).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        Log.d(TAG, "updateBook: ${result.data}")
                        state = state.copy(isUpdatingBook = false, showUpdateBookDialog = false)
                        sendEvent(UiEvent.SnackbarToast("更新成功"))
                        onLoadUserProfile()
                    }

                    is ApiResult.Error -> {
                        Log.e(TAG, "updateBook: ${result.errorMessage}")
                        state = state.copy(isUpdatingBook = false)
                        sendEvent(UiEvent.SnackbarToast("更新失败"))
                    }

                    is ApiResult.Loading -> {
                        state = state.copy(isUpdatingBook = true)
                    }
                }
            }
        }
    }

    private fun onDriftingFinish(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.driftingFinish(book.id).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            showDriftingFinishDialog = false
                        )
                        sendEvent(UiEvent.SnackbarToast("收漂请求已发出"))
                        onLoadUserProfile()
                    }

                    is ApiResult.Error -> {
                        Log.e(TAG, "onDriftingFinish: ${result.errorMessage}")
                        state = state.copy(
                            showDriftingFinishDialog = false
                        )
                        sendEvent(UiEvent.SnackbarToast("收漂请求发送失败"))
                        onLoadUserProfile()
                    }

                    is ApiResult.Loading -> {}
                }
            }
        }
    }

    private fun onLogout() {
        MMKVUtil.clear(USER_TOKEN)
        sendEvent(UiEvent.Finish)
    }

    private fun onUpdateProfile(
        username: String,
        bio: String?,
        latitude: Double?,
        longitude: Double?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepo.updateProfile(username, bio, latitude, longitude).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            isSaving = false
                        )
                        sendEvent(UiEvent.SnackbarToast("个人信息更新成功"))
                        sendEvent(UiEvent.NavBack)
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("个人信息更新失败"))
                        state = state.copy(isSaving = false)
                    }

                    is ApiResult.Loading -> {
                        state = state.copy(isSaving = true)
                    }
                }
            }
        }
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
                        sendEvent(UiEvent.SnackbarToast("个人信息加载失败"))
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