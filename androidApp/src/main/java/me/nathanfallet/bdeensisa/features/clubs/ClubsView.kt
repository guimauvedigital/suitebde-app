package me.nathanfallet.bdeensisa.features.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.features.MainViewModel

@Composable
fun ClubsView(
    modifier: Modifier = Modifier,
    viewModel: ClubsViewModel,
    mainViewModel: MainViewModel
) {

    val mine by viewModel.getMine().observeAsState()
    val clubs by viewModel.getClubs().observeAsState()

    LazyColumn(
        modifier
    ) {
        item {
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 4.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = it.club?.name ?: "",
                                    fontWeight = FontWeight.Bold
                                )
                                Text("${it.club?.membersCount} membre${if (it.club?.membersCount != 1L) "s" else ""}")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (it.club?.validated != true) "EN ATTENTE"
                                else if (it.role == "admin") "ADMIN"
                                else "MEMBRE",
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(
                                        if (it.club?.validated != true) Color(0xFFFFA500)
                                        else if (it.role == "admin") Color.Black
                                        else Color(0xFF0BDA51), MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it.club?.description ?: "")
                    }
                }
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = it.name,
                                fontWeight = FontWeight.Bold
                            )
                            Text("${it.membersCount} membre${if (it.membersCount != 1L) "s" else ""}")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "REJOINDRE",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colors.primary,
                                    MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .clickable {
                                    viewModel.joinClub(it.id, mainViewModel.getToken().value)
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it.description ?: "")
                }
            }
            viewModel.loadMore(mainViewModel.getToken().value, it.id)
        }
    }

}