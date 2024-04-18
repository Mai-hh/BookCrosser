package com.huaihao.bookcrosser.viewmodel.main

import com.huaihao.bookcrosser.model.Review
import com.huaihao.bookcrosser.repo.ReviewRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel

sealed interface ReviewSquareEvent {

}

data class ReviewSquareUiState(
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

class ReviewSquareViewModel(private val reviewRepo: ReviewRepo) : BaseViewModel<ReviewSquareUiState, ReviewSquareEvent>() {
    override fun onEvent(event: ReviewSquareEvent) {
        TODO("Not yet implemented")
    }

    override fun defaultState(): ReviewSquareUiState = ReviewSquareUiState()

}