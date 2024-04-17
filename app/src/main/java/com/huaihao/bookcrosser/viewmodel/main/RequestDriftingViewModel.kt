package com.huaihao.bookcrosser.viewmodel.main

import com.huaihao.bookcrosser.repo.DriftingRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel

sealed interface RequestDriftingEvent {
    data class CoverUrlChange(val coverUrl: String) : RequestDriftingEvent
    data class TitleChange(val title: String) : RequestDriftingEvent
    data class AuthorChange(val author: String) : RequestDriftingEvent
    data class IsbnChange(val isbn: String) : RequestDriftingEvent
    data class RequirementsChange(val description: String) : RequestDriftingEvent
    data object RequestDrifting : RequestDriftingEvent
    data object NavBack : RequestDriftingEvent
}

data class RequestDriftingUiState(
    var title: String = "",
    var author: String = "",
    var isbn: String = "",
    var requirements: String = "",
    var isLoading: Boolean = false
)

class RequestDriftingViewModel(driftingRepo: DriftingRepo) : BaseViewModel<RequestDriftingUiState, RequestDriftingEvent>() {
    override fun onEvent(event: RequestDriftingEvent) {
        TODO("Not yet implemented")
    }

    override fun defaultState(): RequestDriftingUiState = RequestDriftingUiState()
}