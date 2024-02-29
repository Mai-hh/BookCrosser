package com.huaihao.bookcrosser

sealed interface UiEvent {
    data class Error(val message: String): UiEvent
    data class Toast(val message: String): UiEvent
}