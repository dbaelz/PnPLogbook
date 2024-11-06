package de.dbaelz.pnp.logbook.features.subject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.logbook.LogbookViewModel
import de.dbaelz.pnp.logbook.features.subject.SubjectViewModelContract.Event
import de.dbaelz.pnp.logbook.features.subject.SubjectViewModelContract.State
import de.dbaelz.pnp.logbook.ui.Loading
import io.ktor.client.*
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun PersonsScreen() {
    val viewModel: SubjectViewModel = koinViewModel(qualifier = named(ApiRoute.PERSONS.resource))

    SubjectScreen(viewModel)
}

@Composable
fun GroupsScreen() {
    val viewModel: SubjectViewModel = koinViewModel(qualifier = named(ApiRoute.GROUPS.resource))

    SubjectScreen(viewModel)
}

@Composable
fun PlacesScreen() {
    val viewModel: SubjectViewModel = koinViewModel(qualifier = named(ApiRoute.PLACES.resource))

    SubjectScreen(viewModel)
}

@Composable
fun SubjectScreen(viewModel: SubjectViewModel) {
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
    viewModel: SubjectViewModel
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        if (state.message != null) {
            Text(state.message)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.subjectEntries) {
                    SubjectItem(it)
                }
            }
        }

        AddSubject { name, description, notes ->
            viewModel.sendEvent(
                Event.AddSubject(
                    name = name,
                    description = description,
                    notes = notes
                )
            )
        }
    }
}

@Composable
private fun SubjectItem(subject: Subject) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                textAlign = TextAlign.End,
                text = subject.id.toString()
            )

            Text(
                textAlign = TextAlign.End,
                text = subject.name
            )

            Text(
                textAlign = TextAlign.End,
                text = subject.description
            )

            if (subject.notes.isNotEmpty()) {
                Text(
                    textAlign = TextAlign.End,
                    text = subject.notes
                )
            }
        }
    }
}

@Composable
private fun AddSubject(addSubject: (String, String, String) -> Unit) {
    var name: String by remember { mutableStateOf("") }
    var description: String by remember { mutableStateOf("") }
    var notes: String by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AddSubjectTextFields(
            name = name,
            description = description,
            notes = notes,
            onNameValueChanged = { name = it },
            onDescriptionValueChanged = { description = it },
            onNotesValueChanged = { notes = it }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                addSubject(name, description, notes)
            },
            modifier = Modifier.height(IntrinsicSize.Max),
            enabled = name.isNotEmpty() && description.isNotEmpty()
        ) {
            Text("Add")
        }
    }
}

@Composable
private fun RowScope.AddSubjectTextFields(
    name: String,
    description: String,
    notes: String,
    onNameValueChanged: (String) -> Unit,
    onDescriptionValueChanged: (String) -> Unit,
    onNotesValueChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { onNameValueChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Name") },
        )

        OutlinedTextField(
            value = description,
            onValueChange = { onDescriptionValueChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Description") },
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { onNotesValueChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Notes") },
        )
    }
}