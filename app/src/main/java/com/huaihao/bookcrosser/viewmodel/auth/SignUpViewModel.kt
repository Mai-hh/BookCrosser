package com.huaihao.bookcrosser.viewmodel.auth

import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel

sealed interface SignUpEvent {
    data class UsernameChange(val username: String) : SignUpEvent
    data class EmailChange(val email: String) : SignUpEvent
    data class PasswordChange(val password: String) : SignUpEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : SignUpEvent
    data class Register(
        val username: String,
        val email: String,
        val password: String,
        val confirmPassword: String
    ) : SignUpEvent
}

data class SignUpUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false
)

class SignUpViewModel(private val authRepo: AuthRepo) : BaseViewModel<SignUpUiState, SignUpEvent>() {
    override fun onEvent(event: SignUpEvent) = when (event) {
        is SignUpEvent.UsernameChange -> onUsernameChange(username = event.username)
        is SignUpEvent.EmailChange -> onEmailChange(email = event.email)
        is SignUpEvent.PasswordChange -> onPasswordChange(password = event.password)
        is SignUpEvent.ConfirmPasswordChange -> onConfirmPasswordChange(confirmPassword = event.confirmPassword)
        is SignUpEvent.Register -> onRegister(
            username = event.username,
            email = event.email,
            password = event.password,
            confirmPassword = event.confirmPassword
        )
    }

    override fun defaultState(): SignUpUiState = SignUpUiState()

    private fun onUsernameChange(username: String) {
        state = state.copy(
            username = username
        )
    }

    private fun onEmailChange(email: String) {
        state = state.copy(
            email = email
        )
    }

    private fun onPasswordChange(password: String) {
        state = state.copy(
            password = password
        )
    }

    private fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(
            confirmPassword = confirmPassword
        )
    }

    private fun onRegister(username: String, email: String, password: String, confirmPassword: String) {
        state = state.copy(
            isLoading = true
        )
    }
}