package com.huaihao.bookcrosser.ui.main.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookEvent
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookUiState


private const val exampleImageAddress = "https://i.pinimg.com/564x/cc/e4/ef/cce4ef60f77a3cf3b36f5b9897ae378d.jpg"

@Composable
fun ShelfABookScreen(uiState: ShelfABookUiState, onEvent: (ShelfABookEvent) -> Unit) {
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
                    onClick = { onEvent(ShelfABookEvent.UploadCover) },
                    label = {
                        Text(text = "上传封面")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Upload,
                            contentDescription = "上传封面",
                            tint = MaterialTheme.colorScheme.primary
                        )
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
                            label = "书名",
                            value = uiState.title,
                            onValueChange = { onEvent(ShelfABookEvent.TitleChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            label = "作者",
                            value = uiState.author,
                            onValueChange = { onEvent(ShelfABookEvent.AuthorChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            label = "ISBN",
                            value = uiState.isbn,
                            onValueChange = { onEvent(ShelfABookEvent.IsbnChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            label = "简介（可选）",
                            maxLength = 100,
                            maxLines = 4,
                            value = uiState.description,
                            onValueChange = { onEvent(ShelfABookEvent.DescriptionChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }

                Button(onClick = {
                    onEvent(ShelfABookEvent.ShelfBook)
                }, Modifier.fillMaxWidth()) {
                    Text(text = "图书上架")
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShelfABookScreenPreview() {
    BookCrosserTheme {
        ShelfABookScreen(
            ShelfABookUiState(
                coverUrl = exampleImageAddress,
                title = "哈里波特",
                author = "J.K.罗琳",
                isbn = "9787530218562",
                description = "哈利波特是一部由英国作家J.K.罗琳创作的奇幻小说，讲述了一个孤儿哈利波特在霍格沃茨魔法学校的冒险故事。"
            ),
            onEvent = {}
        )
    }
}
