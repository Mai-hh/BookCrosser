package com.huaihao.bookcrosser.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.huaihao.bookcrosser.ui.Destinations.AUTH_ROUTE
import com.huaihao.bookcrosser.ui.Destinations.MAIN_SCREEN_ROUTE
import com.huaihao.bookcrosser.ui.auth.AuthRoute
import com.huaihao.bookcrosser.ui.auth.ForgetPasswordScreen
import com.huaihao.bookcrosser.ui.auth.LoginScreen
import com.huaihao.bookcrosser.ui.auth.SignUpScreen
import com.huaihao.bookcrosser.ui.common.BaseScreenWrapper
import com.huaihao.bookcrosser.ui.main.MainScreenRoute
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.auth.ForgetPasswordViewModel
import com.huaihao.bookcrosser.viewmodel.auth.LoginViewModel
import com.huaihao.bookcrosser.viewmodel.auth.SignUpViewModel
import org.koin.androidx.compose.koinViewModel

object Destinations {
    const val AUTH_ROUTE = "auth"
    const val MAIN_SCREEN_ROUTE = "main_screen"
}


@Composable
fun BookCrosserNavHost(
    navController: NavHostController = rememberNavController()
) {
    Scaffold { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = AUTH_ROUTE,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            navigation(startDestination = AuthRoute.Destinations.LOGIN_ROUTE, route = AUTH_ROUTE) {
                composable(AuthRoute.Destinations.LOGIN_ROUTE) {
                    val viewModel = koinViewModel<LoginViewModel>()
                    BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                        LoginScreen(
                            uiState = viewModel.state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }

                composable(AuthRoute.Destinations.SIGNUP_ROUTE) {
                    val viewModel = koinViewModel<SignUpViewModel>()
                    BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                        SignUpScreen(
                            uiState = viewModel.state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }

                composable(AuthRoute.Destinations.FORGET_PASSWORD_ROUTE) {
                    val viewModel = koinViewModel<ForgetPasswordViewModel>()
                    BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                        ForgetPasswordScreen(
                            uiState = viewModel.state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }
            composable(MAIN_SCREEN_ROUTE) {
                MainScreenRoute()
            }
        }
    }
}

@Preview
@Composable
private fun BookCrosserNavHostPreview() {
    BookCrosserTheme {
        BookCrosserNavHost()
    }
}