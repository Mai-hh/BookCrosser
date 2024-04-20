package com.huaihao.bookcrosser.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.huaihao.bookcrosser.service.ILocationService
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


data class MapUiState(
    var viewState: MapViewState = MapViewState.Loading,
    var currentPosition: LatLng? = null,
    val bookMarkers: List<LatLng> = mutableListOf()
)

sealed interface MapViewState {
    data object Loading : MapViewState
    data class Success(val location: LatLng?) : MapViewState
    data object RevokedPermissions : MapViewState
}

sealed interface MapEvent {
    data object PermissionGranted : MapEvent

    data object PermissionRevoked : MapEvent

}

class MapViewModel(private val locationService: ILocationService) :
    BaseViewModel<MapUiState, MapEvent>() {

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
        }
    }

    override fun defaultState() = MapUiState()
}