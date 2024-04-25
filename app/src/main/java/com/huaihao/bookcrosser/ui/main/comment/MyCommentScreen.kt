package com.huaihao.bookcrosser.ui.main.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Comment
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huaihao.bookcrosser.ui.common.CommonTextAlertDialog
import com.huaihao.bookcrosser.ui.common.UpdateBookCommentDialog
import com.huaihao.bookcrosser.ui.main.profile.PlaceHolderScreen
import com.huaihao.bookcrosser.viewmodel.main.MyCommentEvent
import com.huaihao.bookcrosser.viewmodel.main.MyCommentUiState


@Composable
fun MyCommentScreen(uiState: MyCommentUiState, onEvent: (MyCommentEvent) -> Unit) {

    LaunchedEffect(key1 = uiState.comments) {
        onEvent(MyCommentEvent.LoadComments)
    }

    if (uiState.showDeleteDialog) {
        uiState.selectedComment?.comment?.let { comment ->
            CommonTextAlertDialog(
                onDismissRequest = {
                    onEvent(MyCommentEvent.DismissDeleteDialog)
                },
                onConfirmation = {
                    onEvent(MyCommentEvent.DeleteComment(comment.id))
                },
                dialogTitle = "删除留言",
                dialogText = "确定要删除这条留言吗？",
                icon = Icons.Rounded.QuestionMark
            )
        }
    }

    if (uiState.showUpdateDialog) {
        uiState.selectedComment?.comment?.let { comment ->
            UpdateBookCommentDialog(
                onDismiss = {
                    onEvent(MyCommentEvent.DismissUpdateDialog)
                },
                onConfirm = { event ->
                    onEvent(event)
                },
                comment = comment
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (uiState.comments.isEmpty()) {
            PlaceHolderScreen(text = "留言空空如也~")
            FloatingActionButton(
                onClick = { onEvent(MyCommentEvent.NavToComment) },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.Comment,
                    contentDescription = "Add Comment"
                )
            }
            return
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(uiState.comments) { comment ->
                CommentCard(
                    comment = comment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 16.dp),
                    onCommentUpdateSelected = {
                        onEvent(MyCommentEvent.ShowUpdateDialog(comment))
                    },
                    onCommentDeleteSelected = {
                        onEvent(MyCommentEvent.ShowDeleteDialog(comment))
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { onEvent(MyCommentEvent.NavToComment) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.Comment,
                contentDescription = "Add Comment"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyCommentScreenPreview() {
    MaterialTheme {
        MyCommentScreen(
            uiState = MyCommentUiState()
        ) {}
    }
}