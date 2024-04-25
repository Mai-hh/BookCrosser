package com.huaihao.bookcrosser.ui.main.requests

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.huaihao.bookcrosser.ui.common.UploadCoverDialog
import com.huaihao.bookcrosser.ui.common.UploadImageDialog
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookEvent
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookUiState


const val exampleImageAddress =
    "https://i.pinimg.com/564x/cc/e4/ef/cce4ef60f77a3cf3b36f5b9897ae378d.jpg"

@Composable
fun ShelfABookScreen(uiState: ShelfABookUiState, onEvent: (ShelfABookEvent) -> Unit) {

    if (uiState.showUploadDialog) {
        UploadImageDialog(
            onDismiss = {
                onEvent(ShelfABookEvent.DismissUploadCoverDialog)
            },
            onConfirm = { coverUrl ->
                onEvent(ShelfABookEvent.CoverUrlChange(coverUrl))
                onEvent(ShelfABookEvent.DismissUploadCoverDialog)
            },
            imageUrl = uiState.coverUrl,
            imageDescription = "封面"
        )
    }

    Column(
        Modifier
            .fillMaxSize(),
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
                val (cover, action) = createRefs()
                Card(modifier = Modifier
                    .constrainAs(cover) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize()) {

                    uiState.coverUrl.ifBlank {
                        Image(
                            painter = painterResource(id = R.mipmap.bc_logo_foreground),
                            contentDescription = "封面",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        return@Card
                    }

                    AsyncImage(
                        model = uiState.coverUrl,
                        placeholder = painterResource(id = R.mipmap.bc_logo_foreground),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                FilterChip(
                    selected = true,
                    onClick = { onEvent(ShelfABookEvent.ShowUploadCoverDialog) },
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
                .fillMaxSize()
                .weight(2f)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxSize()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
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
                            singleLine = false,
                            value = uiState.description,
                            onValueChange = { onEvent(ShelfABookEvent.DescriptionChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "位置信息",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        AssistChip(
                            onClick = {
                                onEvent(ShelfABookEvent.GetCurrentLocation)
                            },
                            label = {
                                Text(text = "获取当前位置")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = "获取当前位置",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                    }

                    Row(Modifier.padding(vertical = 8.dp)) {
                        OutlinedTextField(
                            label = {
                                Text("经度")
                            },
                            value = if (uiState.location != null) uiState.location!!.longitude.toString() else "",
                            onValueChange = { },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.weight(0.1f))
                        OutlinedTextField(
                            label = {
                                Text("纬度")
                            },
                            value = if (uiState.location != null) uiState.location!!.latitude.toString() else "",
                            onValueChange = { },
                            modifier = Modifier.weight(1f)
                        )
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
