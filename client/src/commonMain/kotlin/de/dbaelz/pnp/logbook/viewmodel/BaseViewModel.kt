package de.dbaelz.pnp.logbook.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<State, Event, InternalEvent: Event> : ViewModel() {
    protected abstract val initialState: State

    private val _state: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<State> = _state

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow(replay = Int.MAX_VALUE)
    val event = _event.asSharedFlow()

    fun sendEvent(event: Event) {
        _event.tryEmit(event)
    }

    protected fun updateState(state: State) {
        _state.value = state
    }
}