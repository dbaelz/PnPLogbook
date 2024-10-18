package de.dbaelz.pnp.logbook.features.experience

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dbaelz.pnp.logbook.experience.Experience
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModelContract.State

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExperienceScreen(
    viewModel: ExperienceViewModel = viewModel {
        ExperienceViewModel(
            ExperienceRepository()
        )
    }
) {
    val state = viewModel.state.collectAsState().value

    // TODO: Dummy UI. Add proper UI later
    when (state) {
        is State.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize()
            )
        }

        is State.Content -> {
            LazyColumn {
                stickyHeader {
                    Text("Total experience: ${state.total}")
                }
                items(state.experienceEntries) {
                    ExperienceItem(it)
                }
            }
        }
    }
}

@Composable
private fun ExperienceItem(experience: Experience) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(experience.id.toString())

        Text(experience.experience.toString())

        Text(experience.reason)
    }
}