package com.huaihao.bookcrosser.viewmodel

import com.huaihao.bookcrosser.ui.common.BaseViewModel



data class ProfileUiState(
    val name: String = "Unknown"
)

sealed interface ProfileEvent {

}

class ProfileViewModel: BaseViewModel<ProfileUiState, ProfileEvent>() {
    override fun onEvent(event: ProfileEvent) {

    }

    override fun defaultState(): ProfileUiState = ProfileUiState()
}