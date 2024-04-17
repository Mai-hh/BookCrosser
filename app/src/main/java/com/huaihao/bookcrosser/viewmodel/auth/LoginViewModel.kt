package com.huaihao.bookcrosser.viewmodel.auth

import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.Destinations.MAIN_SCREEN_ROUTE
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import kotlinx.coroutines.launch

sealed interface LoginEvent {

    data class NavTo(val destination: String) : LoginEvent
    data class UsernameChange(val username: String) : LoginEvent
    data class EmailChange(val email: String) : LoginEvent
    data class PasswordChange(val password: String) : LoginEvent
    data class LoginByEmail(val email: String, val password: String) : LoginEvent
    data class LoginByUsername(val username: String, val password: String) : LoginEvent
}

enum class LoginType {
    EMAIL,
    USERNAME
}

data class LoginUiState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val loginType: LoginType = LoginType.EMAIL
)

class LoginViewModel(private val authRepo: AuthRepo) :
    BaseViewModel<LoginUiState, LoginEvent>() {
    override fun onEvent(event: LoginEvent) = when (event) {
        is LoginEvent.UsernameChange -> onUsernameChange(username = event.username)
        is LoginEvent.PasswordChange -> onPasswordChange(password = event.password)
        is LoginEvent.LoginByUsername -> onLoginByUsername(
            username = event.username,
            password = event.password
        )

        is LoginEvent.EmailChange -> onEmailChange(email = event.email)
        is LoginEvent.LoginByEmail -> onLoginByEmail(email = event.email, password = event.password)
        is LoginEvent.NavTo -> sendEvent(UiEvent.Navigate(event.destination))
    }

    override fun defaultState(): LoginUiState = LoginUiState()
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

    private fun onLoginByUsername(username: String, password: String) {
        state = state.copy(
            isLoading = true
        )
        viewModelScope.launch {
            authRepo.loginByUsername(username, password).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                    }

                    is ApiResult.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.errorMessage
                        )
                    }

                    is ApiResult.Loading -> {
                        state = state.copy(
                            isLoading = true
                        )
                    }
                }

            }
        }
    }

    private fun onLoginByEmail(email: String, password: String) {
        state = state.copy(
            isLoading = true
        )
        viewModelScope.launch {
            authRepo.loginByEmail(email, password).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                        sendEvent(UiEvent.Navigate(MAIN_SCREEN_ROUTE))
                    }

                    is ApiResult.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.errorMessage
                        )
                    }

                    is ApiResult.Loading -> {
                        state = state.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
}