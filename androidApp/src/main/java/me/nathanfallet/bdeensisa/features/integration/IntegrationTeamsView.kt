package me.nathanfallet.bdeensisa.features.integration

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntegrationTeamsView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    viewModel: IntegrationTeamsViewModel,
    mainViewModel: MainViewModel
) {

    val teams by viewModel.getTeams().observeAsState()
    val challenges by viewModel.getChallenges().observeAsState()

    LazyColumn(
        modifier
    ) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = "Chasse") },
                actions = {
                    IconButton(onClick = {
                        navigate("feed/integration/create")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_add_24),
                            contentDescription = "Nouveau"
                        )
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                text = "Equipes",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(teams?.withIndex()?.toList() ?: listOf()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp)
                    .clickable {
                        mainViewModel.setSelectedIntegrationTeam(it.value)
                    },
                elevation = 4.dp
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    if (mainViewModel.getIntegrationConfiguration().value?.showRank == true) {
                        Text(
                            text = "#${it.index + 1}",
                            style = MaterialTheme.typography.h6
                        )
                    }
                    Column {
                        Text(
                            text = it.value.name
                        )
                        if (
                            it.value.score != null &&
                            mainViewModel.getIntegrationConfiguration().value?.showScore == true
                        ) {
                            Text(
                                text = "${it.value.score} pts",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
        item {
            Text(
                text = "Défis",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(challenges ?: listOf()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = it.name
                        )
                        Text(
                            text = "${it.reward} pts",
                            color = Color.Gray
                        )
                    }
                    Text(text = it.description)
                    it.executionsPerTeam?.let { executionsPerTeam ->
                        Text(text = "${executionsPerTeam} fois par équipe")
                    }
                    it.executionsPerUser?.let { executionsPerUser ->
                        Text(text = "${executionsPerUser} fois par personne")
                    }
                }
            }
        }
    }

}