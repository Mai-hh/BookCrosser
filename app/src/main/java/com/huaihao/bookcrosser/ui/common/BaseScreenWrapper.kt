package com.huaihao.bookcrosser.ui.common

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun <State, ScreenEvent> BaseScreenWrapper(
    navController: NavController,
    viewModel: BaseViewModel<State, ScreenEvent>,
    content: @Composable () -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val backPressedTime = remember { mutableLongStateOf(0L) }

    val onBackPressed = {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime.longValue > 2000) {
            backPressedTime.longValue = currentTime
            Toast.makeText(context, "再按一次退出应用", Toast.LENGTH_SHORT).show()
        } else {
            (context as? Activity)?.finishAffinity()
        }
    }


    BackHandler {
        onBackPressed()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }

                is UiEvent.SnackbarToast -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvent.SystemToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                UiEvent.NavBack -> navController.navigateUp()
                UiEvent.Finish -> {
                    lifecycleOwner.lifecycleScope.launch {
                        (context as? Activity)?.finishAffinity()
                    }
                }

                is UiEvent.PopUpToStartDestination -> {
                    navController.graph.startDestinationRoute?.let { navController.popBackStack(route = it, inclusive = event.inclusive) }
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

@Composable
fun LimitedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    maxLength: Int = 20,
    label: String = "",
    modifier: Modifier,
    singLine: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    enabled : Boolean = true,
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
        enabled = enabled,
        visualTransformation = visualTransformation,
        maxLines = maxLines,
        isError = isError,
        singleLine = singLine,
        supportingText = supportingText,
        label = { Text(label, color = Color.Unspecified.copy(alpha = 0.5f)) },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}