package com.huaihao.bookcrosser.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.huaihao.bookcrosser.util.supportWideScreen
import com.melody.map.gd_compose.GDMap
import com.melody.map.gd_compose.poperties.MapProperties
import com.melody.map.gd_compose.poperties.MapUiSettings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val sheetState = rememberBottomSheetScaffoldState()
    Surface(modifier = Modifier.supportWideScreen()) {
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetContent = {
                SearchBar(
                    query = ,
                    onQueryChange = ,
                    onSearch = ,
                    active = ,
                    onActiveChange = 
                ) {
                    
                }
                Box(Modifier.fillMaxSize())
            }
        ) { padding ->
            MapView()
        }
    }
}

@Composable
fun MapView() {
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }

    val mapProperties by remember {
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



@Preview
@Composable
fun SearchScreenPreview() {
    MaterialTheme {
        SearchScreen()
    }
}