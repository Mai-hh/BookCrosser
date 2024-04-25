package com.huaihao.bookcrosser.ui.common

sealed interface UiEvent {
    data class Error(val message: String): UiEvent
    data class SnackbarToast(val message: String): UiEvent

    data class SystemToast(val message: String): UiEvent
    data class Navigate(val route: String): UiEvent
    data class PopUpToStartDestination(val inclusive: Boolean): UiEvent
    data object NavBack: UiEvent
    data object Finish: UiEvent
    data object ClearFocus: UiEvent

    data object HideSoftwareKeyboard: UiEvent
}