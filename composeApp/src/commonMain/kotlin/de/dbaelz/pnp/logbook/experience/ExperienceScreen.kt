package de.dbaelz.pnp.logbook.experience

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.dbaelz.pnp.logbook.experience.ExperienceViewModelContract.State
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExperienceScreen(state: State) {
    // TODO: Dummy UI. Add proper UI later
    when (state) {
        is State.Loading -> {
            CircularProgressIndicator()
        }

        is State.Content -> {
            LazyColumn {
                items(state.experience) {
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