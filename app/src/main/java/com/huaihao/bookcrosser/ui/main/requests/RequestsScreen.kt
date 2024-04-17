package com.huaihao.bookcrosser.ui.main.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.ui.main.search.SearchType
import com.huaihao.bookcrosser.viewmodel.main.RequestsEvent
import com.huaihao.bookcrosser.viewmodel.main.RequestsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(uiState: RequestsUiState, onEvent: (RequestsEvent) -> Unit) {
    Scaffold{ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            SearchType(modifier = Modifier)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
            ) {
                LimitedOutlinedTextField(
                    label = "书名",
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )

                FilterChip(
                    selected = true,
                    onClick = { /*TODO*/ },
                    label = { Text(text = "完全匹配") },
                    modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), contentAlignment = Alignment.CenterEnd
            ) {
                LimitedOutlinedTextField(
                    label = "作者",
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )

                FilterChip(
                    selected = true,
                    onClick = { /*TODO*/ },
                    label = { Text(text = "完全匹配") },
                    modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                )
            }

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "搜索")
            }
        }
    }
}