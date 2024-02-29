package com.huaihao.bookcrosser.ui.common

sealed interface UiEvent {
    data class Error(val message: String): UiEvent
    data class Toast(val message: String): UiEvent
}