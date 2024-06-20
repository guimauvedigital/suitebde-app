package com.suitebde.features.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.suitebde.models.application.Url
import com.suitebde.ui.components.feed.FeedRootView
import com.suitebde.viewmodels.feed.FeedViewModel
import com.suitebde.viewmodels.feed.SearchViewModel
import com.suitebde.viewmodels.root.RootViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun FeedView(
    navigate: (String) -> Unit,
    rootViewModel: RootViewModel,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<FeedViewModel>()
    val searchViewModel = koinViewModel<SearchViewModel>()

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    val subscriptions by viewModel.subscriptions.collectAsState()
    val events by viewModel.events.collectAsState()

    val sendNotificationVisible by viewModel.sendNotificationVisible.collectAsState()
    val showScannerVisible by viewModel.showScannerVisible.collectAsState()

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
        sendNotificationVisible = sendNotificationVisible,
        showScannerVisible = showScannerVisible,
        onOpenURL = {
            rootViewModel.onOpenURL(Url(it.scheme, it.host, it.path))
        },
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
