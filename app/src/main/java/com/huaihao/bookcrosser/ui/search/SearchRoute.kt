package com.huaihao.bookcrosser.ui.search

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SearchRoute() {
    val viewModel: SearchViewModel = viewModel()

    SearchScreen(viewModel)
}