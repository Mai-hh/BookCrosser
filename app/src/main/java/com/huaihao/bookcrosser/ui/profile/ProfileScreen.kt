package com.huaihao.bookcrosser.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    ConstraintLayout {
                        val (avatar, name) = createRefs()
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(color = Color.Black)
                                .constrainAs(avatar) {
                                    top.linkTo(parent.top)
                                }
                        ) {

                        }
                        Text(
                            text = uiState.name, modifier = Modifier.constrainAs(name) {
                                start.linkTo(avatar.end, margin = 8.dp)
                                bottom.linkTo(avatar.bottom)
                            }
                        )
                    }

                },
                actions = {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = "Settings")
                },
                navigationIcon = {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ElevatedCard {

            }
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    BookCrosserTheme {
        ProfileScreen(uiState = ProfileUiState(), onEvent = {})
    }
}