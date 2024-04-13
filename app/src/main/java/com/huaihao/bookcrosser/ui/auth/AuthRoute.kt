package com.huaihao.bookcrosser.ui.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huaihao.bookcrosser.ui.auth.Destinations.FORGET_PASSWORD_ROUTE
import com.huaihao.bookcrosser.ui.auth.Destinations.LOGIN_ROUTE
import com.huaihao.bookcrosser.ui.auth.Destinations.SIGNUP_ROUTE
import com.huaihao.bookcrosser.ui.auth.Destinations.VERIFICATION_ROUTE
import com.huaihao.bookcrosser.ui.common.BaseScreenWrapper
import com.huaihao.bookcrosser.viewmodel.auth.LoginViewModel
import org.koin.androidx.compose.koinViewModel

val items = listOf(LOGIN_ROUTE, SIGNUP_ROUTE, VERIFICATION_ROUTE, FORGET_PASSWORD_ROUTE)

object Destinations {
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val VERIFICATION_ROUTE = "verification"
    const val FORGET_PASSWORD_ROUTE = "forget_password"
}
@Composable
fun AuthRoute() {
    AuthNavHost()
}

@Composable
fun AuthNavHost(navController: NavHostController = rememberNavController()) {
    Scaffold { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = LOGIN_ROUTE,
        ) {
            composable(LOGIN_ROUTE) {
                val viewModel = koinViewModel<LoginViewModel>()
                BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                    LoginScreen(
                        uiState = viewModel.state,
                        onEvent = viewModel::onEvent
                    )
                }
            }

            composable(SIGNUP_ROUTE) {
            }

            composable(VERIFICATION_ROUTE) {

            }

            composable(FORGET_PASSWORD_ROUTE) {

            }
        }
    }
}