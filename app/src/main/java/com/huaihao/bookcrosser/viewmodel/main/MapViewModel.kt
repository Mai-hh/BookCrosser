package com.huaihao.bookcrosser.viewmodel.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.BookMarker
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.BookRepo
import com.huaihao.bookcrosser.service.ILocationService
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class MapUiState(
    var viewState: MapViewState = MapViewState.Loading,
    var currentPosition: LatLng? = null,
    val bookMarkers: List<BookMarker> = mutableListOf()
)

sealed interface MapViewState {
    data object Loading : MapViewState
    data class Success(val location: LatLng?) : MapViewState
    data object RevokedPermissions : MapViewState
}

sealed interface MapEvent {
    data object PermissionGranted : MapEvent
    data object PermissionRevoked : MapEvent
    data object LoadBookMarkers : MapEvent

}

class MapViewModel(private val locationService: ILocationService, private val bookRepo: BookRepo) :
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
                                sendEvent(UiEvent.Toast("加载书籍失败"))
                            }

                            is ApiResult.Loading -> {

                            }
                        }
                    }
                }
            }
        }
    }

    override fun defaultState() = MapUiState()
}