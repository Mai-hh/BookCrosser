package com.huaihao.bookcrosser.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.ProfileEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    BasicAlertDialog(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBookDialog(
    onDismiss: () -> Unit = {},
    onConfirm: (ProfileEvent) -> Unit = {},
    isUpdating: Boolean = false,
    book: Book
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var description by remember { mutableStateOf(book.description) }

    BasicAlertDialog(onDismissRequest = { }) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(Icons.Rounded.EditNote, contentDescription = "修改")

                LimitedOutlinedTextField(
                    label = "书名",
                    value = book.title,
                    onValueChange = {
                        title = it
                    }, modifier = Modifier
                )
                LimitedOutlinedTextField(
                    label = "作者",
                    value = book.author,
                    onValueChange = {
                        author = it
                    }, modifier = Modifier
                )
                LimitedOutlinedTextField(
                    label = "简介",
                    value = book.description,
                    onValueChange = {
                        description = it
                    }, modifier = Modifier
                )

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = {
                            onConfirm(
                                ProfileEvent.UpdateBook(
                                    bookId = book.id,
                                    title = title,
                                    author = author,
                                    description = description
                                )
                            )
                            onDismiss()
                        },
                    ) {
                        Text("确定")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onDismiss() }, enabled = !isUpdating) {
                        Text("取消")
                    }
                }
            }
        }
    }
}

@Composable
fun CommonAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    isCancellable: Boolean = true
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            OutlinedButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("确认")
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }, enabled = isCancellable) {
                Text("取消")
            }
        }
    )
}

@Preview
@Composable
fun CommonAlertDialogPreview() {
    BookCrosserTheme {
        CommonAlertDialog(
            onDismissRequest = {},
            onConfirmation = {},
            dialogTitle = "Title",
            dialogText = "Text",
            icon = Icons.Rounded.EditNote
        )
    }
}

@Preview
@Composable
fun LogoutAlertPreview() {
    BookCrosserTheme {
        LogoutAlert({}, {})
    }
}

@Preview
@Composable
fun UpdateBookDialogPreview() {
    BookCrosserTheme {
        UpdateBookDialog(
            book = Book(
                id = 1,
                ownerId = 1,
                "ownerUsername",
                uploaderId = 1,
                0.0,
                0.0,
                "status",
                "title",
                "author",
                "isbn",
                "coverUrl",
                "description",
                "createdAt",
                "updatedAt"
            )
        )
    }
}