package de.dbaelz.pnp.logbook.features.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.dbaelz.pnp.logbook.navigation.Screen

@Composable
fun OverviewScreen(navigateTo: (Screen) -> Unit) {
    // TODO: Add proper UI
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Overview")

        Button(
            onClick = { navigateTo(Screen.Experience) }
        ) {
            Text("Experience")
        }
    }
}