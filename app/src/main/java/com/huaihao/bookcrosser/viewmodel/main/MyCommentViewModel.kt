package com.huaihao.bookcrosser.viewmodel.main


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.CommentDTO
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.CommentRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

sealed interface MyCommentEvent {

    data object LoadComments : MyCommentEvent

    data class ShowUpdateDialog(val selectedComment: CommentDTO) : MyCommentEvent

    data object DismissUpdateDialog : MyCommentEvent

    data class UpdateComment(val commentId: Long, val content: String) : MyCommentEvent

    data class ShowDeleteDialog(val selectedComment: CommentDTO) : MyCommentEvent

    data object DismissDeleteDialog : MyCommentEvent

    data class DeleteComment(val commentId: Long) : MyCommentEvent

}

data class MyCommentUiState(
    val comments: List<CommentDTO> = emptyList(),
    val selectedComment: CommentDTO? = null,
    val showUpdateDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isUpdating: Boolean = false
)


class MyCommentViewModel(private val commentRepo: CommentRepo) :
    BaseViewModel<MyCommentUiState, MyCommentEvent>() {
    override fun onEvent(event: MyCommentEvent) {
        when (event) {
            is MyCommentEvent.LoadComments -> {
                loadComments()
            }

            is MyCommentEvent.UpdateComment -> {
                updateComment(event.commentId, event.content)
            }

            MyCommentEvent.DismissUpdateDialog -> {
                dismissUpdateDialog()
            }

            is MyCommentEvent.ShowUpdateDialog -> {
                state = state.copy(selectedComment = event.selectedComment)
                showUpdateDialog()
            }

            is MyCommentEvent.DeleteComment -> {
                deleteComment(event.commentId)
            }

            MyCommentEvent.DismissDeleteDialog -> {
                dismissDeleteDialog()
            }

            is MyCommentEvent.ShowDeleteDialog -> {
                state = state.copy(selectedComment = event.selectedComment)
                showDeleteDialog()
            }
        }

    }

    private fun deleteComment(commentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            commentRepo.deleteComment(commentId).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        sendEvent(UiEvent.SnackbarToast("评论删除成功"))
                        loadComments()
                        dismissDeleteDialog()
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("评论删除失败"))
                        dismissDeleteDialog()
                    }

                    is ApiResult.Loading -> {
                        Log.d(TAG, "deleteComment: loading")
                        state = state.copy(isUpdating = true)
                    }
                }
            }
        }
    }

    private fun dismissDeleteDialog() {
        state = state.copy(showDeleteDialog = false)
    }

    private fun showDeleteDialog() {
        state = state.copy(showDeleteDialog = true)
    }
    private fun showUpdateDialog() {
        state = state.copy(showUpdateDialog = true)
    }

    private fun dismissUpdateDialog() {
        state = state.copy(showUpdateDialog = false)
    }

    private fun updateComment(commentId: Long, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            commentRepo.updateComment(commentId, content).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        sendEvent(UiEvent.SnackbarToast("评论更新成功"))
                        loadComments()
                        dismissUpdateDialog()
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("评论更新失败"))
                        dismissUpdateDialog()
                    }

                    is ApiResult.Loading -> {
                        Log.d(TAG, "updateComment: loading")
                        state = state.copy(isUpdating = true)
                    }
                }
            }
        }
    }

    private fun loadComments() {
        viewModelScope.launch(Dispatchers.IO) {
            commentRepo.loadMyComments().collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        val comments = (result.data as List<CommentDTO>)
                        state = state.copy(comments = comments)
                        Log.d(TAG, "loadAMyComments: comments: $comments")

                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("我的评论加载失败"))
                    }

                    is ApiResult.Loading -> {
                        Log.d(TAG, "loadComments: loading")
                    }
                }
            }
        }
    }

    override fun defaultState(): MyCommentUiState = MyCommentUiState()

    companion object {
        private const val TAG = "MyCommentViewModel"
    }
}