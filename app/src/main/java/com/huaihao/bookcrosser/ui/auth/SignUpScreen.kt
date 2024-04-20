package com.huaihao.bookcrosser.ui.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.viewmodel.auth.SignUpEvent
import com.huaihao.bookcrosser.viewmodel.auth.SignUpUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(uiState: SignUpUiState, onEvent: (event: SignUpEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SignUpEvent.NavBack) }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val (title, content) = createRefs()

                Box(contentAlignment = Alignment.BottomStart, modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(content.top, margin = 16.dp)
                    }) {
                    Text(
                        text = stringResource(id = R.string.input_profile),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.constrainAs(content) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }) {

                    LimitedOutlinedTextField(
                        label = "用户名",
                        value = uiState.username,
                        onValueChange = { username ->
                            onEvent(SignUpEvent.UsernameChange(username))
                        },
                        isError = uiState.usernameError != null,
                        supportingText = {
                            uiState.usernameError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LimitedOutlinedTextField(
                        label = "邮箱",
                        value = uiState.email,
                        onValueChange = {
                            onEvent(SignUpEvent.EmailChange(it))
                        },
                        isError = uiState.emailError != null,
                        supportingText = {
                            uiState.emailError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LimitedOutlinedTextField(
                        label = "密码",
                        visualTransformation = PasswordVisualTransformation(),
                        value = uiState.password,
                        onValueChange = {
                            onEvent(SignUpEvent.PasswordChange(it))
                        },
                        isError = uiState.passwordError != null,
                        supportingText = {
                            uiState.passwordError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LimitedOutlinedTextField(
                        label = "确认密码",
                        visualTransformation = PasswordVisualTransformation(),
                        value = uiState.password,
                        onValueChange = {
                            onEvent(SignUpEvent.PasswordChange(it))
                        },
                        isError = uiState.passwordError != null,
                        supportingText = {
                            uiState.passwordError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        onEvent(
                            SignUpEvent.Register(
                                uiState.username,
                                uiState.email,
                                uiState.password,
                                uiState.confirmPassword
                            )
                        )
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = R.string.register))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    MaterialTheme {
        SignUpScreen(
            uiState = SignUpUiState(),
            onEvent = { }
        )
    }
}
