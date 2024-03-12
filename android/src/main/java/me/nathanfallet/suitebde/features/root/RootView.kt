package me.nathanfallet.suitebde.features.root

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
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.BuildConfig
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.features.MainActivity
import me.nathanfallet.suitebde.features.account.AccountView
import me.nathanfallet.suitebde.features.account.AccountViewModel
import me.nathanfallet.suitebde.features.auth.AuthView
import me.nathanfallet.suitebde.features.chat.*
import me.nathanfallet.suitebde.features.clubs.ClubView
import me.nathanfallet.suitebde.features.clubs.ClubsView
import me.nathanfallet.suitebde.features.events.EventView
import me.nathanfallet.suitebde.features.feed.FeedView
import me.nathanfallet.suitebde.features.notifications.SendNotificationView
import me.nathanfallet.suitebde.features.scanner.ScanHistoryView
import me.nathanfallet.suitebde.features.scanner.ScanHistoryViewModel
import me.nathanfallet.suitebde.features.settings.SettingsView
import me.nathanfallet.suitebde.features.subscriptions.SubscriptionView
import me.nathanfallet.suitebde.features.users.UserView
import me.nathanfallet.suitebde.features.users.UserViewModel
import me.nathanfallet.suitebde.features.users.UsersView
import me.nathanfallet.suitebde.features.users.UsersViewModel
import me.nathanfallet.suitebde.ui.components.auth.AuthErrorView
import me.nathanfallet.suitebde.viewmodels.root.RootViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun RootView(
    owner: MainActivity, // TODO: Remove this dependency
) {

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

    oldViewModel.getShowAccount().observe(owner) {
        if (it != null) navController.navigate("account")
    }
    oldViewModel.getSelectedUser().observe(owner) {
        if (it != null) navController.navigate("account/users/user")
    }
    oldViewModel.getSelectedConversation().observe(owner) {
        if (it != null) navController.navigate("chat/conversation")
    }

    Scaffold(
        bottomBar = {
            if (user == null && BuildConfig.FLAVOR != "ensisa") return@Scaffold
            NavigationBar {
                val currentRoute = navBackStackEntry?.destination?.route
                NavigationItem.entries.filter { it != NavigationItem.ACCOUNT || BuildConfig.FLAVOR == "ensisa" }
                    .forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    painterResource(id = item.icon),
                                    contentDescription = stringResource(item.title)
                                )
                            },
                            label = { Text(text = stringResource(item.title)) },
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
        // TODO: Remove this when ensisa is ready
        if (BuildConfig.FLAVOR == "ensisa") TabNavigation(
            owner = owner,
            viewModel = viewModel,
            oldViewModel = oldViewModel,
            navController = navController,
            padding = padding
        ) else if (loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(padding).fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
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
                owner = owner,
                viewModel = viewModel,
                oldViewModel = oldViewModel,
                navController = navController,
                padding = padding
            )
        } ?: run {
            AuthNavigation(
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
}

@Composable
@Suppress("FunctionName")
fun TabNavigation(
    owner: MainActivity, // TODO: Remove this dependency
    viewModel: RootViewModel,
    oldViewModel: OldRootViewModel,
    navController: NavHostController,
    padding: PaddingValues,
) {

    NavHost(navController = navController, startDestination = "feed") {
        composable("feed") {
            FeedView(
                navigate = navController::navigate,
                oldRootViewModel = oldViewModel,
                modifier = Modifier.padding(padding),
            )
        }
        composable("feed/subscriptions/{subscriptionId}") {
            SubscriptionView(
                id = it.arguments?.getString("subscriptionId")!!,
                navigateUp = navController::navigateUp,
                modifier = Modifier.padding(padding)
            )
        }
        composable("feed/events/{eventId}") {
            EventView(
                id = it.arguments?.getString("eventId")!!,
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
                modifier = Modifier.padding(padding),
                oldRootViewModel = oldViewModel
            )
        }
        composable("feed/suggest_event") {
            EventView(
                id = null,
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
                id = it.arguments?.getString("clubId")!!,
                navigateUp = navController::navigateUp,
                oldRootViewModel = oldViewModel,
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
        composable("account") {
            AccountView(
                modifier = Modifier.padding(padding),
                navigate = navController::navigate,
                viewModel = AccountViewModel(
                    LocalContext.current.applicationContext as Application,
                    null,
                    oldViewModel.getToken().value,
                    oldViewModel.getUser().value?.id,
                    oldViewModel::saveToken,
                    oldViewModel::showAccount
                ),
                oldRootViewModel = oldViewModel
            )
        }
        composable("account/scan_history") {
            ScanHistoryView(
                modifier = Modifier.padding(padding),
                viewModel = ScanHistoryViewModel(
                    LocalContext.current.applicationContext as Application,
                    oldViewModel.getToken().value
                ),
                oldRootViewModel = oldViewModel
            )
        }
        composable(
            "account/code",
            arguments = listOf(
                navArgument("code") { type = NavType.StringType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "bdeensisa://authorize?{code}"
                }
            )
        ) {
            AccountView(
                modifier = Modifier.padding(padding),
                navigate = navController::navigate,
                viewModel = AccountViewModel(
                    LocalContext.current.applicationContext as Application,
                    it.arguments?.getString("code"),
                    oldViewModel.getToken().value,
                    oldViewModel.getUser().value?.id,
                    oldViewModel::saveToken,
                    oldViewModel::showAccount
                ),
                oldRootViewModel = oldViewModel
            )
        }
        composable("account/edit") {
            UserView(
                modifier = Modifier.padding(padding),
                viewModel = UserViewModel(
                    LocalContext.current.applicationContext as Application,
                    oldViewModel.getToken().value,
                    oldViewModel.getUser().value,
                    oldViewModel.getUser().value!!,
                    editable = false,
                    isMyAccount = true
                ),
                oldRootViewModel = oldViewModel,
                navigateUp = navController::navigateUp
            )
        }
        composable("account/users") {
            UsersView(
                modifier = Modifier.padding(padding),
                viewModel = UsersViewModel(
                    LocalContext.current.applicationContext as Application,
                    oldViewModel.getToken().value
                ),
                oldRootViewModel = oldViewModel,
                owner = owner
            )
        }
        composable("account/users/user") {
            UserView(
                modifier = Modifier.padding(padding),
                viewModel = UserViewModel(
                    LocalContext.current.applicationContext as Application,
                    oldViewModel.getToken().value,
                    oldViewModel.getUser().value,
                    oldViewModel.getSelectedUser().value!!,
                    oldViewModel.getUser().value?.hasPermission("admin.users.edit") == true
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
