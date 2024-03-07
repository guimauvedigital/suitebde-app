package me.nathanfallet.suitebde.features.clubs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.features.root.OldRootViewModel
import me.nathanfallet.suitebde.ui.components.clubs.ClubDetailsView
import me.nathanfallet.suitebde.viewmodels.clubs.ClubViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Suppress("FunctionName")
fun ClubView(
    id: String?,
    navigateUp: () -> Unit,
    oldRootViewModel: OldRootViewModel,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<ClubViewModel>(
        parameters = { parametersOf(id) }
    )

    LaunchedEffect(id) {
        viewModel.onAppear()
    }

    val user by oldRootViewModel.getUser().observeAsState()

    val club by viewModel.club.collectAsState()
    val users by viewModel.users.collectAsState()

    club?.let {
        ClubDetailsView(
            club = it,
            users = users ?: emptyList(),
            user = user,
            navigateUp = navigateUp,
            onJoinLeaveClicked = {
                viewModel.viewModelScope.coroutineScope.launch {
                    viewModel.onJoinLeaveClicked()
                }
            },
            modifier = modifier,
        )
    } ?: run {
        CircularProgressIndicator(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }

}
