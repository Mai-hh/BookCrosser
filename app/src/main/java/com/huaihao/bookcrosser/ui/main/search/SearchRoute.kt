package com.huaihao.bookcrosser.ui.main.search

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huaihao.bookcrosser.viewmodel.main.SearchViewModel

@Composable
fun SearchRoute() {
    val viewModel: SearchViewModel = viewModel()

    SearchScreen(uiState = viewModel.state) { event ->
        viewModel.onEvent(event)
    }
}