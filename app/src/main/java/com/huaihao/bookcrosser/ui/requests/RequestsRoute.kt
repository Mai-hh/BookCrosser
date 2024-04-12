package com.huaihao.bookcrosser.ui.requests

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.huaihao.bookcrosser.viewmodel.RequestsViewModel

@Composable
fun RequestsRoute() {

    val viewModel: RequestsViewModel = viewModel()

    RequestsScreen(viewModel.state) {

    }

}
