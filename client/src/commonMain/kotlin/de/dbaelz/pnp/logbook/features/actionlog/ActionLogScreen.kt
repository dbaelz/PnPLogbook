package de.dbaelz.pnp.logbook.features.actionlog

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.dbaelz.pnp.logbook.Platform

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActionLogScreen(actionLogItems: List<ActionLogItem>) {
    LazyColumn(contentPadding = PaddingValues(horizontal = 4.dp)) {
        stickyHeader {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(4.dp)
                    .height(64.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                countSortedByPlatform(actionLogItems).forEach { (platform, count) ->
                    Text("${platform.text}: $count")
                }
            }
        }
        items(actionLogItems) {
            ActionLogItem(it)
        }
    }
}

private fun countSortedByPlatform(actionLogItems: List<ActionLogItem>): List<Pair<Platform, Int>> {
    val map = mutableMapOf<Platform, Int>()

    actionLogItems.forEach {
        map[it.platform] = map.getOrElse(it.platform) { 0 } + 1
    }
    return map.toList().sortedByDescending { it.first.ordinal }
}

@Composable
private fun ActionLogItem(actionLogItem: ActionLogItem) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = actionLogItem.platform.text)

            Text(text = actionLogItem.httpMethod)

            Text(text = actionLogItem.path)
        }
    }
}