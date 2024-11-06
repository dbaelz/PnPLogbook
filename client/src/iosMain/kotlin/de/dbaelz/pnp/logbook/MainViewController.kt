package de.dbaelz.pnp.logbook

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController(): platform.UIKit.UIViewController {
    appInit()

    return ComposeUIViewController { App() }
}