package de.dbaelz.pnp.logbook.features.experience

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
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
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModelContract.Event
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModelContract.State
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun ExperienceScreen(
    viewModel: ExperienceViewModel = viewModel {
        ExperienceViewModel(
            ExperienceRepository()
        )
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
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.25f))
    }
}

@Composable
private fun Content(
    state: State.Content,
    viewModel: ExperienceViewModel
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        if (state.message != null) {
            Text(state.message)
        } else {
            Box(modifier = Modifier.height(32.dp)) {
                Text(
                    text = "Total experience: ${state.total}",
                    fontWeight = FontWeight.Bold
                )
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.experienceEntries) {
                    ExperienceItem(it)
                }
            }
        }

        AddExperience { experience, reason ->
            viewModel.sendEvent(
                Event.AddExperience(
                    experience = experience,
                    reason = reason
                )
            )
        }
    }
}

@Composable
private fun ExperienceItem(experience: Experience) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.End,
            text = experience.id.toString()
        )


        Text(
            textAlign = TextAlign.End,
            text = experience.date.format(dateFormat)
        )

        Text(
            modifier = Modifier.width(64.dp),
            textAlign = TextAlign.End,
            text = experience.experience.toString()
        )

        Text(
            modifier = Modifier.weight(1f),
            text = experience.reason
        )
    }
}

@Composable
private fun AddExperience(addExperience: (Int, String) -> Unit) {
    var experience: Int? by remember { mutableStateOf(null) }
    var reason: String by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = experience?.toString() ?: "",
            onValueChange = { experience = it.toIntOrNull() },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Experience") },
            modifier = Modifier.weight(1f)
        )

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
                experience?.let {
                    addExperience(it, reason)
                }
            },
            enabled = experience != null
        ) {
            Text("Add")
        }
    }
}

val dateFormat = LocalDateTime.Format {
    dayOfMonth()
    char('.')
    monthNumber()
    char('.')
    year()
}