package de.dbaelz.pnp.logbook.features.currency

object CurrencyViewModelContract {
    sealed interface State {
        data object Loading : State

        data class Content(
            val isLoading: Boolean = false,
            val total: Coins = Coins(),
            val currencyEntries: List<Currency> = emptyList(),
            val message: String? = null
        ) : State
    }

    sealed interface Event {
        data class AddCurrency(
            val coins: Coins,
            val reason: String
        ) : Event
    }
}