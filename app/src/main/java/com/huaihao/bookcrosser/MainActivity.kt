package com.huaihao.bookcrosser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.huaihao.bookcrosser.ui.BookCrosserNavHost
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookCrosser()
        }
    }
}

@Composable
fun BookCrosser() {
    BookCrosserTheme {
        BookCrosserNavHost()
    }
}





