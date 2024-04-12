package com.huaihao.bookcrosser.viewmodel

import com.huaihao.bookcrosser.model.Review
import com.huaihao.bookcrosser.ui.common.BaseViewModel

data class ReviewsUiState(
    val reviews: List<Review> = emptyList()
)

sealed interface ReviewsEvent

class ReviewsViewModel : BaseViewModel<ReviewsUiState, ReviewsEvent>() {
    override fun onEvent(event: ReviewsEvent) {
        when (event) {

            else -> {}
        }
    }

    override fun defaultState(): ReviewsUiState = ReviewsUiState()

}