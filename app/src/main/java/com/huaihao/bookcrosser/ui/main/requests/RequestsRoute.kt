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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.huaihao.bookcrosser.ui.main.search.FilterChips
import com.huaihao.bookcrosser.ui.main.shelf.ShelfABookScreen
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsRoute(navController: NavHostController = rememberNavController()) {

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
                    FilterChips(items = listOf("塞入漂流瓶", "寻找漂流瓶"))
                }
            })

        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            ShelfABookScreen()
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RequestsRoutePre() {
    BookCrosserTheme {
        RequestsRoute()
    }
}
