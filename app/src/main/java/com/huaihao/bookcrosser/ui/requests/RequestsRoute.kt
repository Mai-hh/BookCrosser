package com.huaihao.bookcrosser.ui.requests

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RequestsRoute() {

    val viewModel: RequestsViewModel = viewModel()

    RequestsScreen(viewModel.state) {

    }

}
