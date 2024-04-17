package com.huaihao.bookcrosser.ui.main.requests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.huaihao.bookcrosser.ui.common.BaseScreenWrapper
import com.huaihao.bookcrosser.ui.common.FilterChips
import com.huaihao.bookcrosser.ui.main.Destinations.REQUEST_BOOK_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.SHELF_BOOK_ROUTE
import com.huaihao.bookcrosser.ui.main.requests.find.RequestDriftingScreen
import com.huaihao.bookcrosser.ui.main.requests.shelf.ShelfABookScreen
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingViewModel
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriftingRoute(navController: NavHostController = rememberNavController()) {

    var selectedScreen by rememberSaveable { mutableStateOf(SHELF_BOOK_ROUTE) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "图书漂流",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                    FilterChips(items = listOf(SHELF_BOOK_ROUTE, REQUEST_BOOK_ROUTE)) { selected ->
                        selectedScreen = selected
                    }
                }
            })

        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            val shelfViewModel = koinViewModel<ShelfABookViewModel>()
            val requestViewModel = koinViewModel<RequestDriftingViewModel>()
            when (selectedScreen) {
                SHELF_BOOK_ROUTE -> {
                    BaseScreenWrapper(navController = navController, viewModel = shelfViewModel) {
                        ShelfABookScreen(
                            uiState = shelfViewModel.state,
                            onEvent = shelfViewModel::onEvent
                        )
                    }
                }

                REQUEST_BOOK_ROUTE -> {
                    BaseScreenWrapper(navController = navController, viewModel = requestViewModel) {
                        RequestDriftingScreen(
                            uiState = requestViewModel.state,
                            onEvent = requestViewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RequestsRoutePre() {
    BookCrosserTheme {
        DriftingRoute()
    }
}
