package com.huaihao.bookcrosser.viewmodel.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.BookMarker
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.repo.BookRepo
import com.huaihao.bookcrosser.service.ILocationService
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class MarkerInfo(
    var title: String = "当前位置",
    var snippet: String = ""
)

data class MapUiState(
    var viewState: MapViewState = MapViewState.Loading,
    var currentPosition: LatLng? = null,
    var mapDisplayType: MapDisplayType = MapDisplayType.Books,
    val bookMarkers: List<BookMarker> = mutableListOf(),
    val userMarkers: List<User> = mutableListOf(),
    var currentMarkerInfo: MarkerInfo = MarkerInfo()
)

sealed interface MapDisplayType {
    data object Books : MapDisplayType
    data object Users : MapDisplayType
}

sealed interface MapViewState {
    data object Loading : MapViewState
    data class Success(val location: LatLng?) : MapViewState
    data object RevokedPermissions : MapViewState
}

sealed interface MapEvent {
    data object PermissionGranted : MapEvent
    data object PermissionRevoked : MapEvent
    data object LoadBookMarkers : MapEvent
    data object ShowBookMarkers : MapEvent
    data object ShowUserMarkers : MapEvent
    data object LoadUserMarkers : MapEvent

    data class UpdateCurrentMarkerInfo(val title: String?, val snippet: String?) : MapEvent

}

class MapViewModel(
    private val locationService: ILocationService,
    private val bookRepo: BookRepo,
    private val authRepo: AuthRepo
) :
    BaseViewModel<MapUiState, MapEvent>() {

    companion object {
        private const val TAG = "MapViewModel"
    }

    override fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.PermissionGranted -> {
                viewModelScope.launch(Dispatchers.IO) {
                    locationService.requestLocationUpdates().collect { location ->
                        state = state.copy(
                            viewState = MapViewState.Success(location)
                        )
                    }
                }
            }

            is MapEvent.PermissionRevoked -> {
                state = state.copy(
                    viewState = MapViewState.RevokedPermissions
                )
            }

            is MapEvent.LoadBookMarkers -> {
                loadBookMarkers()
            }

            MapEvent.LoadUserMarkers -> {
                loadUserMarkers()
            }

            MapEvent.ShowBookMarkers -> {
                showBookMarkers()
            }

            MapEvent.ShowUserMarkers -> {
                showUserMarkers()
            }

            is MapEvent.UpdateCurrentMarkerInfo -> {
                state = state.copy(
                    currentMarkerInfo = MarkerInfo(
                        title = event.title ?: "当前位置",
                        snippet = event.snippet ?: ""
                    )
                )
            }
        }
    }

    private fun loadUserMarkers() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepo.loadAllUsers().collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        val users = (result.data as List<User>)
                        state = state.copy(
                            userMarkers = users
                        )
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("加载用户失败"))
                        showBookMarkers()
                    }

                    is ApiResult.Loading -> {

                    }
                }
            }
        }
    }

    private fun showBookMarkers() {
        state = state.copy(
            mapDisplayType = MapDisplayType.Books
        )
    }

    private fun showUserMarkers() {
        state = state.copy(
            mapDisplayType = MapDisplayType.Users
        )
    }

    private fun loadBookMarkers() {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.loadBooks().collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        val books = (result.data as List<Book>)
                        Log.d(TAG, "onEvent: $books")
                        state = state.copy(
                            bookMarkers = books.map {
                                BookMarker(
                                    book = it,
                                    position = LatLng(it.latitude, it.longitude)
                                )
                            }
                        )
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("加载书籍失败"))
                    }

                    is ApiResult.Loading -> {

                    }
                }
            }
        }
    }

    override fun defaultState() = MapUiState()
}