package com.huaihao.bookcrosser.viewmodel.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.network.TokenResponse
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.Destinations.MAIN_SCREEN_ROUTE
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.util.AuthUtil
import com.huaihao.bookcrosser.util.MMKVUtil
import com.huaihao.bookcrosser.util.USER_TOKEN
import kotlinx.coroutines.Dispatchers
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
    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoggedIn: Boolean = false,
    val loginType: LoginType = LoginType.EMAIL
)

class LoginViewModel(private val authRepo: AuthRepo) :
    BaseViewModel<LoginUiState, LoginEvent>() {

    init {
        checkLogin()
    }

    private fun checkLogin() {
        val token = MMKVUtil.getString(USER_TOKEN)
        if (token.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                authRepo.checkLogin(token).collect { result ->
                    when (result) {
                        is ApiResult.Success<*> -> {
                            state = state.copy(
                                isLoggedIn = true
                            )
                            sendEvent(UiEvent.Navigate(MAIN_SCREEN_ROUTE))
                        }

                        is ApiResult.Error -> {
                            MMKVUtil.clear(USER_TOKEN)
                        }

                        is ApiResult.Loading -> {}
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "LoginViewModel"
    }

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
            email = email,
            emailError = AuthUtil.validateEmail(email)
        )
    }

    private fun onPasswordChange(password: String) {
        state = state.copy(
            password = password,
            passwordError = AuthUtil.validatePassword(password)
        )
    }

    private fun onLoginByUsername(username: String, password: String) {
        sendEvent(UiEvent.ClearFocus)
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
        sendEvent(UiEvent.ClearFocus)
        viewModelScope.launch {
            authRepo.loginByEmail(email, password).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            isLoading = false,
                            isLoggedIn = true
                        )
                        Log.d(TAG, "MMKV Token: " + MMKVUtil.getString(USER_TOKEN))
                        Log.d(TAG, "Response Message: " + (result.data as TokenResponse).token)
                        MMKVUtil.put(USER_TOKEN, (result.data as TokenResponse).token)
                        sendEvent(UiEvent.SystemToast("登录成功"))
                        sendEvent(UiEvent.Navigate(MAIN_SCREEN_ROUTE))
                    }

                    is ApiResult.Error -> {
                        state = state.copy(
                            isLoading = false,
                            error = result.errorMessage
                        )
                        sendEvent(UiEvent.SnackbarToast("登录失败\n原因: ${result.errorMessage}"))
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