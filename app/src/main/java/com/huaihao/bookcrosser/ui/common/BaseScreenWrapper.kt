package com.huaihao.bookcrosser.ui.common

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
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

                UiEvent.NavBack -> navController.navigateUp()
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

@Composable
fun LimitedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    maxLength: Int = 20,
    label: String = "",
    modifier: Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val fieldValue = remember { mutableStateOf(TextFieldValue(value)) }

    OutlinedTextField(
        value = fieldValue.value,
        onValueChange = {
            if (it.text.length <= maxLength) {
                fieldValue.value = it
                onValueChange(it.text)
            }
        },
        visualTransformation = visualTransformation,
        maxLines = maxLines,
        label = { Text(label) },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}