package me.nathanfallet.bdeensisa.features

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.features.account.AccountView
import me.nathanfallet.bdeensisa.features.account.AccountViewModel
import me.nathanfallet.bdeensisa.features.calendar.CalendarView
import me.nathanfallet.bdeensisa.features.calendar.CalendarViewModel
import me.nathanfallet.bdeensisa.features.clubs.ClubView
import me.nathanfallet.bdeensisa.features.clubs.ClubViewModel
import me.nathanfallet.bdeensisa.features.clubs.ClubsView
import me.nathanfallet.bdeensisa.features.clubs.ClubsViewModel
import me.nathanfallet.bdeensisa.features.events.EventView
import me.nathanfallet.bdeensisa.features.events.EventViewModel
import me.nathanfallet.bdeensisa.features.feed.FeedView
import me.nathanfallet.bdeensisa.features.notifications.SendNotificationView
import me.nathanfallet.bdeensisa.features.settings.SettingsView
import me.nathanfallet.bdeensisa.features.shop.ShopItemView
import me.nathanfallet.bdeensisa.features.shop.ShopItemViewModel
import me.nathanfallet.bdeensisa.features.shop.ShopView
import me.nathanfallet.bdeensisa.features.users.UserView
import me.nathanfallet.bdeensisa.features.users.UserViewModel
import me.nathanfallet.bdeensisa.features.users.UsersView
import me.nathanfallet.bdeensisa.features.users.UsersViewModel
import me.nathanfallet.bdeensisa.workers.FetchEventsWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        scheduleAppRefresh()

        setContent {
            BDEApp(this)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun scheduleAppRefresh() {
        PeriodicWorkRequest
            .Builder(
                FetchEventsWorker::class.java,
                1, TimeUnit.HOURS, // repeatInterval (the period cycle)
                15, TimeUnit.MINUTES // flexInterval (the tolerance for when to run)
            )
            .build()
            .also { workRequest ->
                WorkManager.getInstance(this).enqueue(workRequest)
            }
    }

}

enum class NavigationItem(
    val route: String,
    val icon: Int,
    val title: String
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
    SHOP(
        "shop",
        R.drawable.ic_baseline_shopping_cart_24,
        "Boutique"
    ),
    ACCOUNT(
        "account",
        R.drawable.ic_baseline_person_24,
        "Mon compte"
    )

}

@Composable
fun BDEApp(owner: LifecycleOwner) {
    BDETheme {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val viewModel: MainViewModel = viewModel()

        viewModel.getShowAccount().observe(owner) {
            if (it != null) navController.navigate("account")
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
        viewModel.getSelectedShopItem().observe(owner) {
            if (it != null) navController.navigate("shop/item")
        }

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val currentRoute = navBackStackEntry?.destination?.route
                    NavigationItem
                        .values()
                        .forEach { item ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painterResource(id = item.icon),
                                        contentDescription = item.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        fontSize = 9.sp
                                    )
                                },
                                selectedContentColor = Color.White,
                                unselectedContentColor = Color.White.copy(0.4f),
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
            NavHost(navController = navController, startDestination = "feed") {
                composable("feed") {
                    FeedView(
                        modifier = Modifier.padding(padding),
                        navigate = navController::navigate,
                        mainViewModel = viewModel
                    )
                }
                composable("feed/event") {
                    EventView(
                        modifier = Modifier.padding(padding),
                        viewModel = EventViewModel(
                            LocalContext.current.applicationContext as Application,
                            viewModel.getSelectedEvent().value!!,
                            viewModel.getUser().value?.hasPermission("admin.events.edit") == true
                        ),
                        mainViewModel = viewModel
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
                        mainViewModel = viewModel
                    )
                }
                composable("feed/suggest_event") {
                    EventView(
                        modifier = Modifier.padding(padding),
                        viewModel = EventViewModel(
                            LocalContext.current.applicationContext as Application,
                            null,
                            false
                        ),
                        mainViewModel = viewModel
                    )
                }
                composable("calendar") {
                    CalendarView(
                        modifier = Modifier.padding(padding),
                        viewModel = CalendarViewModel(
                            LocalContext.current.applicationContext as Application,
                            viewModel.getToken().value
                        ),
                        mainViewModel = viewModel,
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
                        mainViewModel = viewModel
                    )
                }
                composable("clubs/club") {
                    ClubView(
                        modifier = Modifier.padding(padding),
                        viewModel = ClubViewModel(
                            LocalContext.current.applicationContext as Application,
                            viewModel.getSelectedClub().value!!
                        ),
                        mainViewModel = viewModel
                    )
                }
                composable("shop") {
                    ShopView(
                        modifier = Modifier.padding(padding),
                        mainViewModel = viewModel
                    )
                }
                composable("shop/item") {
                    ShopItemView(
                        modifier = Modifier.padding(padding),
                        viewModel = ShopItemViewModel(
                            LocalContext.current.applicationContext as Application,
                            viewModel.getSelectedShopItem().value!!
                        ),
                        mainViewModel = viewModel
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
                        mainViewModel = viewModel
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
                        mainViewModel = viewModel
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
                        mainViewModel = viewModel
                    )
                }
                composable("account/users") {
                    UsersView(
                        modifier = Modifier.padding(padding),
                        viewModel = UsersViewModel(
                            LocalContext.current.applicationContext as Application,
                            viewModel.getToken().value
                        ),
                        mainViewModel = viewModel,
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
                        mainViewModel = viewModel
                    )
                }
            }
        }

    }
}
