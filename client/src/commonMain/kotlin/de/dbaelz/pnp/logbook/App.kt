package de.dbaelz.pnp.logbook

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.dbaelz.pnp.logbook.app.AppViewModel
import de.dbaelz.pnp.logbook.app.AppViewModelContract.Event
import de.dbaelz.pnp.logbook.di.appModule
import de.dbaelz.pnp.logbook.features.actionlog.ActionLogScreen
import de.dbaelz.pnp.logbook.features.currency.CurrencyScreen
import de.dbaelz.pnp.logbook.features.experience.ExperienceScreen
import de.dbaelz.pnp.logbook.features.logbook.LogbookScreen
import de.dbaelz.pnp.logbook.features.overview.OverviewScreen
import de.dbaelz.pnp.logbook.features.subject.GroupsScreen
import de.dbaelz.pnp.logbook.features.subject.PersonsScreen
import de.dbaelz.pnp.logbook.features.subject.PlacesScreen
import de.dbaelz.pnp.logbook.navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinApplication(
        application = { modules(appModule) }
    ) {
        val viewModel: AppViewModel = koinViewModel()

        val state = viewModel.state.collectAsState()

        MaterialTheme {
            val navController: NavHostController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentScreen = Screen.valueOf(
                backStackEntry?.destination?.route ?: Screen.Overview.name
            )

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopBar(
                        currentScreen = currentScreen,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        actionLogCount = state.value.actionLogItems.size,
                        onActionLogCountClicked = {
                            if (currentScreen != Screen.ActionLog) {
                                navController.navigate(Screen.ActionLog.name)
                            }
                        },
                        onShutdownClicked = {
                            viewModel.sendEvent(Event.ShutdownServer)
                        }
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

                    composable(route = Screen.Persons.name) {
                        PersonsScreen()
                    }

                    composable(route = Screen.Groups.name) {
                        GroupsScreen()
                    }

                    composable(route = Screen.Places.name) {
                        PlacesScreen()
                    }

                    composable(route = Screen.ActionLog.name) {
                        ActionLogScreen(state.value.actionLogItems)
                    }
                }
            }
        }
    }

}

@Composable
private fun TopBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    actionLogCount: Int,
    onActionLogCountClicked: () -> Unit,
    onShutdownClicked: () -> Unit
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
        },
        actions = {
            Button(
                onClick = onActionLogCountClicked,
                shape = CircleShape,
                border = BorderStroke(2.dp, MaterialTheme.colors.onPrimary),
                elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Text(actionLogCount.toString())
            }

            IconButton(
                onClick = onShutdownClicked
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = Icons.Default.Warning.name,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    )
}