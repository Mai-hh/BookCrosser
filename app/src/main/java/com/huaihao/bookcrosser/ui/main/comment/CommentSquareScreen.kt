package com.huaihao.bookcrosser.ui.main.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.Comment
import com.huaihao.bookcrosser.model.CommentDTO
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.ui.main.profile.PlaceHolderScreen
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.CommentSquareEvent
import com.huaihao.bookcrosser.viewmodel.main.CommentSquareUiState

@Composable
fun CommentSquareScreen(uiState: CommentSquareUiState, onEvent: (CommentSquareEvent) -> Unit) {

    LaunchedEffect(uiState.comments) {
        onEvent(CommentSquareEvent.LoadAllComments)
    }

    if (uiState.comments.isEmpty()) {
        PlaceHolderScreen(text = "留言广场空空如也~")
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(uiState.comments) { comment ->
            CommentCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                comment = comment
            )
        }
    }
}


@Composable
fun CommentCard(
    modifier: Modifier = Modifier,
    comment: CommentDTO,
    onRequestSelected: (() -> Unit)? = {},
    onCommentUpdateSelected: (() -> Unit)? = null,
    onCommentDeleteSelected: (() -> Unit)? = null,
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        BoxWithConstraints {
            val cardHeight = maxHeight
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (content, preview, actions, sender) = createRefs()
                Image(
                    painter = painterResource(id = R.mipmap.bc_logo_foreground),
                    contentDescription = comment.book.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.constrainAs(preview) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.value(200.dp)
                    }
                )

                Column(
                    modifier = Modifier
                        .constrainAs(content) {
                            top.linkTo(preview.bottom)
                            start.linkTo(parent.start)
                            width = Dimension.fillToConstraints
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = comment.book.title,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = comment.comment.content,
                        maxLines = if (expanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .heightIn(min = 100.dp)
                            .fillMaxWidth()
                    )
                }
                Box(modifier = Modifier.constrainAs(sender) {
                    bottom.linkTo(preview.bottom, margin = 16.dp)
                    end.linkTo(preview.end, margin = 16.dp)
                }) {
                    if (onCommentDeleteSelected == null) {
                        CommentTag(sender = comment.sender)
                    } else {
                        IconButton(
                            onClick = onCommentDeleteSelected,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                        ) {
                            Icon(Icons.Rounded.DeleteForever, contentDescription = "删除")
                        }
                    }
                }

                Row(modifier = Modifier.constrainAs(actions) {
                    top.linkTo(content.bottom)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }) {
                    onRequestSelected?.let {
                        OutlinedButton(onClick = it) {
                            Text(text = "求漂")
                        }
                    }

                    onCommentUpdateSelected?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(onClick = it) {
                            Text(text = "编辑")
                        }
                    }

                    onCommentDeleteSelected?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(onClick = it) {
                            Text(text = "删除")
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    val rotationAngle by animateFloatAsState(
                        targetValue = if (expanded) 180f else 0f,
                        animationSpec = tween(durationMillis = 300), label = ""
                    )

                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = { expanded = expanded.not() },
                        modifier = Modifier.rotate(rotationAngle)
                    ) {
                        Icon(Icons.Rounded.ExpandMore, contentDescription = "展开")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CommentTag(onTagClick: () -> Unit = {}, sender: User? = null) {
    var isTagSelected by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = {
        isTagSelected = isTagSelected.not()
    }) {
        Row(modifier = Modifier.padding(4.dp)) {
            Icon(Icons.Rounded.Person, contentDescription = null)
            AnimatedVisibility(visible = isTagSelected) {
                Text(
                    text = sender?.username ?: "未知", modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewSquareScreenPre() {
    BookCrosserTheme {
        CommentSquareScreen(
            CommentSquareUiState()
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewCardPreview() {
    BookCrosserTheme {
        CommentCard(
            modifier = Modifier.padding(16.dp),
            onCommentUpdateSelected = {},
            onCommentDeleteSelected = {},
            comment = CommentDTO(
                comment = Comment(
                    id = 1,
                    bookId = 1,
                    userId = 1,
                    content = "content",
                    createdAt = "2021-09-01",
                    updatedAt = "2021-09-01"
                ),
                sender = User(
                    username = "username",
                    email = "email",
                    password = "password",
                    avatar = "avatar",
                    bio = "bio",
                    location = "location",
                    latitude = 0.0,
                    longitude = 0.0,
                    createdAt = "2021-09-01",
                    updatedAt = "2021-09-01"
                ),
                book = Book(
                    id = 1,
                    title = "title",
                    author = "author",
                    isbn = "isbn",
                    description = "description",
                    coverUrl = "coverUrl",
                    latitude = 0.0,
                    longitude = 0.0,
                    createdAt = "2021-09-01",
                    updatedAt = "2021-09-01",
                    ownerId = 1,
                    ownerUsername = "ownerUsername",
                    uploaderId = 1,
                    status = "status"
                )
            )
        )
    }
}