package com.huaihao.bookcrosser.ui.main.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PinDrop
import androidx.compose.material.icons.rounded.RoundaboutRight
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.DriftingRequest
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.ui.common.CommonTextAlertDialog
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingEvent
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingUiState


@Composable
fun RequestDriftingScreen(
    uiState: RequestDriftingUiState,
    onEvent: (RequestDriftingEvent) -> Unit
) {

    if (uiState.showDriftDialog) {
        CommonTextAlertDialog(
            onDismiss = { onEvent(RequestDriftingEvent.DismissDriftDialog) },
            onConfirm = { onEvent(RequestDriftingEvent.Drift(uiState.selectedRequest!!)) },
            dialogTitle = "起漂",
            dialogText = "确认起漂\"${uiState.selectedRequest!!.book.title}\"吗？",
            icon = Icons.Rounded.RoundaboutRight
        )
    }

    if (uiState.showRejectDialog) {
        CommonTextAlertDialog(
            onDismiss = { onEvent(RequestDriftingEvent.DismissRejectDialog) },
            onConfirm = { onEvent(RequestDriftingEvent.RejectDriftingRequest) },
            dialogTitle = "拒绝",
            dialogText = "确认拒绝\"${uiState.selectedRequest!!.book.title}\"吗？",
            icon = Icons.Rounded.RoundaboutRight
        )
    }

    LaunchedEffect(Unit) {
        onEvent(RequestDriftingEvent.LoadDriftingRequests)
    }

    if (uiState.request.isEmpty()) {
        RequestPlaceHolderScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.request) { request ->
                BookRequestCard(
                    request = request,
                    onLocateSelected = {
                        onEvent(RequestDriftingEvent.Locate(request))
                    },
                    onDriftSelected = {
                        onEvent(RequestDriftingEvent.ShowDriftDialog(request))
                    },
                    onRejectSelected = {
                        onEvent(RequestDriftingEvent.ShowRejectDialog(request))
                    }
                )
            }
        }
    }
}

@Composable
fun BookRequestCard(
    modifier: Modifier = Modifier,
    request: DriftingRequest,
    onLocateSelected: () -> Unit = {},
    onDriftSelected: () -> Unit = {},
    onRejectSelected: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 150.dp)
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()
        ) {
            val (box1, box2, actions) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(box1) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .width(80.dp)
                    .fillMaxHeight(),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(request.book.coverUrl)
                        .error(R.mipmap.bc_logo_foreground)
                        .build(),
                    contentDescription = request.book.title,
                    modifier = Modifier.fillMaxHeight(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(box2) {
                        top.linkTo(parent.top)
                        start.linkTo(box1.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = request.book.title,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        request.requester.username?.let {
                            AssistChip(onClick = {},
                                label = { Text(text = "来自: $it") }
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier
                .constrainAs(actions) {
                    top.linkTo(box2.top, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    bottom.linkTo(parent.bottom, margin = 8.dp)
                }) {
                OutlinedButton(
                    onClick = {
                        onDriftSelected()
                    }
                ) {
                    Text(text = "起漂")
                }

                Button(
                    onClick = { onRejectSelected() }
                ) {
                    Text(text = "拒绝")
                }

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        onLocateSelected()
                    },
                ) {
                    Icon(Icons.Rounded.PinDrop, contentDescription = "定位")
                }
            }
        }
    }
}

@Composable
fun RequestPlaceHolderScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "没有书友请求你的图书~",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRequestDriftingScreen() {
    BookCrosserTheme {
        RequestPlaceHolderScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookRequestCard() {
    BookCrosserTheme {
        BookRequestCard(
            request = DriftingRequest(
                id = 1,
                book = Book(
                    id = 1,
                    ownerId = 1,
                    ownerUsername = null,
                    uploaderId = 1,
                    title = "书名",
                    coverUrl = "https://img3.doubanio.com/view/subject/l/public/s3365739.jpg",
                    latitude = 0.0,
                    longitude = 0.0,
                    status = "AVAILABLE",
                    author = "作者",
                    isbn = "ISBN",
                    description = "描述",
                    createdAt = "创建时间",
                    updatedAt = "更新时间"
                ),
                requester = User(
                    username = "用户名",
                    email = "邮箱"
                )
            )
        )
    }
}