package de.dbaelz.pnp.logbook

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.dbaelz.pnp.logbook.features.currency.CurrencyScreen
import de.dbaelz.pnp.logbook.features.experience.ExperienceScreen
import de.dbaelz.pnp.logbook.features.logbook.LogbookScreen
import de.dbaelz.pnp.logbook.features.overview.OverviewScreen
import de.dbaelz.pnp.logbook.navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController: NavHostController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = Screen.valueOf(
            backStackEntry?.destination?.route ?: Screen.Overview.name
        )

        // TODO: Improve UI
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }

                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Overview.name,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = Screen.Overview.name) {
                    OverviewScreen(
                        navigateTo = {
                            navController.navigate(it.name)
                        }
                    )
                }

                composable(route = Screen.Logbook.name) {
                    LogbookScreen()
                }

                composable(route = Screen.Experience.name) {
                    ExperienceScreen()
                }

                composable(route = Screen.Currency.name) {
                    CurrencyScreen()
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    TopAppBar(
        title = { Text(currentScreen.title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = Icons.AutoMirrored.Filled.ArrowBack.name
                    )
                }
            }
        }
    )
}