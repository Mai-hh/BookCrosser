package com.huaihao.bookcrosser.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.huaihao.bookcrosser.model.RequestBody
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.BookRepo
import com.huaihao.bookcrosser.service.ILocationService
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.util.AuthUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface ShelfABookEvent {
    data class CoverUrlChange(val coverUrl: String) : ShelfABookEvent
    data class TitleChange(val title: String) : ShelfABookEvent
    data class AuthorChange(val author: String) : ShelfABookEvent
    data class IsbnChange(val isbn: String) : ShelfABookEvent
    data object ShowUploadCoverDialog : ShelfABookEvent
    data object DismissUploadCoverDialog : ShelfABookEvent
    data class DescriptionChange(val description: String) : ShelfABookEvent
    data object ShelfBook : ShelfABookEvent
    data object NavBack : ShelfABookEvent

    data object GetCurrentLocation : ShelfABookEvent

    data object ShowScanning : ShelfABookEvent

    data class DoneScanning(val isbn: String) : ShelfABookEvent

    data object CloseScanning : ShelfABookEvent

}

data class ShelfABookUiState(
    val coverUrl: String = "",
    var title: String = "",
    var author: String = "",
    var isbn: String = "",
    var description: String = "",
    var isLoading: Boolean = false,
    var location: LatLng? = null,
    val showUploadDialog: Boolean = false,
    val isbnError: String? = null,
    val showScanningBarcode: Boolean = false
)

class ShelfABookViewModel(
    private val bookRepo: BookRepo,
    private val locationService: ILocationService
) :
    BaseViewModel<ShelfABookUiState, ShelfABookEvent>() {
    override fun onEvent(event: ShelfABookEvent) {
        when (event) {
            is ShelfABookEvent.CoverUrlChange -> onCoverUrlChange(coverUrl = event.coverUrl)
            is ShelfABookEvent.TitleChange -> onTitleChange(title = event.title)
            is ShelfABookEvent.AuthorChange -> onAuthorChange(author = event.author)
            is ShelfABookEvent.IsbnChange -> onIsbnChange(isbn = event.isbn)
            is ShelfABookEvent.DescriptionChange -> onDescriptionChange(description = event.description)
            ShelfABookEvent.ShelfBook -> onShelfBook()
            ShelfABookEvent.NavBack -> sendEvent(UiEvent.NavBack)

            is ShelfABookEvent.GetCurrentLocation -> {
                viewModelScope.launch(Dispatchers.IO) {
                    locationService.requestCurrentLocation().collect { location ->
                        if (location != null) {
                            val currentLocation =
                                LatLng(location.latitude, location.longitude)
                            state = state.copy(
                                location = currentLocation
                            )
                            sendEvent(UiEvent.SnackbarToast("获取位置成功"))
                        } else {
                            sendEvent(UiEvent.SnackbarToast("位置获取异常，请确认是否开启定位权限"))
                        }
                    }
                }
            }

            ShelfABookEvent.DismissUploadCoverDialog -> {
                dismissUploadCoverDialog()
            }
            ShelfABookEvent.ShowUploadCoverDialog -> {
                showUploadCoverDialog()
            }

            ShelfABookEvent.ShowScanning -> {
                state = state.copy(showScanningBarcode = true)
            }

            is ShelfABookEvent.DoneScanning -> {
                state = state.copy(
                    isbn = event.isbn,
                    showScanningBarcode = false
                )
            }

            ShelfABookEvent.CloseScanning -> {
                state = state.copy(showScanningBarcode = false)
            }
        }
    }

    private fun showUploadCoverDialog() {
        state = state.copy(showUploadDialog = true)
    }

    private fun dismissUploadCoverDialog() {
        state = state.copy(showUploadDialog = false)
    }

    override fun defaultState(): ShelfABookUiState = ShelfABookUiState()

    private fun onCoverUrlChange(coverUrl: String) {
        state = state.copy(
            coverUrl = coverUrl,
        )
    }

    private fun onTitleChange(title: String) {
        state = state.copy(
            title = title,
        )
    }

    private fun onAuthorChange(author: String) {
        state = state.copy(
            author = author,
        )
    }

    private fun onIsbnChange(isbn: String) {
        state = state.copy(
            isbn = isbn,
            isbnError = AuthUtil.validateISBN(isbn)
        )
    }

    private fun onDescriptionChange(description: String) {
        state = state.copy(
            description = description
        )
    }

    private fun onShelfBook() {
        sendEvent(UiEvent.ClearFocus)

        if (state.location == null) {
            sendEvent(UiEvent.SnackbarToast("未获取位置信息，请重试"))
            return
        }

        val book = RequestBody.Book(
            coverUrl = state.coverUrl,
            title = state.title,
            author = state.author,
            isbn = state.isbn,
            description = state.description,
            latitude = state.location!!.latitude,
            longitude = state.location!!.longitude
        )

        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.shelfABook(book).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(isLoading = false)
                        sendEvent(UiEvent.SnackbarToast("上架成功"))
                        sendEvent(UiEvent.NavBack)
                    }

                    is ApiResult.Error -> {
                        state = state.copy(isLoading = false)
                        sendEvent(UiEvent.SnackbarToast("上架失败\n ${result.errorMessage}"))
                    }

                    is ApiResult.Loading -> {
                        state = state.copy(isLoading = true)
                    }
                }
            }
        }
    }

}