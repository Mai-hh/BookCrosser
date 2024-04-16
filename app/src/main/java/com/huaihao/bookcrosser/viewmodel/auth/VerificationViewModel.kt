package com.huaihao.bookcrosser.viewmodel.auth

import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent

sealed interface VerificationEvent {
    data class CodeChange(val code: String) : VerificationEvent
    data class Verify(val code: String) : VerificationEvent
    data object ResendCode : VerificationEvent
    data object NavBack : VerificationEvent
}

data class VerificationUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isVerified: Boolean = false,
    val codeError: String? = null
)

class VerificationViewModel :  BaseViewModel<VerificationUiState, VerificationEvent>() {
    override fun onEvent(event: VerificationEvent) = when (event) {
        is VerificationEvent.CodeChange -> onCodeChange(code = event.code)
        is VerificationEvent.Verify -> onVerify(code = event.code)
        VerificationEvent.ResendCode -> onResendCode()
        VerificationEvent.NavBack -> sendEvent(UiEvent.NavBack)
    }

    override fun defaultState(): VerificationUiState = VerificationUiState()

    private fun onCodeChange(code: String) {

    }

    private fun onResendCode() {

    }

    private fun onVerify(code: String) {

    }
}