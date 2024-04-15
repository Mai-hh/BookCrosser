package com.huaihao.bookcrosser.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.ui.auth.AuthRoute.Destinations.FORGET_PASSWORD_ROUTE
import com.huaihao.bookcrosser.ui.auth.AuthRoute.Destinations.SIGNUP_ROUTE
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.auth.LoginEvent
import com.huaihao.bookcrosser.viewmodel.auth.LoginType
import com.huaihao.bookcrosser.viewmodel.auth.LoginUiState

@Composable
fun LoginScreen(uiState: LoginUiState, onEvent: (event: LoginEvent) -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val (title, content, action) = createRefs()

        Text(
            text = stringResource(id = R.string.app_name_space),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(content.top)
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(content) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {

            LimitedOutlinedTextField(
                label = "邮箱",
                value = uiState.email,
                onValueChange = { email ->
                    onEvent(LoginEvent.EmailChange(email))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LimitedOutlinedTextField(
                label = "密码",
                visualTransformation = PasswordVisualTransformation(),
                value = uiState.password,
                onValueChange = { password ->
                    onEvent(LoginEvent.PasswordChange(password))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                onEvent(
                    when (uiState.loginType) {
                        LoginType.EMAIL -> LoginEvent.LoginByEmail(uiState.email, uiState.password)
                        LoginType.USERNAME -> LoginEvent.LoginByUsername(uiState.username, uiState.password)
                    }
                )
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.login))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.forget_password),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable {
                    onEvent(LoginEvent.NavTo(FORGET_PASSWORD_ROUTE))
                }
            )
        }

        OutlinedButton(onClick = {
            onEvent(LoginEvent.NavTo(SIGNUP_ROUTE))
        }, modifier = Modifier
            .fillMaxWidth()
            .constrainAs(action) {
                bottom.linkTo(parent.bottom, margin = 24.dp)
            }) {
            Text(
                text = stringResource(id = R.string.register),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BookCrosserTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onEvent = { }
        )
    }
}