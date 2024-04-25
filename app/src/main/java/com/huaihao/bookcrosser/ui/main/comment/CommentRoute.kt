package com.huaihao.bookcrosser.ui.main.comment

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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.huaihao.bookcrosser.ui.common.BaseScreenWrapper
import com.huaihao.bookcrosser.ui.common.FilterChips
import com.huaihao.bookcrosser.ui.main.Destinations.MY_REVIEW_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.REVIEW_SQUARE_ROUTE
import com.huaihao.bookcrosser.viewmodel.main.MyCommentViewModel
import com.huaihao.bookcrosser.viewmodel.main.CommentSquareViewModel
import com.huaihao.bookcrosser.viewmodel.main.SearchViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentRoute(navController: NavHostController = rememberNavController(), bookId: Long? = null) {

    var selectedScreen by rememberSaveable { mutableStateOf(REVIEW_SQUARE_ROUTE) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("图书")
                            withStyle(
                                style = SpanStyle(color = MaterialTheme.colorScheme.primary)
                            ) {
                                append("流")
                            }
                            append("言")
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                    FilterChips(
                        items = listOf(
                            REVIEW_SQUARE_ROUTE,
                            MY_REVIEW_ROUTE
                        )
                    ) { selected ->
                        selectedScreen = selected
                    }
                }
            })

        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            val squareViewModel = koinNavViewModel<CommentSquareViewModel>(
                viewModelStoreOwner = navController.getViewModelStoreOwner(navController.graph.id)
            )
            val myCommentViewModel = koinNavViewModel<MyCommentViewModel>(
                viewModelStoreOwner = navController.getViewModelStoreOwner(navController.graph.id)
            )

            when (selectedScreen) {
                REVIEW_SQUARE_ROUTE -> {
                    BaseScreenWrapper(navController = navController, viewModel = squareViewModel) {
                        CommentSquareScreen(
                            uiState = squareViewModel.state,
                            bookId = bookId,
                            onEvent = squareViewModel::onEvent
                        )
                    }
                }

                MY_REVIEW_ROUTE -> {
                    BaseScreenWrapper(navController = navController, viewModel = myCommentViewModel) {
                        MyCommentScreen(
                            uiState = myCommentViewModel.state,
                            onEvent = myCommentViewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}