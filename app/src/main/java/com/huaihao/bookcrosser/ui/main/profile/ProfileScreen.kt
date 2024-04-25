package com.huaihao.bookcrosser.ui.main.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PinDrop
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.BookProfileItem
import com.huaihao.bookcrosser.model.toProfileItem
import com.huaihao.bookcrosser.ui.common.CommonTextAlertDialog
import com.huaihao.bookcrosser.ui.common.FilterChips
import com.huaihao.bookcrosser.ui.common.PostBookCommentDialog
import com.huaihao.bookcrosser.ui.common.UpdateBookDialog
import com.huaihao.bookcrosser.ui.main.Destinations.MY_BORROWED_BOOKS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.MY_REQUESTS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.MY_UPLOADED_BOOKS_ROUTE
import com.huaihao.bookcrosser.viewmodel.main.ProfileEvent
import com.huaihao.bookcrosser.viewmodel.main.ProfileUiState

private val items = listOf(
    MY_UPLOADED_BOOKS_ROUTE,
    MY_BORROWED_BOOKS_ROUTE,
    MY_REQUESTS_ROUTE
)

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    firstScreen: String? = MY_UPLOADED_BOOKS_ROUTE
) {

    LaunchedEffect(Unit) {
        onEvent(ProfileEvent.LoadUserProfile)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            val (avatar, name, bio, settings, my) = createRefs()
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.1f))
                    .constrainAs(avatar) {
                        top.linkTo(parent.top)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Text(
                text = uiState.userProfile.username,
                modifier = Modifier.constrainAs(name) {
                    start.linkTo(avatar.end, margin = 16.dp)
                    top.linkTo(avatar.top)
                    bottom.linkTo(bio.top)
                },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = uiState.userProfile.bio ?: "简介空空如也~",
                modifier = Modifier.constrainAs(bio) {
                    start.linkTo(name.start)
                    bottom.linkTo(avatar.bottom)
                    top.linkTo(name.bottom)
                },
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            var showLogoutAlert by remember { mutableStateOf(false) }

            if (showLogoutAlert) {
                CommonTextAlertDialog(
                    onDismiss = { showLogoutAlert = false },
                    onConfirm = {
                        onEvent(ProfileEvent.Logout)
                    },
                    dialogTitle = "退出确认",
                    dialogText = "你确定要退出登录吗？",
                    icon = Icons.AutoMirrored.Rounded.Logout
                )
            }

            Row(modifier = Modifier.constrainAs(settings) {
                end.linkTo(parent.end)
                top.linkTo(avatar.top)
                bottom.linkTo(avatar.bottom)
            }) {
                IconButton(onClick = {
                    onEvent(ProfileEvent.NavToSettings)
                }) {
                    Icon(Icons.Rounded.Settings, contentDescription = "退出")
                }

                IconButton(onClick = {
                    showLogoutAlert = true
                }) {
                    Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = "退出")
                }
            }


            Column(modifier = Modifier.constrainAs(my) {
                start.linkTo(parent.start)
                top.linkTo(avatar.bottom, margin = 16.dp)
            }) {
                Text(
                    text = "我的图书",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                var selectedScreen by remember { mutableStateOf(firstScreen) }


                FilterChips(
                    items = items,
                    onSelected = { selected ->
                        selectedScreen = selected
                    },
                    selectedIndex = items.indexOf(selectedScreen)
                )

                when (selectedScreen) {
                    MY_UPLOADED_BOOKS_ROUTE -> {
                        MyUploadedScreen(uiState, onEvent)
                    }

                    MY_BORROWED_BOOKS_ROUTE -> {
                        MyBorrowedScreen(uiState, onEvent)
                    }

                    MY_REQUESTS_ROUTE -> {
                        MyRequestsScreen(uiState, onEvent)
                    }
                }
            }
        }
    }

}

@Composable
fun PlaceHolderScreen(text: String = "空空如也~") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
fun MyRequestsScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    if (uiState.userProfile.booksInRequesting.isNullOrEmpty()) {
        PlaceHolderScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            items(uiState.userProfile.booksInRequesting!!) { book ->
                BookProfileCard(
                    book = book.toProfileItem(),
                    onLocateSelected = {
                        onEvent(ProfileEvent.LocatedBook(book))
                    },
                    showUpdateBookBtn = false
                )
            }
        }
    }
}


// 这个页面可以查看我借阅的图书，并且决定是否起漂
@Composable
fun MyBorrowedScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {

    if (uiState.userProfile.booksBorrowed.isNullOrEmpty()) {
        PlaceHolderScreen(text = "你还没有借阅/可留言的图书~")
    } else {
        uiState.userProfile.booksBorrowed?.let { books ->
            var selectedBook: Book? by remember { mutableStateOf(null) }

            if (uiState.showCommentDialog) {
                selectedBook?.let { book ->
                    PostBookCommentDialog(
                        onDismiss = { onEvent(ProfileEvent.DismissCommentDialog) },
                        onConfirm = {
                            onEvent(it)
                        },
                        book = book,
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                items(books) { book ->
                    BookProfileCardBorrowed(
                        book = book.toProfileItem(),
                        onComment = {
                            selectedBook = book
                            onEvent(ProfileEvent.ShowCommentDialog)
                        },
                        onLookForComment = {
                            onEvent(ProfileEvent.NavToBookComments(book.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MyUploadedScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    if (uiState.userProfile.booksUploaded.isNullOrEmpty()) {
        PlaceHolderScreen()
    } else {

        uiState.userProfile.booksUploaded?.let { books ->
            var selectedBook: Book? by remember { mutableStateOf(null) }

            if (uiState.showDriftingFinishDialog) {
                selectedBook?.let { book ->
                    CommonTextAlertDialog(
                        onDismiss = { onEvent(ProfileEvent.DismissFinishDriftingDialog) },
                        onConfirm = { onEvent(ProfileEvent.DriftingFinish(book)) },
                        dialogTitle = "收漂确认",
                        dialogText = "你确定要收漂\"${book.title}\"吗？\n这将请求此书的持有者归还图书",
                        icon = Icons.Rounded.Route
                    )
                }
            }

            if (uiState.showUpdateBookDialog) {
                selectedBook?.let {
                    UpdateBookDialog(
                        onDismiss = {
                            onEvent(ProfileEvent.DismissUpdateBookDialog)
                        },
                        onConfirm = { event ->
                            onEvent(event)
                        },
                        book = it,
                        isUpdating = uiState.isUpdatingBook
                    )
                }
            }

            if (uiState.showDeleteBookDialog) {
                selectedBook?.let {
                    CommonTextAlertDialog(
                        onDismiss = { onEvent(ProfileEvent.DismissDeleteBookDialog) },
                        onConfirm = { onEvent(ProfileEvent.Delete(it.id)) },
                        dialogTitle = "下架确认",
                        dialogText = "你确定要下架\"${it.title}\"吗？\n这将删除此书的所有信息和相关留言",
                        icon = Icons.Rounded.DeleteOutline
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                items(books) { book ->

                    BookProfileCard(
                        book = book.toProfileItem(),
                        onBookInfoChanged = {
                            selectedBook = book
                            onEvent(ProfileEvent.ShowUpdateBookDialog)
                        },
                        onLocateSelected = {
                            onEvent(
                                ProfileEvent.LocatedBook(
                                    book
                                )
                            )
                        },
                        onDriftingFinish = {
                            selectedBook = book
                            onEvent(ProfileEvent.ShowFinishDriftingDialog)
                        },
                        onBookDelete = {
                            selectedBook = book
                            onEvent(ProfileEvent.ShowDeleteBookDialog)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookProfileCardBorrowed(
    modifier: Modifier = Modifier,
    book: BookProfileItem,
    onComment: () -> Unit = {},
    onLookForComment: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .heightIn(max = 150.dp)
            .padding(vertical = 8.dp)
    ) {
        val (frame, title, author, actions) = createRefs()
        ElevatedCard(modifier = Modifier
            .fillMaxSize()
            .constrainAs(frame) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {

        }

        Text(
            text = book.title,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(frame.top, margin = 12.dp)
                    start.linkTo(frame.start)
                }
                .padding(horizontal = 12.dp),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "作者: ${book.author}",
            modifier = Modifier
                .constrainAs(author) {
                    top.linkTo(title.bottom)
                    start.linkTo(frame.start, margin = 12.dp)
                },
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(modifier = Modifier
            .constrainAs(actions) {
                end.linkTo(frame.end, margin = 8.dp)
                bottom.linkTo(frame.bottom, margin = 8.dp)
            }
            .padding(8.dp)
        ) {
            OutlinedButton(
                onClick = { onComment() },
                colors = ButtonDefaults.outlinedButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "写留言")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onLookForComment() },
            ) {
                Text(text = "查看留言")
            }
        }
    }
}

@Composable
fun BookProfileCard(
    book: BookProfileItem,
    onLocateSelected: () -> Unit = {},
    onBookInfoChanged: () -> Unit = {},
    onDriftingFinish: () -> Unit = {},
    onBookDelete: () -> Unit = {},
    showUpdateBookBtn: Boolean = true
) {
    var showStatusCard by remember {
        mutableStateOf(false)
    }

    val uploadedAndOwned by remember {
        mutableStateOf(book.ownerId == book.uploaderId)
    }

    Surface {
        Card(modifier = Modifier.padding(vertical = 8.dp)) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (title, author, description, actions, status) = createRefs()
                OutlinedCard(
                    modifier = Modifier
                        .constrainAs(status) {
                            top.linkTo(actions.bottom, margin = 8.dp)
                            start.linkTo(parent.start, margin = 8.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                            width = Dimension.fillToConstraints
                            bottom.linkTo(parent.bottom, margin = 8.dp)  // 确保卡片底部根据状态文本调整
                        }
                ) {
                    AnimatedVisibility(
                        visible = showStatusCard,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "持有者: ${book.ownerUsername ?: "未知"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "更新时间: ${book.updatedAt}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "状态: ${book.status}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                Text(
                                    text = "ISBN: ${book.isbn}",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }

                Text(
                    text = book.title,
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top, margin = 12.dp)
                            start.linkTo(parent.start)
                        }
                        .padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "作者: ${book.author}",
                    modifier = Modifier
                        .constrainAs(author) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start, margin = 12.dp)
                        },
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = book.description,
                    modifier = Modifier
                        .constrainAs(description) {
                            start.linkTo(parent.start, margin = 12.dp)
                            top.linkTo(author.bottom, margin = 8.dp)
                            end.linkTo(parent.end, margin = 12.dp)
                            width = Dimension.fillToConstraints
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Row(modifier = Modifier
                    .constrainAs(actions) {
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(description.bottom, margin = 16.dp)
                    }
                    .padding(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            showStatusCard = !showStatusCard
                        },
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(text = "查看状态")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (!uploadedAndOwned) {
                        OutlinedButton(
                            onClick = {
                                onDriftingFinish()
                            },
                            colors = ButtonDefaults.outlinedButtonColors().copy(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "收漂")
                        }
                    }

                    if (uploadedAndOwned) {
                        OutlinedIconButton(
                            onClick = {
                                onBookDelete()
                            }
                        ) {
                            Icon(Icons.Rounded.DeleteOutline, contentDescription = "下架")
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = {
                            onLocateSelected()
                        },
                    ) {
                        Icon(Icons.Rounded.PinDrop, contentDescription = "收藏")
                    }

                    if (showUpdateBookBtn) {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                                onBookInfoChanged()
                            },
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            Icon(Icons.Rounded.EditNote, contentDescription = "收藏")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(uiState = ProfileUiState(), onEvent = {})
    }
}


@Preview(showBackground = true)
@Composable
fun BookProfileCardPreview() {
    MaterialTheme {
        BookProfileCard(
            book = BookProfileItem(
                title = "书名",
                author = "作者",
                description = stringResource(id = R.string.content_test),
                status = "借阅中",
                coverUrl = null,
                updatedAt = "2021-09-09",
                uploaderId = 1,
                ownerId = 2,
                isbn = "1234567890"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookProfileCardBorrowedPreview() {
    MaterialTheme {
        BookProfileCardBorrowed(
            book = BookProfileItem(
                title = "书名",
                author = "作者",
                description = "描述",
                status = "借阅中",
                coverUrl = null,
                updatedAt = "2021-09-09",
                uploaderId = 1,
                ownerId = 1,
                isbn = "1234567890"
            )
        )
    }
}

