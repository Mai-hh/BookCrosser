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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PinDrop
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.huaihao.bookcrosser.model.DriftingRequest
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingEvent
import com.huaihao.bookcrosser.viewmodel.main.RequestDriftingUiState


@Composable
fun RequestDriftingScreen(
    uiState: RequestDriftingUiState,
    onEvent: (RequestDriftingEvent) -> Unit
) {
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

                    },
                    onDriftSelected = {
                        onEvent(RequestDriftingEvent.Drift(request))
                    },
                    onRejectSelected = {
                        onEvent(RequestDriftingEvent.RejectDriftingRequest(request))
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
    ElevatedCard(
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
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
            ) {
                Text(
                    text = request.book.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Ellipsis
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
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Column {
                    request.requester.username?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    request.requester.email?.let { Text(text = it) }
                }
            }

            Row(modifier = Modifier
                .constrainAs(actions) {
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

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { onRejectSelected() }
                ) {
                    Text(text = "拒绝")
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