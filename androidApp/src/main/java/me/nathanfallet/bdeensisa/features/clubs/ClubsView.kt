package me.nathanfallet.bdeensisa.features.clubs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClubsView(
    modifier: Modifier = Modifier,
    viewModel: ClubsViewModel,
    mainViewModel: MainViewModel
) {

    val user by mainViewModel.getUser().observeAsState()

    val mine by viewModel.getMine().observeAsState()
    val clubs by viewModel.getClubs().observeAsState()

    LazyColumn(
        modifier
    ) {
        stickyHeader {
            TopAppBar(
                title = {
                    Text(text = "Clubs")
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (mine?.isNotEmpty() == true) {
            item {
                Text(
                    text = "Mes clubs",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
            items(mine ?: listOf()) {
                ClubCard(
                    club = it.club!!,
                    badgeText = if (it.club?.validated != true) "EN ATTENTE"
                    else if (it.role == "admin") "ADMIN"
                    else "MEMBRE",
                    badgeColor = if (it.club?.validated != true) Color(0xFFFFA500)
                    else if (it.role == "admin") Color.Black
                    else Color(0xFF0BDA51),
                    action = null,
                    detailsEnabled = true,
                    showDetails = mainViewModel::setSelectedClub
                )
                viewModel.loadMore(mainViewModel.getToken().value, it.clubId)
            }
            item {
                Text(
                    text = "Autres clubs",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
        }
        items(clubs?.filter { club -> mine?.none { it.clubId == club.id } ?: true } ?: listOf()) {
            ClubCard(
                club = it,
                badgeText = if (user?.cotisant != null) "REJOINDRE" else null,
                badgeColor = MaterialTheme.colors.primary,
                action = { viewModel.joinClub(it.id, mainViewModel.getToken().value) },
                detailsEnabled = true,
                showDetails = mainViewModel::setSelectedClub
            )
            viewModel.loadMore(mainViewModel.getToken().value, it.id)
        }
    }

}