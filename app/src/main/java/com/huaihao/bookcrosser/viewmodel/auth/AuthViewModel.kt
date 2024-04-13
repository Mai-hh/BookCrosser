package com.huaihao.bookcrosser.viewmodel.auth

import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import kotlinx.coroutines.launch





sealed interface AuthEvent {

    data class UsernameChange(val username: String) : AuthEvent
    data class EmailChange(val email: String) : AuthEvent
    data class PasswordChange(val password: String) : AuthEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : AuthEvent
    data class Login(val usernameOrEmail: String, val password: String) : AuthEvent
    data class Register(val username: String, val email: String, val password: String, val confirmPassword: String) :
        AuthEvent
    data class ForgotPassword(val email: String) : AuthEvent
    data class ResetPassword(val email: String, val code: String, val newPassword: String) :
        AuthEvent
}

data class ResetPasswordUiState(
    val email: String = "",
    val code: String = "",
    val newPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPasswordReset: Boolean = false
)

data class AuthUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val isResetCodeSent: Boolean = false,
    val isPasswordReset: Boolean = false
)

class AuthViewModel(private val authRepository: AuthRepo) : BaseViewModel<AuthUiState, AuthEvent>() {
    override fun onEvent(event: AuthEvent) = when (event) {
        is AuthEvent.UsernameChange -> onUsernameChange(username = event.username)
        is AuthEvent.EmailChange -> onEmailChange(email = event.email)
        is AuthEvent.PasswordChange -> onPasswordChange(password = event.password)
        is AuthEvent.ConfirmPasswordChange -> onConfirmPasswordChange(confirmPassword = event.confirmPassword)
        is AuthEvent.Login -> onLogin(usernameOrEmail = event.usernameOrEmail, password = event.password)
        is AuthEvent.Register -> onRegister(username = event.username, email = event.email, password = event.password, confirmPassword = event.confirmPassword)
        is AuthEvent.ForgotPassword -> onForgotPassword(email = event.email)
        is AuthEvent.ResetPassword -> onResetPassword(email = event.email, code = event.code, newPassword = event.newPassword)
    }

    override fun defaultState(): AuthUiState = AuthUiState()

    private fun onUsernameChange(username: String) {
        state = state.copy(username = username)
    }

    private fun onEmailChange(email: String) {
        state = state.copy(email = email)
    }

    private fun onPasswordChange(password: String) {
        state = state.copy(password = password)
    }

    private fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword)
    }

    private fun onLogin(usernameOrEmail: String, password: String) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = authRepository.login(usernameOrEmail, password)
            state = when (result) {
                is ApiResult.Success -> state.copy(isLoading = false, isLoggedIn = true)
                is ApiResult.Error -> state.copy(isLoading = false, error = result.error)
            }
        }
    }

    private fun onRegister(username: String, email: String, password: String, confirmPassword: String) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            if (password != confirmPassword) {
                state = state.copy(isLoading = false, error = "密码不匹配")
                return@launch
            }
            val result = authRepository.register(username, email, password)
            state = when (result) {
                is ApiResult.Success -> state.copy(isLoading = false, isRegistered = true)
                is ApiResult.Error -> state.copy(isLoading = false, error = result.error)
            }
        }
    }

    private fun onForgotPassword(email: String) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = authRepository.sendResetCode(email)
            state = when (result) {
                is ApiResult.Success -> state.copy(isLoading = false, isResetCodeSent = true)
                is ApiResult.Error -> state.copy(isLoading = false, error = result.error)
            }
        }
    }

    private fun onResetPassword(email: String, code: String, newPassword: String) {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            val result = authRepository.resetPassword(email, code, newPassword)
            state = when (result) {
                is ApiResult.Success -> state.copy(isLoading = false, isPasswordReset = true)
                is ApiResult.Error -> state.copy(isLoading = false, error = result.error)
            }
        }
    }
}