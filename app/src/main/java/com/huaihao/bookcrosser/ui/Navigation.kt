package com.huaihao.bookcrosser.ui

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
import com.huaihao.bookcrosser.ui.auth.LoginScreen
import com.huaihao.bookcrosser.ui.common.BaseScreenWrapper
import com.huaihao.bookcrosser.ui.main.MainScreenRoute
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.auth.LoginViewModel
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
        ) {
            navigation(startDestination = "login", route = AUTH_ROUTE) {
                composable("login") {
                    val viewModel = koinViewModel<LoginViewModel>()
                    BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                        LoginScreen(
                            uiState = viewModel.state,
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }

            navigation(startDestination = "search", route = MAIN_SCREEN_ROUTE) {
                composable("search") {
                    MainScreenRoute()
                }
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