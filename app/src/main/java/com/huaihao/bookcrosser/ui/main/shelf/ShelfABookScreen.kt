package com.huaihao.bookcrosser.ui.main.shelf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme


val exampleImageAddress = "https://i.pinimg.com/564x/cc/e4/ef/cce4ef60f77a3cf3b36f5b9897ae378d.jpg"

@Composable
fun ShelfABookScreen() {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val (cover, info, action) = createRefs()
                Card(modifier = Modifier
                    .constrainAs(cover) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize()) {
                    AsyncImage(
                        model = exampleImageAddress,
                        placeholder = painterResource(id = R.mipmap.bc_logo_foreground),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                FilterChip(
                    selected = true,
                    onClick = { /*TODO*/ },
                    label = {
                        Text(text = "上传封面")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Upload, contentDescription = "上传封面", tint = MaterialTheme.colorScheme.primary)
                    },
                    modifier = Modifier.constrainAs(action) {
                        bottom.linkTo(cover.bottom)
                        end.linkTo(cover.end, margin = 8.dp)
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier) {
                    Text(
                        text = "书籍信息",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Column(Modifier.padding(vertical = 8.dp)) {
                        LimitedOutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

                Button(onClick = { }, Modifier.fillMaxWidth()) {
                    Text(text = "塞入漂流瓶")
                }
            }


        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShelfABookScreenPreview() {
    BookCrosserTheme {
        ShelfABookScreen()
    }
}
