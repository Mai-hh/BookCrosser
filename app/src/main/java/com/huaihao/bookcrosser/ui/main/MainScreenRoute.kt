package com.huaihao.bookcrosser.ui.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huaihao.bookcrosser.ui.common.BaseScreenWrapper
import com.huaihao.bookcrosser.ui.main.Destinations.MAP_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.PROFILE_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.REQUESTS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.REVIEWS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.SEARCH_ROUTE
import com.huaihao.bookcrosser.ui.main.map.MapScreen
import com.huaihao.bookcrosser.ui.main.profile.ProfileRoute
import com.huaihao.bookcrosser.ui.main.requests.DriftingRoute
import com.huaihao.bookcrosser.ui.main.reviews.ReviewsRoute
import com.huaihao.bookcrosser.ui.main.search.SearchRoute
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.MapViewModel
import org.koin.androidx.compose.koinViewModel

val items = listOf(MAP_ROUTE, SEARCH_ROUTE, REQUESTS_ROUTE, REVIEWS_ROUTE, PROFILE_ROUTE)

object Destinations {
    const val MAP_ROUTE = "地图"

    const val SEARCH_ROUTE = "搜索"
    const val SHELF_BOOK_ROUTE = "起漂"
    const val REQUEST_BOOK_ROUTE = "求漂"

    const val REQUESTS_ROUTE = "漂流"
    const val REVIEWS_ROUTE = "评论"
    const val PROFILE_ROUTE = "我的"
}

private val IconImageVectors = listOf(
    Icons.Rounded.Map,
    Icons.Rounded.Search,
    Icons.Rounded.Book,
    Icons.Rounded.LocalFlorist,
    Icons.Rounded.ManageAccounts
)

@Composable
fun MainScreenRoute(
    navController: NavHostController = rememberNavController()
) {
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(IconImageVectors[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(items[selectedItem])
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = MAP_ROUTE,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(MAP_ROUTE) {
                val viewModel = koinViewModel<MapViewModel>()
                BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                    MapScreen(uiState = viewModel.state, onEvent = viewModel::onEvent)
                }
            }

            composable(SEARCH_ROUTE) {
                SearchRoute()
            }

            composable(REQUESTS_ROUTE) {
                DriftingRoute(navController = navController)
            }
            composable(REVIEWS_ROUTE) {
                ReviewsRoute()
            }

            composable(PROFILE_ROUTE) {
                ProfileRoute()
            }
        }
    }
}

@Preview
@Composable
private fun BookCrosserNavHostPreview() {
    BookCrosserTheme {
        MainScreenRoute()
    }
}