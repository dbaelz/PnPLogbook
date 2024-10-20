package de.dbaelz.pnp.logbook.features.overview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dbaelz.pnp.logbook.navigation.Screen

@Composable
fun OverviewScreen(navigateTo: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OverviewCard(Screen.Logbook) { navigateTo(it) }

        OverviewCard(Screen.Experience) { navigateTo(it) }

        OverviewCard(Screen.Currency) { navigateTo(it) }

        OverviewCard(Screen.Persons) { navigateTo(it) }

        OverviewCard(Screen.Groups) { navigateTo(it) }

        OverviewCard(Screen.Places) { navigateTo(it) }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun OverviewCard(screen: Screen, navigateTo: (Screen) -> Unit) {
    Card(
        modifier = Modifier
            .height(96.dp)
            .widthIn(0.dp, 512.dp)
            .clickable { navigateTo(screen) },
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = screen.title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}