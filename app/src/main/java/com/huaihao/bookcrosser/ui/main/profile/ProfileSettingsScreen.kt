package com.huaihao.bookcrosser.ui.main.profile

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.ProfileEvent
import com.huaihao.bookcrosser.viewmodel.main.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ProfileEvent.NavBack) }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            var newUsername by rememberSaveable { mutableStateOf(uiState.userProfile.username) }
            var newBio by rememberSaveable { mutableStateOf(uiState.userProfile.bio) }

            Text(
                text = "基本信息",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Column(Modifier.padding(vertical = 8.dp)) {
                LimitedOutlinedTextField(
                    label = "用户名",
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { }
                )
                LimitedOutlinedTextField(
                    label = "邮箱",
                    value = uiState.userProfile.email ?: "",
                    onValueChange = { },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    onEvent(ProfileEvent.SendToast("邮箱不可修改"))
                                }
                            )
                        }
                )
                LimitedOutlinedTextField(
                    label = "个性签名",
                    value = newBio ?: "",
                    onValueChange = { newBio = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "位置信息",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.width(16.dp))

                AssistChip(
                    onClick = {
                        onEvent(ProfileEvent.GetCurrentLocation)
                    },
                    label = {
                        Text(text = "获取当前位置")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "获取当前位置",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }

            Row(Modifier.padding(vertical = 8.dp)) {
                OutlinedTextField(
                    label = {
                        Text("经度")
                    },
                    value = uiState.userProfile.longitude?.toString() ?: "",
                    onValueChange = {},
                    isError = uiState.userProfile.longitude == null,
                    supportingText = {
                        if (uiState.userProfile.longitude == null) {
                            Text("请输入正确的经度", color = Color.Red)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(0.1f))
                OutlinedTextField(
                    label = {
                        Text("纬度")
                    },
                    value = uiState.userProfile.latitude?.toString() ?: "",
                    onValueChange = {},
                    isError = uiState.userProfile.latitude == null,
                    supportingText = {
                        if (uiState.userProfile.latitude == null) {
                            Text("请输入正确的纬度", color = Color.Red)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    onEvent(ProfileEvent.UpdateProfile(newUsername, newBio, uiState.userProfile.latitude, uiState.userProfile.longitude))
                },
                enabled = !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(text = "保存修改")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileSettingsScreenPreview() {
    BookCrosserTheme {
        ProfileSettingsScreen(
            uiState = ProfileUiState(),
            onEvent = {}
        )
    }
}