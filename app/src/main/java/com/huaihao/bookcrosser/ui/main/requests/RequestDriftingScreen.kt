package com.huaihao.bookcrosser.ui.main.requests

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingEvent
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingUiState


@Composable
fun RequestDriftingScreen(
    uiState: RequestDriftingUiState,
    onEvent: (RequestDriftingEvent) -> Unit
) {
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
                val (cover) = createRefs()
                OutlinedCard(modifier = Modifier
                    .constrainAs(cover) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.mipmap.bc_logo_foreground),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
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
                            onValueChange = { onEvent(RequestDriftingEvent.TitleChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            label = "作者",
                            value = uiState.author,
                            onValueChange = { onEvent(RequestDriftingEvent.AuthorChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            label = "ISBN",
                            value = uiState.isbn,
                            onValueChange = { onEvent(RequestDriftingEvent.IsbnChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        LimitedOutlinedTextField(
                            label = "备注（可选）",
                            maxLength = 100,
                            maxLines = 4,
                            value = uiState.requirements,
                            onValueChange = { onEvent(RequestDriftingEvent.RequirementsChange(it)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Button(onClick = {
                    onEvent(RequestDriftingEvent.RequestDrifting)
                }, Modifier.fillMaxWidth()) {
                    Text(text = "请求图书")
                }
            }


        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRequestsScreen() {
    BookCrosserTheme {
        RequestDriftingScreen(uiState = RequestDriftingUiState(), onEvent = {})
    }
}