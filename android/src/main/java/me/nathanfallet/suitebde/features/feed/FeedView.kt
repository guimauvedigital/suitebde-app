package me.nathanfallet.suitebde.features.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import me.nathanfallet.suitebde.features.root.OldRootViewModel
import me.nathanfallet.suitebde.ui.components.feed.FeedRootView
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import me.nathanfallet.suitebde.viewmodels.feed.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun FeedView(
    navigate: (String) -> Unit,
    oldRootViewModel: OldRootViewModel,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<FeedViewModel>()
    val searchViewModel = koinViewModel<SearchViewModel>()

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    val user by oldRootViewModel.getUser().observeAsState()

    val subscriptions by viewModel.subscriptions.collectAsState()
    val events by viewModel.events.collectAsState()

    val search by searchViewModel.search.collectAsState()
    val users by searchViewModel.users.collectAsState()
    val clubs by searchViewModel.clubs.collectAsState()
    val hasMoreUsers by searchViewModel.hasMoreUsers.collectAsState()
    val hasMoreClubs by searchViewModel.hasMoreClubs.collectAsState()

    FeedRootView(
        search = search,
        updateSearch = searchViewModel::updateSearch,
        subscriptions = subscriptions ?: emptyList(),
        events = events ?: emptyList(),
        sendNotificationVisible = user?.hasPermission("admin.notifications") == true,
        showScannerVisible = user?.hasPermission("admin.users.view") == true,
        onOpenURL = oldRootViewModel::onOpenURL,
        users = users ?: emptyList(),
        clubs = clubs ?: emptyList(),
        hasMoreUsers = hasMoreUsers,
        hasMoreClubs = hasMoreClubs,
        loadMoreUsers = searchViewModel::loadMoreUsers,
        loadMoreClubs = searchViewModel::loadMoreClubs,
        navigate = navigate,
        modifier = modifier,
    )

}
