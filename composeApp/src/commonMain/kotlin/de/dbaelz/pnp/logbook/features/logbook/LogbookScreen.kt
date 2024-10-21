package de.dbaelz.pnp.logbook.features.logbook

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dbaelz.pnp.logbook.features.logbook.LogbookViewModelContract.Event
import de.dbaelz.pnp.logbook.features.logbook.LogbookViewModelContract.State
import de.dbaelz.pnp.logbook.network.httpClient
import de.dbaelz.pnp.logbook.ui.Loading
import de.dbaelz.pnp.logbook.ui.localDateTimeFormat
import kotlinx.datetime.format

@Composable
fun LogbookScreen(
    viewModel: LogbookViewModel = viewModel {
        LogbookViewModel(LogbookRepository(httpClient))
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
    viewModel: LogbookViewModel
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        if (state.message != null) {
            Text(state.message)
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(state.logbookEntries) {
                    LogbookItem(it) { id ->
                        viewModel.sendEvent(
                            Event.DeleteLogbook(id)
                        )
                    }
                }
            }
        }

        AddLogbookEntry { location, text ->
            viewModel.sendEvent(
                Event.AddLogbook(
                    location = location,
                    text = text
                )
            )
        }
    }
}

@Composable
private fun LogbookItem(
    logbookEntry: LogbookEntry,
    onDeleteClicked: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    textAlign = TextAlign.End,
                    text = logbookEntry.date.format(localDateTimeFormat)
                )

                Text(
                    textAlign = TextAlign.End,
                    text = "• ${logbookEntry.id}"
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = Icons.Default.Delete.name,
                    modifier = Modifier
                        .clickable { onDeleteClicked(logbookEntry.id) }
                )
            }

            Text(
                textAlign = TextAlign.End,
                text = logbookEntry.location
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = logbookEntry.text
            )
        }
    }
}

@Composable
private fun AddLogbookEntry(addLogbook: (String, String) -> Unit) {
    var location: String by remember { mutableStateOf("") }
    var text: String by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Text") },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                if (location.isNotEmpty() && text.isNotEmpty()) {
                    addLogbook(location, text)
                }
            },
            enabled = location.isNotEmpty() && text.isNotEmpty()
        ) {
            Text("Add")
        }
    }
}