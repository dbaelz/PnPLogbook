package de.dbaelz.pnp.logbook.features.currency

import androidx.lifecycle.viewModelScope
import de.dbaelz.pnp.logbook.features.currency.CurrencyViewModel.Internal
import de.dbaelz.pnp.logbook.features.currency.CurrencyViewModelContract.Event
import de.dbaelz.pnp.logbook.features.currency.CurrencyViewModelContract.State
import de.dbaelz.pnp.logbook.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CurrencyViewModel(
    private val currencyRepository: CurrencyRepository
) : BaseViewModel<State, Event, Internal.Event>() {
    override val initialState: State
        get() = State.Loading

    init {
        viewModelScope.launch {
            event.map { reduce(state.value, it) }.collect { updateState(it) }
        }

        viewModelScope.launch {
            getCurrency()
        }
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Internal.Event -> reduceInternal(state, event)

            is Event.AddCurrency -> {
                addCurrency(event.coins, event.reason)
                if (state is State.Content) {
                    state.copy(
                        isLoading = true
                    )
                } else {
                    State.Content(
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun reduceInternal(state: State, event: Internal.Event): State {
        return when (event) {
            is Internal.Event.UpdateCurrency -> {
                State.Content(
                    isLoading = false,
                    total = event.currency.total,
                    currencyEntries = event.currency.entries
                )
            }

            is Internal.Event.ShowMessage -> {
                State.Content(
                    isLoading = false,
                    message = event.message
                )
            }
        }
    }

    private fun getCurrency() {
        viewModelScope.launch {
            try {
                val currency = currencyRepository.getCurrency()
                sendEvent(Internal.Event.UpdateCurrency(currency))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error loading currency: ${exception.message}"))
            }
        }
    }

    private fun addCurrency(coins: Coins, reason: String) {
        viewModelScope.launch {
            try {
                val updatedCurrency = currencyRepository.addCurrency(coins, reason)
                sendEvent(Internal.Event.UpdateCurrency(updatedCurrency))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error adding currency: ${exception.message}"))
            }
        }
    }

    object Internal {
        sealed interface Event : CurrencyViewModelContract.Event {
            data class UpdateCurrency(val currency: CurrencyDTO) : Event

            data class ShowMessage(val message: String) : Event
        }
    }
}