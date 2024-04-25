package com.huaihao.bookcrosser.viewmodel.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.BookStatus
import com.huaihao.bookcrosser.model.CommentDTO
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.BookRepo
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

    data class ShowRequestDialog(val book: Book) : CommentSquareEvent

    data object DismissRequestDialog : CommentSquareEvent

    data object Request : CommentSquareEvent
}

data class CommentSquareUiState(
    val showRequestDialog: Boolean = false,
    val comments: List<CommentDTO> = emptyList(),
    val selectedBook: Book? = null
)

class CommentSquareViewModel(private val commentRepo: CommentRepo, private val bookRepo: BookRepo) :
    BaseViewModel<CommentSquareUiState, CommentSquareEvent>() {
    override fun onEvent(event: CommentSquareEvent) {
        when (event) {
            is CommentSquareEvent.LoadAllComments -> {
                loadAllComments()
            }

            CommentSquareEvent.NavToComment -> {
                sendEvent(UiEvent.Navigate("${PROFILE_ROUTE}/${MY_BORROWED_BOOKS_ROUTE}"))
            }

            is CommentSquareEvent.ShowRequestDialog -> {
                state = state.copy(selectedBook = event.book)
                showRequestDialog()
            }

            CommentSquareEvent.DismissRequestDialog -> {
                dismissRequestDialog()
            }

            CommentSquareEvent.Request -> {
                onRequestBook()
            }
        }
    }

    private fun onRequestBook() {
        state.selectedBook?.let { book ->
            viewModelScope.launch(Dispatchers.IO) {
                bookRepo.requestABook(book.id).collect { result ->
                    when (result) {
                        is ApiResult.Success<*> -> {
                            sendEvent(UiEvent.SnackbarToast("请求成功"))
                            loadAllComments()
                            dismissRequestDialog()
                            state = state.copy(
                                comments = state.comments.map { oldComment ->
                                    if (oldComment.book.id == book.id) {
                                        oldComment.copy(
                                                book = oldComment.book.copy(
                                                    status = BookStatus.REQUESTED.statusString
                                                )
                                            )
                                    } else {
                                        oldComment
                                    }
                                }
                            )
                        }

                        is ApiResult.Error -> {
                            sendEvent(UiEvent.SnackbarToast("请求失败\n原因: ${result.errorMessage}"))
                            dismissRequestDialog()
                            Log.e(TAG, "onRequestBook: ${result.errorMessage}")
                        }

                        is ApiResult.Loading -> {
                            Log.d(TAG, "onRequestBook: loading")
                        }
                    }
                }
            }
        }
    }

    private fun showRequestDialog() {
        state = state.copy(showRequestDialog = true)
    }

    private fun dismissRequestDialog() {
        state = state.copy(showRequestDialog = false, selectedBook = null)
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