package de.dbaelz.pnp.logbook

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    appInit()

    ComposeViewport(document.body!!) {
        App()
    }
}