package com.huaihao.bookcrosser.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun <State, ScreenEvent> BaseScreenWrapper(
    navController: NavController,
    viewModel: BaseViewModel<State, ScreenEvent>,
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }

                is UiEvent.Toast -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }

        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState)}
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}