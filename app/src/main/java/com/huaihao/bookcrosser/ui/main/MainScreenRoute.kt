package com.huaihao.bookcrosser.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.ChatBubbleOutline
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
import com.huaihao.bookcrosser.ui.main.Destinations.MAP_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.PROFILE_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.REQUESTS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.REVIEWS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.SEARCH_ROUTE
import com.huaihao.bookcrosser.ui.main.map.MapScreen
import com.huaihao.bookcrosser.ui.main.profile.ProfileRoute
import com.huaihao.bookcrosser.ui.main.requests.RequestsRoute
import com.huaihao.bookcrosser.ui.main.reviews.ReviewsRoute
import com.huaihao.bookcrosser.ui.main.search.SearchRoute
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme

val items = listOf(MAP_ROUTE, SEARCH_ROUTE, REQUESTS_ROUTE, REVIEWS_ROUTE, PROFILE_ROUTE)

object Destinations {
    const val MAP_ROUTE = "map"
    const val SEARCH_ROUTE = "search"
    const val REQUESTS_ROUTE = "requests"
    const val REVIEWS_ROUTE = "reviews"
    const val PROFILE_ROUTE = "profile"
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
        ) {
            composable(MAP_ROUTE) {
                MapScreen()
            }

            composable(SEARCH_ROUTE) {
                SearchRoute()
            }
            composable(REQUESTS_ROUTE) {
                RequestsRoute()
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