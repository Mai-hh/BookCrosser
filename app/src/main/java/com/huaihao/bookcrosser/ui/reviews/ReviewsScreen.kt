package com.huaihao.bookcrosser.ui.reviews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.model.Review

@Composable
fun ReviewsScreen(uiState: ReviewsUiState, onEvent: (ReviewsEvent) -> Unit) {
    LazyColumn {
        items(count = uiState.reviews.size) { index: Int ->
            ReviewCard(review = uiState.reviews[index])
        }
    }
}

@Composable
fun ReviewCard(modifier: Modifier = Modifier, review: Review) {
    Card(modifier = modifier) {
        ConstraintLayout {
            val (title, content, preview) = createRefs()
            Text(text = "Title", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start, margin = 8.dp)
            })
            Text(text = stringResource(id = R.string.content_test),
                modifier = Modifier
                    .constrainAs(content) {
                        top.linkTo(title.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
                    .padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun ReviewCardPreview() {
    MaterialTheme {
        ReviewCard(modifier = Modifier, Review(title = "TestTile", content = ""))
    }
}

@Preview
@Composable
fun ReviewsScreenPreview() {
    MaterialTheme {
        ReviewsScreen(uiState = ReviewsUiState()) {
            
        }
    }
}