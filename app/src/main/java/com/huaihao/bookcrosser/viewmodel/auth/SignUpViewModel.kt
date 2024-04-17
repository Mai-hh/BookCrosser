package com.huaihao.bookcrosser.viewmodel.auth

import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.AuthRepo
import com.huaihao.bookcrosser.ui.Destinations.MAIN_SCREEN_ROUTE
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.util.AuthUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    data object NavBack : SignUpEvent
}

data class SignUpUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

class SignUpViewModel(private val authRepo: AuthRepo) :
    BaseViewModel<SignUpUiState, SignUpEvent>() {
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

        SignUpEvent.NavBack -> sendEvent(UiEvent.NavBack)
    }

    override fun defaultState(): SignUpUiState = SignUpUiState()

    private fun onUsernameChange(username: String) {
        state = state.copy(
            username = username,
            usernameError = validateUsername(username)
        )
    }

    private fun onEmailChange(email: String) {
        state = state.copy(
            email = email,
            emailError = validateEmail(email)
        )
    }

    private fun onPasswordChange(password: String) {
        state = state.copy(
            password = password,
            passwordError = validatePassword(password),
        )
    }

    private fun onConfirmPasswordChange(confirmPassword: String) {
        state = state.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = validateConfirmPassword(confirmPassword)
        )
    }

    private fun onRegister(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        val usernameError = validateUsername(username)
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)
        val confirmPasswordError = validateConfirmPassword(confirmPassword)

        if (usernameError != null || emailError != null || passwordError != null || confirmPasswordError != null) {
            state = state.copy(
                usernameError = usernameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        state = state.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            authRepo.register(
                username = username,
                email = email,
                password = password
            ).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(isLoading = false, isRegistered = true)
                        sendEvent(UiEvent.Navigate(MAIN_SCREEN_ROUTE))
                    }

                    is ApiResult.Error -> {
                        state = state.copy(isLoading = false, error = result.errorMessage)
                    }

                    is ApiResult.Loading -> {}
                }
            }
        }

    }

    private fun validateUsername(username: String): String? =
        AuthUtil.validateUsername(username)


    private fun validateEmail(email: String): String? = AuthUtil.validateEmail(email)

    private fun validatePassword(password: String): String? = AuthUtil.validatePassword(password)

    private fun validateConfirmPassword(confirmPassword: String): String? {
        if (confirmPassword != state.password) {
            return "Passwords do not match"
        }
        return null
    }

}