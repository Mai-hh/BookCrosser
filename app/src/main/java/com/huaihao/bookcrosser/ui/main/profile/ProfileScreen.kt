package com.huaihao.bookcrosser.ui.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.huaihao.bookcrosser.model.ProfileNotification
import com.huaihao.bookcrosser.model.ProfileNotificationType
import com.huaihao.bookcrosser.viewmodel.main.ProfileEvent
import com.huaihao.bookcrosser.viewmodel.main.ProfileUiState

@Composable
fun ProfileScreen(uiState: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    Scaffold { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ConstraintLayout(
                modifier = Modifier.padding(16.dp)
            ) {
                val (avatar, name, bio, settings, notifications, my, inReading) = createRefs()
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
                    text = uiState.user.username, modifier = Modifier.constrainAs(name) {
                        start.linkTo(avatar.end, margin = 16.dp)
                        top.linkTo(avatar.top)
                        bottom.linkTo(bio.top)
                    },
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = uiState.user.bio ?: "", modifier = Modifier.constrainAs(bio) {
                        start.linkTo(name.start)
                        bottom.linkTo(avatar.bottom)
                        top.linkTo(name.bottom)
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(settings) {
                    end.linkTo(parent.end)
                    top.linkTo(avatar.top)
                    bottom.linkTo(avatar.bottom)
                }) {
                    Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                }

                NotificationList(notifications = uiState.notifications, modifier = Modifier.constrainAs(notifications) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(avatar.bottom, margin = 28.dp)
                })
                
                // TODO: Add my books and in reading books
                Text(text = "我上架的书籍", modifier = Modifier.constrainAs(my) {
                    start.linkTo(parent.start)
                    top.linkTo(notifications.bottom, margin = 16.dp)
                }, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = "我借阅的书籍", modifier = Modifier.constrainAs(inReading) {
                    start.linkTo(parent.start)
                    top.linkTo(my.bottom, margin = 16.dp)
                }, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
                    .fillParentMaxWidth(1f/2f)
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


