package com.suitebde.features.root

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.suitebde.R
import com.suitebde.features.auth.AuthView
import com.suitebde.features.chat.*
import com.suitebde.features.clubs.ClubView
import com.suitebde.features.clubs.ClubsView
import com.suitebde.features.events.EventView
import com.suitebde.features.feed.FeedView
import com.suitebde.features.notifications.SendNotificationView
import com.suitebde.features.scans.ScanHistoryView
import com.suitebde.features.settings.SettingsView
import com.suitebde.features.subscriptions.SubscriptionView
import com.suitebde.features.users.QRCodeView
import com.suitebde.features.users.UserView
import com.suitebde.ui.components.auth.AuthErrorView
import com.suitebde.viewmodels.root.RootViewModel
import dev.kaccelero.models.UUID
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun RootView() {

    val viewModel = koinViewModel<RootViewModel>()
    val oldViewModel: OldRootViewModel = viewModel()

    val user by viewModel.user.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUser()
    }

    Scaffold(
        bottomBar = {
            if (user == null) return@Scaffold
            NavigationBar {
                val currentRoute = navBackStackEntry?.destination?.route
                NavigationItem.entries.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(item.icon),
                                contentDescription = stringResource(item.title)
                            )
                        },
                        label = { Text(stringResource(item.title)) },
                        alwaysShowLabel = true,
                        selected = currentRoute?.startsWith(item.route) ?: false,
                        onClick = {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        if (loading) Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            CircularProgressIndicator()
        } else error?.let {
            AuthErrorView(
                error = it,
                tryAgainClicked = {
                    viewModel.viewModelScope.coroutineScope.launch {
                        viewModel.fetchUser()
                    }
                },
                modifier = Modifier.padding(padding)
            )
        } ?: user?.let {
            TabNavigation(
                viewModel = viewModel,
                oldViewModel = oldViewModel,
                navController = navController,
                padding = padding
            )
        } ?: AuthNavigation(
            navController = navController,
            padding = padding,
            onUserLogged = {
                viewModel.viewModelScope.coroutineScope.launch {
                    viewModel.fetchUser()
                }
            }
        )
    }
}

@Composable
@Suppress("FunctionName")
fun TabNavigation(
    viewModel: RootViewModel,
    oldViewModel: OldRootViewModel,
    navController: NavHostController,
    padding: PaddingValues,
) {

    LaunchedEffect(Unit) {
        viewModel.scannedUser.onEach {
            it?.let {
                navController.navigate("feed/users/${it.associationId}/${it.userId}")
            }
        }.launchIn(viewModel.viewModelScope.coroutineScope)
    }

    val user by viewModel.user.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "feed"
    ) {
        composable("feed") {
            FeedView(
                navigate = navController::navigate,
                rootViewModel = viewModel,
                modifier = Modifier.padding(padding),
            )
        }
        composable("feed/subscriptions/{subscriptionId}") {
            SubscriptionView(
                id = it.arguments?.getString("subscriptionId")?.let(::UUID)!!,
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding)
            )
        }
        composable("feed/events/{eventId}") {
            EventView(
                id = it.arguments?.getString("eventId")?.let(::UUID)!!,
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding)
            )
        }
        composable("feed/settings") {
            SettingsView(
                navigateUp = navController::navigateUp,
                rootViewModel = viewModel,
                modifier = Modifier.padding(padding)
            )
        }
        composable("feed/send_notification") {
            SendNotificationView(
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding)
            )
        }
        composable("feed/suggest_event") {
            EventView(
                id = null,
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding),
            )
        }
        composable("feed/users/{associationId}/{userId}") {
            UserView(
                associationId = it.arguments?.getString("associationId")?.let(::UUID)!!,
                userId = it.arguments?.getString("userId")?.let(::UUID)!!,
                navigate = navController::navigate,
                modifier = Modifier.padding(padding),
                navigateUp = navController::navigateUp
            )
        }
        composable("account") {
            UserView(
                associationId = user?.associationId!!,
                userId = user?.id!!,
                navigate = navController::navigate,
                modifier = Modifier.padding(padding),
            )
        }
        composable("account/qrcode") {
            QRCodeView(
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding)
            )
        }
        composable("feed/scan_history") {
            ScanHistoryView(
                navigate = navController::navigate,
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding),
            )
        }
        composable("clubs") {
            ClubsView(
                navigate = navController::navigate,
                modifier = Modifier.padding(padding),
            )
        }
        composable("clubs/{clubId}") {
            ClubView(
                id = it.arguments?.getString("clubId")?.let(::UUID)!!,
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding),
            )
        }
        composable("chat") {
            ChatView(
                modifier = Modifier.padding(padding),
                viewModel = ChatViewModel(
                    LocalContext.current.applicationContext as Application,
                    oldViewModel.getToken().value
                ),
                oldRootViewModel = oldViewModel
            )
        }
        composable("chat/conversation") {
            ConversationView(
                modifier = Modifier.padding(padding),
                viewModel = ConversationViewModel(
                    LocalContext.current.applicationContext as Application,
                    oldViewModel.getToken().value,
                    oldViewModel.getSelectedConversation().value!!
                ),
                oldRootViewModel = oldViewModel,
                navigate = navController::navigate,
                navigateUp = navController::navigateUp
            )
        }
        composable("chat/conversation/settings") {
            ConversationSettingsView(
                modifier = Modifier.padding(padding),
                viewModel = ConversationSettingsViewModel(
                    LocalContext.current.applicationContext as Application,
                    oldViewModel.getToken().value,
                    oldViewModel.getSelectedConversation().value!!
                ),
                oldRootViewModel = oldViewModel,
                navigateUp = navController::navigateUp
            )
        }
    }

}

@Composable
@Suppress("FunctionName")
fun AuthNavigation(
    navController: NavHostController,
    padding: PaddingValues,
    onUserLogged: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            AuthView(
                onUserLogged = onUserLogged,
                modifier = Modifier.padding(padding)
            )
        }
        composable(
            "auth/code",
            arguments = listOf(
                navArgument("code") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "suitebde://auth?code={code}"
                }
            )
        ) {
            AuthView(
                onUserLogged = onUserLogged,
                modifier = Modifier.padding(padding),
                code = it.arguments?.getString("code")
            )
        }
    }
}

enum class NavigationItem(
    val route: String,
    val icon: Int,
    val title: Int,
) {

    FEED(
        "feed",
        R.drawable.ic_baseline_newspaper_24,
        R.string.feed_title
    ),
    CLUBS(
        "clubs",
        R.drawable.ic_baseline_pedal_bike_24,
        R.string.clubs_title
    ),
    ACCOUNT(
        "account",
        R.drawable.ic_baseline_person_24,
        R.string.account_title
    )

}
