package de.dbaelz.pnp.logbook.currency

class CurrencyRepository {
    private val currency = mutableListOf<Currency>()

    fun getAmountAndItems(): Pair<Coins, List<Currency>> {
        // TODO: Calculate properly and convert values
        val coinsArray = currency.fold(arrayOf(0, 0, 0, 0, 0)) { coins, current ->
            coins[0] += current.coins.copper
            coins[1] += current.coins.silver
            coins[2] += current.coins.electrum
            coins[3] += current.coins.gold
            coins[4] += current.coins.platinum

            coins
        }

        val coins = Coins(
            copper = coinsArray[0],
            silver = coinsArray[1],
            electrum = coinsArray[2],
            gold = coinsArray[3],
            platinum = coinsArray[4]
        )

        return coins to currency
    }

    fun add(currency: Currency) {
        this.currency.add(currency)
    }
}