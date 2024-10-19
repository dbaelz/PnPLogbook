package de.dbaelz.pnp.logbook.features.experience

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dbaelz.pnp.logbook.features.experience.Experience
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModelContract.State

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExperienceScreen(
    viewModel: ExperienceViewModel = viewModel {
        ExperienceViewModel(
            ExperienceRepository()
        )
    },
    navigateBack: () -> Unit
) {
    // TODO: Dummy UI. Add proper UI later
    when (val state = viewModel.state.collectAsState().value) {
        is State.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize(0.25f))
            }
        }

        is State.Content -> {
            if (state.message != null) {
                Text(state.message)
            } else if (state.experienceEntries.isNotEmpty()) {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    stickyHeader {
                        Box(modifier = Modifier.height(24.dp)) {
                            Text("Total experience: ${state.total}")
                        }
                    }
                    items(state.experienceEntries) {
                        ExperienceItem(it)
                    }
                }
            } else {

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