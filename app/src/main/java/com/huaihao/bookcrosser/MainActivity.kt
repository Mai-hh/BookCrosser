package com.huaihao.bookcrosser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.amap.api.maps.MapsInitializer
import com.huaihao.bookcrosser.ui.BookCrosserNavHost
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        setContent {
            BookCrosserTheme {
                BookCrosserNavHost()
            }
        }
    }
}





