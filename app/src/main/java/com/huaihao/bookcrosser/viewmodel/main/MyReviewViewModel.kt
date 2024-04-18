package com.huaihao.bookcrosser.viewmodel.main

import com.huaihao.bookcrosser.model.Review
import com.huaihao.bookcrosser.repo.ReviewRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel

sealed interface MyReviewEvent {

}

data class MyReviewUiState(
    val reviews: List<Review> = listOf(
        Review(
            title = "这是一个留言",
            content = "这是对这本书的评论这是对这本书的评论这是对这本书的评论"
        ),
        Review(
            title = "这是一个留言",
            content = "这是对这本书的评论这是对这本书的评论这是对这本书的评论"
        ),
        Review(
            title = "这是一个留言",
            content = "这是对这本书的评论这是对这本书的评论这是对这本书的评论"
        )
    )
)


class MyReviewViewModel(private val reviewRepo: ReviewRepo) : BaseViewModel<MyReviewUiState, MyReviewEvent>() {
    override fun onEvent(event: MyReviewEvent) {

    }

    override fun defaultState(): MyReviewUiState = MyReviewUiState()
}