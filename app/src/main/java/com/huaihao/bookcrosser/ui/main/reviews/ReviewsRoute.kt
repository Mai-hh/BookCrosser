package com.huaihao.bookcrosser.ui.main.reviews

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huaihao.bookcrosser.viewmodel.main.ReviewsViewModel

@Composable
fun ReviewsRoute() {
    val viewModel: ReviewsViewModel = viewModel()

    ReviewsScreen(uiState = viewModel.state) { event ->
        viewModel.onEvent(event)
    }
}