package com.huaihao.bookcrosser.ui.main.reviews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.model.Review
import com.huaihao.bookcrosser.viewmodel.main.MyReviewEvent
import com.huaihao.bookcrosser.viewmodel.main.MyReviewUiState

@Composable
fun MyReviewScreen(uiState: MyReviewUiState, onEvent: (MyReviewEvent) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(count = uiState.reviews.size) { index: Int ->
            ReviewCard(
                review = uiState.reviews[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ReviewsScreenPreview() {
    MaterialTheme {
        MyReviewScreen(
            uiState = MyReviewUiState(
                listOf(
                    Review(title = "A1", content = stringResource(id = R.string.content_test)),
                    Review(title = "A1", content = stringResource(id = R.string.content_test))
                )
            )
        ) {}
    }
}