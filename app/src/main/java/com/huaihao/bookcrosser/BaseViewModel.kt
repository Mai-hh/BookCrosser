package com.huaihao.bookcrosser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, ScreenEvent> : ViewModel() {

    var state by mutableStateOf(this.defaultState())
        protected set

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEvents = _uiEventChannel.receiveAsFlow()

    abstract fun onEvent(event: ScreenEvent)
    protected abstract fun defaultState(): State

    protected fun sendEvent(event: UiEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiEventChannel.send(event)
        }
    }
}