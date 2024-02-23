package com.huaihao.bookcrosser.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.huaihao.bookcrosser.util.supportWideScreen
import com.melody.map.gd_compose.GDMap
import com.melody.map.gd_compose.poperties.MapProperties
import com.melody.map.gd_compose.poperties.MapUiSettings


@Composable
fun SearchScreen() {
    Surface(modifier = Modifier.supportWideScreen()) {
        MapView()
    }
}

@Composable
fun MapView() {
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }

    var mapProperties by remember {
        mutableStateOf(
            MapProperties()
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 地图
        GDMap(
            modifier = Modifier.matchParentSize(),
            uiSettings = uiSettings,
            properties = mapProperties
        )
    }
}