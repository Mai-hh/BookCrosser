package com.huaihao.bookcrosser.ui.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PinDrop
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.gms.maps.model.LatLng
import com.huaihao.bookcrosser.model.BookProfileItem
import com.huaihao.bookcrosser.model.ProfileNotification
import com.huaihao.bookcrosser.model.ProfileNotificationType
import com.huaihao.bookcrosser.model.toProfileItem
import com.huaihao.bookcrosser.ui.common.FilterChips
import com.huaihao.bookcrosser.ui.main.Destinations.BOOKS_WAITING_FOR_COMMENT_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.MY_BORROWED_BOOKS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.MY_REQUESTS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.MY_UPLOADED_BOOKS_ROUTE
import com.huaihao.bookcrosser.viewmodel.main.ProfileEvent
import com.huaihao.bookcrosser.viewmodel.main.ProfileUiState

@Composable
fun ProfileScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {

    LaunchedEffect(Unit) {
        onEvent(ProfileEvent.LoadUserProfile)
    }

    Scaffold { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = Modifier.padding(16.dp)
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
                    LogoutAlert(
                        onDismiss = { showLogoutAlert = false },
                        onConfirm = {
                            onEvent(ProfileEvent.Logout)
                        }
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

                    var selectedScreen by remember { mutableStateOf(MY_UPLOADED_BOOKS_ROUTE) }
                    FilterChips(
                        items = listOf(
                            MY_UPLOADED_BOOKS_ROUTE,
                            MY_BORROWED_BOOKS_ROUTE,
                            BOOKS_WAITING_FOR_COMMENT_ROUTE,
                            MY_REQUESTS_ROUTE
                        ),
                        onSelected = { selected ->
                            selectedScreen = selected
                        }
                    )

                    when (selectedScreen) {
                        MY_UPLOADED_BOOKS_ROUTE -> {
                            MyUploadedScreen(uiState, onEvent)
                        }

                        MY_BORROWED_BOOKS_ROUTE -> {
                            MyBorrowedScreen(uiState, onEvent)
                        }

                        BOOKS_WAITING_FOR_COMMENT_ROUTE -> {
                            MyWait4CommentScreen(uiState, onEvent)
                        }

                        MY_REQUESTS_ROUTE -> {
                            MyRequestsScreen(uiState, onEvent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceHolderScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "空空如也~",
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(uiState.userProfile.booksInRequesting!!) { book ->
                BookProfileCard(book = book.toProfileItem())
            }
        }
    }
}

@Composable
fun NotificationList(notifications: List<ProfileNotification>, modifier: Modifier) {

    LazyRow(modifier = modifier.fillMaxWidth()) {
        items(notifications) { notification ->
            Box(
                modifier = Modifier
                    .fillParentMaxWidth(1f / 2f)
            ) {
                NotificationCard(notification)
            }
        }
    }
}

@Composable
fun NotificationCard(notification: ProfileNotification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp)
            .height(80.dp),
    ) {
        ConstraintLayout(modifier = Modifier.padding(10.dp)) {
            val (icon, title, message) = createRefs()
            Icon(
                imageVector = notification.type.iconImageVector,
                contentDescription = null,
                modifier = Modifier.constrainAs(icon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )
            Text(text = notification.title, modifier = Modifier.constrainAs(title) {
                start.linkTo(icon.end, margin = 8.dp)
                bottom.linkTo(icon.bottom)
            }, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))

        }
    }
}

@Composable
fun BookProfileCardBorrowed(
    modifier: Modifier = Modifier,
    book: BookProfileItem,
    onDriftingFinish: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .heightIn(max = 150.dp)
            .padding(vertical = 8.dp)
    ) {
        val (frame, title, author, description, actions, status) = createRefs()
        ElevatedCard(modifier = Modifier
            .fillMaxSize()
            .constrainAs(frame) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {

        }

        FilterChip(
            selected = true,
            onClick = { },
            label = {
                Text(text = "状态: ${book.status}")
            },

            modifier = Modifier.constrainAs(status) {
                bottom.linkTo(frame.bottom, margin = 8.dp)
                start.linkTo(frame.start, margin = 8.dp)
            }
        )

        Text(
            text = book.title,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(frame.top, margin = 12.dp)
                    start.linkTo(frame.start)
                }
                .padding(horizontal = 12.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = book.author,
            modifier = Modifier
                .constrainAs(author) {
                    top.linkTo(title.bottom)
                    start.linkTo(frame.start, margin = 12.dp)
                },
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = book.description,
            modifier = Modifier
                .constrainAs(description) {
                    start.linkTo(title.end)
                    top.linkTo(title.top)
                    end.linkTo(frame.end, margin = 8.dp)
                    width = Dimension.fillToConstraints
                },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Row(modifier = Modifier
            .constrainAs(actions) {
                end.linkTo(frame.end, margin = 8.dp)
                bottom.linkTo(frame.bottom, margin = 8.dp)
            }) {
            OutlinedButton(
                onClick = { onDriftingFinish() },
                colors = ButtonDefaults.outlinedButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "追踪")
            }
        }
    }
}

@Composable
fun BookProfileCard(
    modifier: Modifier = Modifier,
    book: BookProfileItem,
    onLocateSelected: () -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
            .heightIn(max = 150.dp)
            .padding(vertical = 8.dp)
    ) {
        val (frame, title, author, description, actions, status) = createRefs()
        ElevatedCard(modifier = Modifier
            .fillMaxSize()
            .constrainAs(frame) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {

        }

        FilterChip(
            selected = true,
            onClick = { },
            label = {
                Text(text = "状态: ${book.status}")
            },

            modifier = Modifier.constrainAs(status) {
                bottom.linkTo(frame.bottom, margin = 8.dp)
                start.linkTo(frame.start, margin = 8.dp)
            }
        )

        Text(
            text = book.title,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(frame.top, margin = 12.dp)
                    start.linkTo(frame.start)
                }
                .padding(horizontal = 12.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = book.author,
            modifier = Modifier
                .constrainAs(author) {
                    top.linkTo(title.bottom)
                    start.linkTo(frame.start, margin = 12.dp)
                },
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = book.description,
            modifier = Modifier
                .constrainAs(description) {
                    start.linkTo(title.end)
                    top.linkTo(title.top)
                    end.linkTo(frame.end, margin = 8.dp)
                    width = Dimension.fillToConstraints
                },
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Row(modifier = Modifier
            .constrainAs(actions) {
                end.linkTo(frame.end, margin = 8.dp)
                bottom.linkTo(frame.bottom, margin = 8.dp)
            }) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.outlinedButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "追踪")
            }

            Spacer(modifier = Modifier.width(8.dp))

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
        }
    }
}


// 这个页面可以查看我借阅的图书，并且决定是否起漂
@Composable
fun MyBorrowedScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    if (uiState.userProfile.booksBorrowed.isNullOrEmpty()) {
        PlaceHolderScreen()
    } else {
        uiState.userProfile.booksBorrowed?.let { books ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books) { book ->
                    if (book.uploaderId == uiState.userProfile.id) {
                        BookProfileCard(
                            book = book.toProfileItem(),
                            onLocateSelected = {
                                onEvent(
                                    ProfileEvent.LocatedBook(
                                        LatLng(book.latitude, book.longitude)
                                    )
                                )
                            })
                    } else {
                        BookProfileCardBorrowed(
                            book = book.toProfileItem(),
                            onDriftingFinish = {
                                onEvent(ProfileEvent.DriftingFinish(book))
                            }
                        )
                    }
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
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books) { book ->
                    BookProfileCard(book = book.toProfileItem(), onLocateSelected = {
                        onEvent(
                            ProfileEvent.LocatedBook(
                                LatLng(book.latitude, book.longitude)
                            )
                        )
                    })
                }
            }
        }
    }
}


@Composable
fun MyWait4CommentScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    if (uiState.userProfile.bookUncommented.isNullOrEmpty()) {
        PlaceHolderScreen()
    } else {
        uiState.userProfile.bookUncommented?.let { books ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(books) { book ->
                    BookProfileCard(book = book.toProfileItem())
                }
            }
        }
    }
}

@Composable
fun LogoutAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "退出登录？",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("确定")
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

@Preview
@Composable
private fun NotificationListPreview() {
    MaterialTheme {
        NotificationList(
            listOf(
                ProfileNotification(
                    type = ProfileNotificationType.BookRequest,
                    title = "Hello",
                    message = "Hello, World",
                    time = "2021-09-01"
                ),
                ProfileNotification(
                    type = ProfileNotificationType.BookReturn,
                    title = "Hello",
                    message = "Hello, World",
                    time = "2021-09-01"
                ),
                ProfileNotification(
                    type = ProfileNotificationType.BookRequest,
                    title = "Hello",
                    message = "Hello, World",
                    time = "2021-09-01"
                ),
            ),
            Modifier,
        )
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
                description = "描述",
                status = "借阅中",
                coverUrl = null
            )
        )
    }
}


