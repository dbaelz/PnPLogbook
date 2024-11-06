package de.dbaelz.pnp.logbook.features.currency

import de.dbaelz.pnp.logbook.helper.executeQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface CurrencyRepository {
    suspend fun getAmountAndEntries(): Pair<Coins, List<Currency>>

    suspend fun add(currency: AddCurrency)
}

class CurrencyRepositoryImpl : CurrencyRepository {
    private object CurrencyTable : IntIdTable() {
        val date = datetime("date").defaultExpression(CurrentDateTime)
        val copper = integer("copper")
        val silver = integer("silver")
        val electrum = integer("electrum")
        val gold = integer("gold")
        val platinum = integer("platinum")
        val reason = varchar("reason", 255)
    }

    init {
        transaction {
            SchemaUtils.create(CurrencyTable)
        }
    }

    override suspend fun getAmountAndEntries(): Pair<Coins, List<Currency>> {
        val currencyEntries = executeQuery {
            CurrencyTable.selectAll()
                .map {
                    Currency(
                        id = it[CurrencyTable.id].value,
                        date = it[CurrencyTable.date],
                        coins = Coins(
                            copper = it[CurrencyTable.copper],
                            silver = it[CurrencyTable.silver],
                            electrum = it[CurrencyTable.electrum],
                            gold = it[CurrencyTable.gold],
                            platinum = it[CurrencyTable.platinum]
                        ),
                        reason = it[CurrencyTable.reason]
                    )
                }
        }

        val coinsArray = currencyEntries.fold(arrayOf(0, 0, 0, 0, 0)) { coins, current ->
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

        return coins to currencyEntries
    }

    override suspend fun add(currency: AddCurrency) {
        executeQuery {
            CurrencyTable.insert {
                it[copper] = currency.coins.copper
                it[silver] = currency.coins.silver
                it[electrum] = currency.coins.electrum
                it[gold] = currency.coins.gold
                it[platinum] = currency.coins.platinum
                it[reason] = currency.reason
            }
        }
    }
}