package com.huaihao.bookcrosser.viewmodel.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.CommentDTO
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.CommentRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.ui.main.Destinations.MY_BORROWED_BOOKS_ROUTE
import com.huaihao.bookcrosser.ui.main.Destinations.PROFILE_ROUTE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface CommentSquareEvent {
    data object LoadAllComments : CommentSquareEvent

    data object NavToComment : CommentSquareEvent
}

data class CommentSquareUiState(
    val comments: List<CommentDTO> = emptyList()
)

class CommentSquareViewModel(private val commentRepo: CommentRepo) :
    BaseViewModel<CommentSquareUiState, CommentSquareEvent>() {
    override fun onEvent(event: CommentSquareEvent) {
        when (event) {
            is CommentSquareEvent.LoadAllComments -> {
                loadAllComments()
            }

            CommentSquareEvent.NavToComment -> {
                sendEvent(UiEvent.Navigate("${PROFILE_ROUTE}/${MY_BORROWED_BOOKS_ROUTE}"))
            }
        }
    }

    private fun loadAllComments() {
        viewModelScope.launch(Dispatchers.IO) {
            commentRepo.loadAllComments().collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        val comments = (result.data as List<CommentDTO>)
                        state = state.copy(comments = comments)
                        Log.d(TAG, "loadAllComments: comments: $comments")
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("评论加载失败，请检查网络"))
                        Log.e(TAG, "loadAllComments: ${result.errorMessage}")
                    }

                    is ApiResult.Loading -> {
                        Log.d(TAG, "loadAllComments: loading")
                    }
                }

            }
        }

    }

    override fun defaultState(): CommentSquareUiState = CommentSquareUiState()

    companion object {
        private const val TAG = "ReviewSquareViewModel"
    }

}