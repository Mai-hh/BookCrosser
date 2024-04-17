package com.huaihao.bookcrosser.ui.common

sealed interface UiEvent {
    data class Error(val message: String): UiEvent
    data class Toast(val message: String): UiEvent
    data class Navigate(val route: String): UiEvent
    data object NavBack: UiEvent
    data object Finish: UiEvent
}