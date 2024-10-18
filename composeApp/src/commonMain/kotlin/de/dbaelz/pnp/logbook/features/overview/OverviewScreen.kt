package de.dbaelz.pnp.logbook.features.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun OverviewScreen(onNavigateToExperience: () -> Unit) {
    // TODO: Add proper UI
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Overview")

        Button(
            onClick = onNavigateToExperience
        ) {
            Text("Experience")
        }
    }
}