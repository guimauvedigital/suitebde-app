package com.suitebde.features.clubs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.suitebde.ui.components.clubs.ClubDetailsView
import com.suitebde.viewmodels.clubs.ClubViewModel
import dev.kaccelero.models.UUID
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Suppress("FunctionName")
fun ClubView(
    id: UUID?,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<ClubViewModel>(
        parameters = { parametersOf(id) }
    )

    LaunchedEffect(id) {
        viewModel.onAppear()
    }

    val club by viewModel.club.collectAsState()
    val users by viewModel.users.collectAsState()

    club?.let {
        ClubDetailsView(
            club = it,
            users = users ?: emptyList(),
            navigateUp = navigateUp,
            loadMore = { id ->
                viewModel.loadMoreIfNeeded(id)
            },
            onJoinLeaveClicked = {
                viewModel.viewModelScope.coroutineScope.launch {
                    viewModel.onJoinLeaveClicked()
                }
            },
            modifier = modifier,
        )
    } ?: run {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

}
