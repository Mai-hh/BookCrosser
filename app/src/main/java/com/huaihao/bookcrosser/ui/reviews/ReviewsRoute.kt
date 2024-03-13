package com.huaihao.bookcrosser.ui.reviews

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ReviewsRoute() {
    val viewModel: ReviewsViewModel = viewModel()

    ReviewsScreen(uiState = viewModel.state) { event ->
        viewModel.onEvent(event)
    }
}