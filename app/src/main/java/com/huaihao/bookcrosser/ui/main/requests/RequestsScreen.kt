package com.huaihao.bookcrosser.ui.main.requests

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.huaihao.bookcrosser.viewmodel.main.RequestsEvent
import com.huaihao.bookcrosser.viewmodel.main.RequestsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(uiState: RequestsUiState, onEvent: (RequestsEvent) -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Requests")
            })
        }
    ) { paddingValues ->
        
    }

}