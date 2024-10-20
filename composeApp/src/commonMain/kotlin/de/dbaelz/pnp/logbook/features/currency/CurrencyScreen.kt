package de.dbaelz.pnp.logbook.features.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dbaelz.pnp.logbook.features.currency.CurrencyViewModelContract.Event
import de.dbaelz.pnp.logbook.features.currency.CurrencyViewModelContract.State
import de.dbaelz.pnp.logbook.ui.Loading
import de.dbaelz.pnp.logbook.ui.localDateTimeFormat
import kotlinx.datetime.format

@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel = viewModel {
        CurrencyViewModel(CurrencyRepository())
    }
) {
    when (val state = viewModel.state.collectAsState().value) {
        is State.Loading -> {
            Loading()
        }

        is State.Content -> {
            if (state.isLoading) {
                Loading()
            } else {
                Content(state, viewModel)
            }
        }
    }
}

@Composable
private fun Content(
    state: State.Content,
    viewModel: CurrencyViewModel
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        if (state.message != null) {
            Text(state.message)
        } else {
            Box(modifier = Modifier.height(32.dp)) {
                Text(
                    text = "Total: ${getCoinsText(state.total)}",
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.currencyEntries) {
                    CurrencyItem(it)
                }
            }
        }

        AddCurrency { coins, reason ->
            viewModel.sendEvent(
                Event.AddCurrency(
                    coins = coins,
                    reason = reason
                )
            )
        }
    }
}

@Composable
private fun CurrencyItem(currency: Currency) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.End,
            text = currency.id.toString()
        )

        Text(
            textAlign = TextAlign.End,
            text = currency.date.format(localDateTimeFormat)
        )

        Text(
            modifier = Modifier.width(256.dp),
            textAlign = TextAlign.End,
            text = getCoinsText(currency.coins)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = currency.reason
        )
    }
}

private fun getCoinsText(coins: Coins): String {
    return buildString {
        if (coins.copper != 0) append("${coins.copper}cp ")
        if (coins.silver != 0) append("${coins.silver}sp ")
        if (coins.electrum != 0) append("${coins.electrum}ep ")
        if (coins.gold != 0) append("${coins.gold}gp ")
        if (coins.platinum != 0) append("${coins.platinum}pp ")
    }.trim()
}

@Composable
private fun AddCurrency(addCurrency: (Coins, String) -> Unit) {
    val coins = remember { mutableStateListOf(0, 0, 0, 0, 0) }
    var reason: String by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AddCoinTextField(coins[0], { coins[0] = it }, "Copper")
        AddCoinTextField(coins[1], { coins[1] = it }, "Silver")
        AddCoinTextField(coins[2], { coins[2] = it }, "Electrum")
        AddCoinTextField(coins[3], { coins[3] = it }, "Gold")
        AddCoinTextField(coins[4], { coins[4] = it }, "Platinum")

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("Reason") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                addCurrency(
                    Coins(
                        copper = coins[0],
                        silver = coins[1],
                        electrum = coins[2],
                        gold = coins[3],
                        platinum = coins[4]
                    ),
                    reason
                )
            },
            enabled = reason.isNotEmpty()
        ) {
            Text("Add")
        }
    }
}

@Composable
private fun RowScope.AddCoinTextField(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = {
            it.toIntOrNull()?.let { value ->
                onValueChange(value)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = { Text(label) },
        modifier = Modifier.weight(1f)
    )
}