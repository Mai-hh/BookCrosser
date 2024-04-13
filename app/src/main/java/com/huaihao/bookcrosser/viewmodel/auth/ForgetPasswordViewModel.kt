package com.huaihao.bookcrosser.viewmodel.auth

import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel

sealed interface ForgotPasswordEvent {
    data class EmailChange(val email: String) : ForgotPasswordEvent
    data class ForgotPassword(val email: String) : ForgotPasswordEvent
}

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isResetCodeSent: Boolean = false
)

class ForgetPasswordViewModel(private val authRepo: AuthRepo) : BaseViewModel<ForgotPasswordUiState, ForgotPasswordEvent>() {
    override fun onEvent(event: ForgotPasswordEvent) = when (event) {
        is ForgotPasswordEvent.EmailChange -> onEmailChange(email = event.email)
        is ForgotPasswordEvent.ForgotPassword -> onForgotPassword(email = event.email)
    }

    override fun defaultState(): ForgotPasswordUiState = ForgotPasswordUiState()

    private fun onEmailChange(email: String) {
        state = state.copy(
            email = email
        )
    }

    private fun onForgotPassword(email: String) {
        state = state.copy(
            isLoading = true
        )
    }
}