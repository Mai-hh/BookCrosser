package com.huaihao.bookcrosser.ui.main.reviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.model.Review
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.viewmodel.main.ReviewSquareEvent
import com.huaihao.bookcrosser.viewmodel.main.ReviewSquareUiState

@Composable
fun ReviewSquareScreen(uiState: ReviewSquareUiState, onEvent: (ReviewSquareEvent) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(uiState.reviews) { review ->
            ReviewCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp), review = review
            )
        }
    }
}


@Composable
fun ReviewCard(modifier: Modifier = Modifier, review: Review) {
    ElevatedCard(
        modifier = modifier.aspectRatio(
            6f / 7f
        )
    ) {
        BoxWithConstraints {
            val cardHeight = maxHeight
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (content, preview, actions) = createRefs()
                Image(
                    painter = painterResource(id = R.mipmap.bc_logo_foreground),
                    contentDescription = review.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.constrainAs(preview) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.value(cardHeight / 2)
                    }
                )

                Column(
                    modifier = Modifier
                        .constrainAs(content) {
                            top.linkTo(preview.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.content_test),
                    )
                }

                Row(modifier = Modifier.constrainAs(actions) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }) {
                    OutlinedButton(onClick = {  }) {
                        Text(text = "请求此书")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = {  }, ) {
                        Icon(Icons.Rounded.StarBorder, contentDescription = "收藏")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewSquareScreenPre() {
    BookCrosserTheme {
        ReviewSquareScreen(
            ReviewSquareUiState()
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewCardPreview() {
    BookCrosserTheme {
        ReviewCard(modifier = Modifier, Review(title = "TestTile", content = ""))
    }
}