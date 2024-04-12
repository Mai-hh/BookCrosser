package com.huaihao.bookcrosser.ui.search

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huaihao.bookcrosser.viewmodel.SearchViewModel

@Composable
fun SearchRoute() {
    val viewModel: SearchViewModel = viewModel()

    SearchScreen(uiState = viewModel.state) { event ->
        viewModel.onEvent(event)
    }
}