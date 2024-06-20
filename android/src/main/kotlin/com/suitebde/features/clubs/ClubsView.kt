package com.suitebde.features.clubs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.suitebde.ui.components.clubs.ClubsListView
import com.suitebde.viewmodels.clubs.ClubsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun ClubsView(
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<ClubsViewModel>()

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    val myClubs by viewModel.myClubs.collectAsState()
    val moreClubs by viewModel.moreClubs.collectAsState()

    ClubsListView(
        myClubs = myClubs ?: emptyList(),
        moreClubs = moreClubs ?: emptyList(),
        loadMoreIfNeeded = { viewModel.loadMoreIfNeeded(it) },
        navigate = navigate,
        modifier = modifier,
    )

}
