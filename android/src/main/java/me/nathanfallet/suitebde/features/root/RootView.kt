package me.nathanfallet.suitebde.features.root

import android.app.Application
import android.nfc.NfcAdapter
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.features.MainActivity
import me.nathanfallet.suitebde.features.account.AccountView
import me.nathanfallet.suitebde.features.account.AccountViewModel
import me.nathanfallet.suitebde.features.calendar.CalendarView
import me.nathanfallet.suitebde.features.calendar.CalendarViewModel
import me.nathanfallet.suitebde.features.chat.*
import me.nathanfallet.suitebde.features.clubs.ClubView
import me.nathanfallet.suitebde.features.clubs.ClubViewModel
import me.nathanfallet.suitebde.features.clubs.ClubsView
import me.nathanfallet.suitebde.features.clubs.ClubsViewModel
import me.nathanfallet.suitebde.features.events.EventView
import me.nathanfallet.suitebde.features.feed.FeedView
import me.nathanfallet.suitebde.features.notifications.SendNotificationView
import me.nathanfallet.suitebde.features.scanner.ScanHistoryView
import me.nathanfallet.suitebde.features.scanner.ScanHistoryViewModel
import me.nathanfallet.suitebde.features.settings.SettingsView
import me.nathanfallet.suitebde.features.shop.ShopItemView
import me.nathanfallet.suitebde.features.shop.ShopItemViewModel
import me.nathanfallet.suitebde.features.users.UserView
import me.nathanfallet.suitebde.features.users.UserViewModel
import me.nathanfallet.suitebde.features.users.UsersView
import me.nathanfallet.suitebde.features.users.UsersViewModel

@Composable
fun RootView(
    owner: MainActivity, // TODO: Remove this dependency
) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val viewModel: RootViewModel = viewModel()

    viewModel.getShowAccount().observe(owner) {
        if (it != null) navController.navigate("account")
    }
    viewModel.getNFCMode().observe(owner) {
        if (it != null) {
            owner.nfcAdapter?.enableReaderMode(
                owner,
                viewModel::nfcResult,
                NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null
            )
        } else {
            owner.nfcAdapter?.disableReaderMode(owner)
        }
    }
    viewModel.getSelectedUser().observe(owner) {
        if (it != null) navController.navigate("account/users/user")
    }
    viewModel.getSelectedEvent().observe(owner) {
        if (it != null) navController.navigate("feed/event")
    }
    viewModel.getSelectedClub().observe(owner) {
        if (it != null) navController.navigate("clubs/club")
    }
    viewModel.getSelectedConversation().observe(owner) {
        if (it != null) navController.navigate("chat/conversation")
    }
    viewModel.getSelectedShopItem().observe(owner) {
        if (it != null) navController.navigate("feed/shop/item")
    }
    viewModel.getSelectedIntegrationTeam().observe(owner) {
        if (it != null) navController.navigate("feed/integration/team")
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navBackStackEntry?.destination?.route
                NavigationItem.entries.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
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
        TabNavigation(
            owner = owner,
            viewModel = viewModel,
            navController = navController,
            padding = padding
        )
    }
}

@Composable
fun TabNavigation(
    owner: MainActivity, // TODO: Remove this dependency
    viewModel: RootViewModel,
    navController: NavHostController,
    padding: PaddingValues,
) {

    NavHost(navController = navController, startDestination = "feed") {
        composable("feed") {
            FeedView(
                modifier = Modifier.padding(padding),
                navigate = navController::navigate,
                rootViewModel = viewModel
            )
        }
        composable("feed/events/{eventId}") { backStackEntry ->
            EventView(
                id = backStackEntry.arguments?.getString("eventId")!!,
                modifier = Modifier.padding(padding),
                navigateUp = navController::navigateUp
            )
        }
        composable("feed/event") {
            EventView(
                id = viewModel.getSelectedEvent().value?.id ?: "",
                modifier = Modifier.padding(padding),
                navigateUp = navController::navigateUp
            )
        }
        composable("feed/settings") {
            SettingsView(
                modifier = Modifier.padding(padding)
            )
        }
        composable("feed/send_notification") {
            SendNotificationView(
                modifier = Modifier.padding(padding),
                rootViewModel = viewModel
            )
        }
        composable("feed/suggest_event") {
            EventView(
                modifier = Modifier.padding(padding),
                id = null,
                navigateUp = navController::navigateUp
            )
        }
        composable("feed/shop/item") {
            ShopItemView(
                modifier = Modifier.padding(padding),
                viewModel = ShopItemViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getSelectedShopItem().value!!
                ),
                rootViewModel = viewModel,
                navigateUp = navController::navigateUp
            )
        }
        composable("calendar") {
            CalendarView(
                modifier = Modifier.padding(padding),
                viewModel = CalendarViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value
                ),
                rootViewModel = viewModel,
                owner = owner
            )
        }
        composable("clubs") {
            ClubsView(
                modifier = Modifier.padding(padding),
                viewModel = ClubsViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value
                ),
                rootViewModel = viewModel
            )
        }
        composable("clubs/club") {
            ClubView(
                modifier = Modifier.padding(padding),
                viewModel = ClubViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getSelectedClub().value!!
                ),
                rootViewModel = viewModel,
                navigateUp = navController::navigateUp
            )
        }
        composable("chat") {
            ChatView(
                modifier = Modifier.padding(padding),
                viewModel = ChatViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value
                ),
                rootViewModel = viewModel
            )
        }
        composable("chat/conversation") {
            ConversationView(
                modifier = Modifier.padding(padding),
                viewModel = ConversationViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value,
                    viewModel.getSelectedConversation().value!!
                ),
                rootViewModel = viewModel,
                navigate = navController::navigate,
                navigateUp = navController::navigateUp
            )
        }
        composable("chat/conversation/settings") {
            ConversationSettingsView(
                modifier = Modifier.padding(padding),
                viewModel = ConversationSettingsViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value,
                    viewModel.getSelectedConversation().value!!
                ),
                rootViewModel = viewModel,
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
                    viewModel.getToken().value,
                    viewModel.getUser().value?.id,
                    viewModel::saveToken,
                    viewModel::showAccount
                ),
                rootViewModel = viewModel
            )
        }
        composable("account/scan_history") {
            ScanHistoryView(
                modifier = Modifier.padding(padding),
                viewModel = ScanHistoryViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value
                ),
                rootViewModel = viewModel
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
        ) { backStackEntry ->
            AccountView(
                modifier = Modifier.padding(padding),
                navigate = navController::navigate,
                viewModel = AccountViewModel(
                    LocalContext.current.applicationContext as Application,
                    backStackEntry.arguments?.getString("code"),
                    viewModel.getToken().value,
                    viewModel.getUser().value?.id,
                    viewModel::saveToken,
                    viewModel::showAccount
                ),
                rootViewModel = viewModel
            )
        }
        composable("account/edit") {
            UserView(
                modifier = Modifier.padding(padding),
                viewModel = UserViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value,
                    viewModel.getUser().value,
                    viewModel.getUser().value!!,
                    editable = false,
                    isMyAccount = true
                ),
                rootViewModel = viewModel,
                navigateUp = navController::navigateUp
            )
        }
        composable("account/users") {
            UsersView(
                modifier = Modifier.padding(padding),
                viewModel = UsersViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value
                ),
                rootViewModel = viewModel,
                owner = owner
            )
        }
        composable("account/users/user") {
            UserView(
                modifier = Modifier.padding(padding),
                viewModel = UserViewModel(
                    LocalContext.current.applicationContext as Application,
                    viewModel.getToken().value,
                    viewModel.getUser().value,
                    viewModel.getSelectedUser().value!!,
                    viewModel.getUser().value?.hasPermission("admin.users.edit") == true
                ),
                rootViewModel = viewModel,
                navigateUp = navController::navigateUp
            )
        }
    }

}

enum class NavigationItem(
    val route: String,
    val icon: Int,
    val title: String,
) {

    FEED(
        "feed",
        R.drawable.ic_baseline_newspaper_24,
        "Actualité"
    ),
    CALENDAR(
        "calendar",
        R.drawable.ic_baseline_calendar_month_24,
        "Calendrier"
    ),
    CLUBS(
        "clubs",
        R.drawable.ic_baseline_pedal_bike_24,
        "Clubs"
    ),
    CHAT(
        "chat",
        R.drawable.ic_baseline_chat_bubble_24,
        "Chat"
    ),
    ACCOUNT(
        "account",
        R.drawable.ic_baseline_person_24,
        "Mon compte"
    )

}
