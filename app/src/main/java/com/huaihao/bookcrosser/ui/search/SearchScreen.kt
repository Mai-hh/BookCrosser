package com.huaihao.bookcrosser.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.huaihao.bookcrosser.util.supportWideScreen
import com.melody.map.gd_compose.GDMap
import com.melody.map.gd_compose.poperties.MapProperties
import com.melody.map.gd_compose.poperties.MapUiSettings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(uiState: SearchUiState, onEvent: (event: SearchEvent) -> Unit) {
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            initialValue = SheetValue.Expanded
        )
    )
    Surface(modifier = Modifier.supportWideScreen()) {
        BottomSheetScaffold(
            sheetPeekHeight = 200.dp,
            scaffoldState = sheetState,
            sheetContent = {
                SearchBar(
                    query = uiState.searchingText,
                    onQueryChange = { text ->
                        onEvent(SearchEvent.SearchingTextChange(text))
                    },
                    onSearch = { query ->
                        onEvent(SearchEvent.Search(query))
                    },
                    active = uiState.isSearching,
                    onActiveChange = {
                        onEvent(SearchEvent.ToggleSearch(it))
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {

                }
                var state by remember { mutableIntStateOf(0) }
                val titles = listOf("Tab 1", "Tab 2", "Tab 3 with lots of text")
                Column {
                    TabRow(
                        selectedTabIndex = state,
                        indicator = { tabPositions ->
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[state])
                                    .height(4.dp)
                                    .padding(horizontal = 28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color = MaterialTheme.colorScheme.primary)
                            )
                        }
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = {
                                    Text(
                                        text = title,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "Text tab ${state + 1} selected",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Box(Modifier.fillMaxSize())
            }
        ) { paddingValue ->
            ConstraintLayout(modifier = Modifier.padding(paddingValue)) {

            }
//            MapView()
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
        SearchScreen(
            onEvent = {},
            uiState = SearchUiState()
        )
    }
}