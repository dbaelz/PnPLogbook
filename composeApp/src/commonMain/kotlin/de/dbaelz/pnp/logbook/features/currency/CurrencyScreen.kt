package de.dbaelz.pnp.logbook.features.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import de.dbaelz.pnp.logbook.network.httpClient
import de.dbaelz.pnp.logbook.ui.Loading
import de.dbaelz.pnp.logbook.ui.localDateTimeFormat
import kotlinx.datetime.format

@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel = viewModel {
        CurrencyViewModel(CurrencyRepository(httpClient))
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

        Spacer(Modifier.height(8.dp))

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
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .height(96.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    textAlign = TextAlign.End,
                    text = currency.id.toString()
                )

                Text(
                    textAlign = TextAlign.End,
                    text = currency.date.format(localDateTimeFormat)
                )
            }

            Text(text = getCoinsText(currency.coins))

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = currency.reason)
        }
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
private fun ColumnScope.AddCurrency(addCurrency: (Coins, String) -> Unit) {
    val coins = remember { mutableStateListOf(0, 0, 0, 0, 0) }
    var reason: String by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AddCoinTextField(coins[0], { coins[0] = it }, "Copper")
        AddCoinTextField(coins[1], { coins[1] = it }, "Silver")
        AddCoinTextField(coins[2], { coins[2] = it }, "Electrum")
        AddCoinTextField(coins[3], { coins[3] = it }, "Gold")
        AddCoinTextField(coins[4], { coins[4] = it }, "Platinum")

    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("Reason") },
            modifier = Modifier.weight(1f)
        )

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
            enabled = reason.isNotEmpty() && coins.sum() > 0
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