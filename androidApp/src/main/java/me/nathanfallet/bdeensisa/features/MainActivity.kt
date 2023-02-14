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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.features.account.AccountView
import me.nathanfallet.bdeensisa.features.account.AccountViewModel
import me.nathanfallet.bdeensisa.features.clubs.ClubView
import me.nathanfallet.bdeensisa.features.clubs.ClubViewModel
import me.nathanfallet.bdeensisa.features.clubs.ClubsView
import me.nathanfallet.bdeensisa.features.clubs.ClubsViewModel
import me.nathanfallet.bdeensisa.features.feed.FeedView
import me.nathanfallet.bdeensisa.features.manage.ManageView
import me.nathanfallet.bdeensisa.features.users.UserView
import me.nathanfallet.bdeensisa.features.users.UserViewModel
import me.nathanfallet.bdeensisa.features.users.UsersView
import me.nathanfallet.bdeensisa.features.users.UsersViewModel
import me.nathanfallet.bdeensisa.models.User

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

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

}

enum class NavigationItem(
    val route: String,
    val icon: Int,
    val title: String,
    val shown: (User?) -> Boolean = { true }
) {

    FEED(
        "feed",
        R.drawable.ic_baseline_newspaper_24,
        "Actualité"
    ),
    CLUBS(
        "clubs",
        R.drawable.ic_baseline_pedal_bike_24,
        "Clubs"
    ),
    ACCOUNT(
        "account",
        R.drawable.ic_baseline_person_24,
        "Mon compte"
    ),
    MANAGE(
        "manage",
        R.drawable.ic_baseline_app_settings_alt_24,
        "Gestion",
        { it?.hasPermission("admin.users.view") ?: false }
    )

}

@Composable
fun BDEApp(owner: LifecycleOwner) {
    BDETheme {

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val viewModel: MainViewModel = viewModel()

        val user by viewModel.getUser().observeAsState()

        viewModel.getSelectedUser().observe(owner) {
            if (it != null) navController.navigate("manage/user")
        }
        viewModel.getSelectedClub().observe(owner) {
            if (it != null) navController.navigate("clubs/club")
        }

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val currentRoute = navBackStackEntry?.destination?.route
                    NavigationItem
                        .values()
                        .filter { it.shown(user) }
                        .forEach { item ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painterResource(id = item.icon),
                                        contentDescription = item.title
                                    )
                                },
                                label = { Text(text = item.title) },
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
                        navigate = navController::navigate
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
                composable("account") {
                    AccountView(
                        modifier = Modifier.padding(padding),
                        viewModel = AccountViewModel(
                            LocalContext.current.applicationContext as Application,
                            null,
                            viewModel::saveToken
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
                        viewModel = AccountViewModel(
                            LocalContext.current.applicationContext as Application,
                            backStackEntry.arguments?.getString("code"),
                            viewModel::saveToken
                        ),
                        mainViewModel = viewModel
                    )
                }
                composable("manage") {
                    ManageView(
                        modifier = Modifier.padding(padding),
                        navigate = navController::navigate,
                        mainViewModel = viewModel
                    )
                }
                composable("manage/users") {
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
                composable("manage/user") {
                    UserView(
                        modifier = Modifier.padding(padding),
                        viewModel = UserViewModel(
                            LocalContext.current.applicationContext as Application,
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
