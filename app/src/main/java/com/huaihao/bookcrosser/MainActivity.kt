package com.huaihao.bookcrosser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.amap.api.maps.MapsInitializer
import com.huaihao.bookcrosser.ui.BookCrosserNavHost
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MapsInitializer.updatePrivacyShow(applicationContext.applicationContext, true, true)
        MapsInitializer.updatePrivacyAgree(applicationContext.applicationContext, true)

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        setContent {
            BookCrosserTheme {
                BookCrosserNavHost()
            }
        }
    }
}



