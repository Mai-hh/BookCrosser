package com.huaihao.bookcrosser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amap.api.maps.MapView
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.LatLngBounds
import com.huaihao.bookcrosser.ui.BookCrosserNavHost
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.melody.map.gd_compose.GDMap
import com.melody.map.gd_compose.poperties.MapProperties
import com.melody.map.gd_compose.poperties.MapUiSettings
import com.melody.map.gd_compose.position.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MapsInitializer.updatePrivacyShow(applicationContext.applicationContext, true, true)
        MapsInitializer.updatePrivacyAgree(applicationContext.applicationContext, true)

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BookCrosserNavHost()
            }
        }
    }
}



