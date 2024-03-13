package com.huaihao.bookcrosser.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.huaihao.bookcrosser.ui.Destinations.CHATS_ROUTE
import com.huaihao.bookcrosser.ui.Destinations.PROFILE_ROUTE
import com.huaihao.bookcrosser.ui.Destinations.REQUESTS_ROUTE
import com.huaihao.bookcrosser.ui.Destinations.REVIEWS_ROUTE
import com.huaihao.bookcrosser.ui.Destinations.SEARCH_ROUTE
import com.huaihao.bookcrosser.ui.profile.ProfileRoute
import com.huaihao.bookcrosser.ui.reviews.ReviewsRoute
import com.huaihao.bookcrosser.ui.search.SearchRoute

val items = listOf(SEARCH_ROUTE, REQUESTS_ROUTE, REVIEWS_ROUTE, CHATS_ROUTE, PROFILE_ROUTE)

object Destinations {
    const val SEARCH_ROUTE = "search"
    const val REQUESTS_ROUTE = "requests"
    const val REVIEWS_ROUTE = "reviews"
    const val CHATS_ROUTE = "chats"
    const val PROFILE_ROUTE = "profile"
}

private val IconImageVectors = listOf(
    Icons.Rounded.Search,
    Icons.Rounded.Book,
    Icons.Rounded.LocalFlorist,
    Icons.Rounded.ChatBubbleOutline,
    Icons.Rounded.ManageAccounts
)

@Composable
fun BookCrosserNavHost(
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
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = SEARCH_ROUTE,
        ) {
            composable(SEARCH_ROUTE) {
                SearchRoute()
            }
            composable(PROFILE_ROUTE) {
                ProfileRoute()
            }
            composable(REVIEWS_ROUTE) {
                ReviewsRoute()
            }
        }
    }
}

@Preview
@Composable
private fun BookCrosserNavHostPreview() {
    MaterialTheme {
        BookCrosserNavHost()
    }
}