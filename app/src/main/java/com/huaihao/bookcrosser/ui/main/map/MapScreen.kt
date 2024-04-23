package com.huaihao.bookcrosser.ui.main.map

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.huaihao.bookcrosser.model.BookMarker
import com.huaihao.bookcrosser.util.hasLocationPermission
import com.huaihao.bookcrosser.viewmodel.main.MapDisplayType
import com.huaihao.bookcrosser.viewmodel.main.MapEvent
import com.huaihao.bookcrosser.viewmodel.main.MapUiState
import com.huaihao.bookcrosser.viewmodel.main.MapViewState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(uiState: MapUiState, onEvent: (MapEvent) -> Unit, initialPosition: LatLng? = null, title: String? = null, snippet: String? = null) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val cameraState = rememberCameraPositionState()

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(!context.hasLocationPermission()) {
        permissionState.launchMultiplePermissionRequest()
    }


    when {
        permissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                onEvent(MapEvent.PermissionGranted)
            }
            LaunchedEffect(Unit) {
                onEvent(MapEvent.LoadBookMarkers)
                onEvent(MapEvent.LoadUserMarkers)
                onEvent(MapEvent.UpdateCurrentMarkerInfo(title, snippet))
            }
        }

        permissionState.shouldShowRationale -> {
            RationaleAlert(onDismiss = { }) {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
            LaunchedEffect(Unit) {
                onEvent(MapEvent.PermissionRevoked)
            }
        }
    }

    OutlinedCard(
        modifier = Modifier.padding(16.dp),
        shape = ShapeDefaults.ExtraLarge,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            when (uiState.viewState) {
                is MapViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                MapViewState.RevokedPermissions -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("使用地图需要定位权限")
                        Button(
                            onClick = {
                                context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS))
                            },
                            enabled = !context.hasLocationPermission()
                        ) {
                            if (context.hasLocationPermission()) CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = Color.White
                            )
                            else Text("设置")
                        }
                    }
                }

                is MapViewState.Success -> {
                    val location = remember { (uiState.viewState as MapViewState.Success).location }

                    val currentLoc = initialPosition
                        ?: LatLng(
                            location?.latitude ?: 0.0,
                            location?.longitude ?: 0.0
                        )

                    val currentMarkerState: MarkerState = rememberMarkerState(position = currentLoc)

                    LaunchedEffect(key1 = currentLoc) {
                        cameraState.centerOnLocation(currentLoc)
                        currentMarkerState.showInfoWindow()
                    }

                    ConstraintLayout {
                        val (map, fabs) = createRefs()
                        MapContentScreen(
                            currentMarkerState = currentMarkerState,
                            cameraState = cameraState,
                            bookMarkers = uiState.bookMarkers,
                            uiState = uiState,
                            modifier = Modifier.constrainAs(map) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                        )

                        Row(
                            modifier = Modifier.constrainAs(fabs) {
                                start.linkTo(parent.start, margin = 16.dp)
                                bottom.linkTo(parent.bottom, margin = 16.dp)
                            }
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    coroutineScope.launch {
                                        cameraState.centerOnLocation(currentLoc)
                                    }
                                }
                            ) {
                                Icon(Icons.Rounded.MyLocation, contentDescription = "我的位置")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            FloatingActionButton(
                                onClick = {
                                    onEvent(MapEvent.ShowBookMarkers)
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.MenuBook,
                                    contentDescription = "展示图书"
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            FloatingActionButton(
                                onClick = {
                                    onEvent(MapEvent.ShowUserMarkers)
                                }
                            ) {
                                Icon(Icons.Rounded.Groups, contentDescription = "展示用户")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "使用地图需要定位权限",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun MapContentScreen(
    modifier: Modifier = Modifier,
    currentMarkerState: MarkerState,
    cameraState: CameraPositionState,
    bookMarkers: List<BookMarker> = emptyList(),
    uiState: MapUiState
) {

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false
        )
    }


    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraState,
        uiSettings = mapUiSettings,
        properties = MapProperties(
            mapType = MapType.HYBRID,
        )
    ) {
        Marker(
            state = currentMarkerState,
            title = uiState.currentMarkerInfo.title,
            snippet = uiState.currentMarkerInfo.snippet,
            zIndex = 20.0f,
            draggable = false
        )

        if (uiState.mapDisplayType == MapDisplayType.Users) {
            uiState.userMarkers.forEach { user ->
                user.latitude?.also { latitude ->
                    user.longitude?.also { longitude ->
                        Marker(
                            state = MarkerState(position = LatLng(latitude, longitude)),
                            title = user.username,
                            snippet = user.bio,
                            alpha = 0.8f,
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                            draggable = false
                        )
                    }
                }
            }
        } else {
            bookMarkers.forEach { marker ->
                Marker(
                    state = MarkerState(position = marker.position),
                    title = marker.book.title,
                    snippet = marker.book.author,
                    alpha = 0.8f,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                    draggable = false,
                )
            }
        }
    }
}

private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        15f
    )
)

@Preview(showBackground = true)
@Composable
fun RationalDialogPreview() {
    RationaleAlert(onDismiss = { }, onConfirm = { })
}
