package com.huaihao.bookcrosser.ui.main.profile

import androidx.compose.runtime.Composable
import com.huaihao.bookcrosser.viewmodel.main.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileRoute() {
    val viewModel = koinViewModel<ProfileViewModel>()

    ProfileScreen(
        uiState = viewModel.state,
        onEvent = viewModel::onEvent
    )
}