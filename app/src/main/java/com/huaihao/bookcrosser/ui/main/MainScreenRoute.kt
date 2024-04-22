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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.LatLng
import com.huaihao.bookcrosser.ui.common.BaseScreenWrapper
import com.huaihao.bookcrosser.ui.main.Destinations.MAP_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.PROFILE_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.REQUESTS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.REVIEWS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.SEARCH_ROUTE
import com.huaihao.bookcrosser.ui.main.map.MapScreen
import com.huaihao.bookcrosser.ui.main.profile.ProfileScreen
import com.huaihao.bookcrosser.ui.main.requests.DriftingRoute
import com.huaihao.bookcrosser.ui.main.reviews.ReviewsRoute
import com.huaihao.bookcrosser.ui.main.search.SearchScreen
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.MapViewModel
import com.huaihao.bookcrosser.viewmodel.main.ProfileViewModel
import com.huaihao.bookcrosser.viewmodel.main.SearchViewModel
import org.koin.androidx.compose.koinViewModel

val items = listOf(MAP_ROUTE, SEARCH_ROUTE, REQUESTS_ROUTE, REVIEWS_ROUTE, PROFILE_ROUTE)

object Destinations {
    const val MAP_ROUTE = "地图"

    const val SEARCH_ROUTE = "搜索"
    const val BASIC_SEARCH_ROUTE = "基本"
    const val ISBN_SEARCH_ROUTE = "ISBN"
    const val BCID_SEARCH_ROUTE = "BCID"

    const val REQUESTS_ROUTE = "漂流"
    const val SHELF_BOOK_ROUTE = "上架"
    const val REQUEST_BOOK_ROUTE = "求漂"

    const val REVIEWS_ROUTE = "评论"
    const val REVIEW_SQUARE_ROUTE = "广场"
    const val MY_REVIEW_ROUTE = "我的评论"

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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var selectedItem by remember { mutableIntStateOf(0) }

    // 根据当前路由更新 selectedItem 的值
    when (currentRoute) {
        MAP_ROUTE, "$MAP_ROUTE/{latitude}/{longitude}" -> selectedItem = 0
        SEARCH_ROUTE -> selectedItem = 1
        REQUESTS_ROUTE -> selectedItem = 2
        REVIEWS_ROUTE -> selectedItem = 3
        PROFILE_ROUTE -> selectedItem = 4
    }


    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(IconImageVectors[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            if (selectedItem != index) {
                                selectedItem = index
                                navController.navigate(items[selectedItem])
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = PROFILE_ROUTE,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(
                route = "$MAP_ROUTE/{latitude}/{longitude}",
                arguments = listOf(
                    navArgument("latitude") { type = NavType.FloatType },
                    navArgument("longitude") { type = NavType.FloatType }
                )
            ) { backStackEntry ->
                val latitude = backStackEntry.arguments?.getFloat("latitude")?.toDouble()
                val longitude = backStackEntry.arguments?.getFloat("longitude")?.toDouble()
                val initialPos: LatLng? = if (latitude != null && longitude != null) {
                    LatLng(latitude, longitude)
                } else {
                    null
                }
                val viewModel = koinViewModel<MapViewModel>()
                BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                    MapScreen(
                        uiState = viewModel.state,
                        onEvent = viewModel::onEvent,
                        initialPosition = initialPos
                    )
                }
            }

            composable(
                route = MAP_ROUTE
            ) {
                val viewModel = koinViewModel<MapViewModel>()
                BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                    MapScreen(
                        uiState = viewModel.state,
                        onEvent = viewModel::onEvent,
                        initialPosition = null
                    )
                }
            }

            composable(SEARCH_ROUTE) {
                val viewModel = koinViewModel<SearchViewModel>()
                BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                    SearchScreen(uiState = viewModel.state, onEvent = viewModel::onEvent)
                }
            }

            composable(REQUESTS_ROUTE) {
                DriftingRoute(navController = navController)
            }
            composable(REVIEWS_ROUTE) {
                ReviewsRoute(navController = navController)
            }

            composable(PROFILE_ROUTE) {
                val viewModel = koinViewModel<ProfileViewModel>()
                BaseScreenWrapper(navController = navController, viewModel = viewModel) {
                    ProfileScreen(uiState = viewModel.state, onEvent = viewModel::onEvent)
                }
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