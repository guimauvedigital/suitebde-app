package me.nathanfallet.bdeensisa.features.integration

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntegrationTeamView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    viewModel: IntegrationTeamViewModel,
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {

    val user by mainViewModel.getUser().observeAsState()

    val members by viewModel.getMembers().observeAsState()
    val executions by viewModel.getExecutions().observeAsState()
    val member by viewModel.getMember().observeAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = viewModel.team.name) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    if (member == true) {
                        IconButton(onClick = {
                            navigate("feed/integration/execution")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                                contentDescription = "Compléter un défi"
                            )
                        }
                        IconButton(
                            onClick = { viewModel.leave(mainViewModel.getToken().value, user) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_logout_24),
                                contentDescription = "Quitter"
                            )
                        }
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Text(
                                text = viewModel.team.name,
                                fontWeight = FontWeight.Bold
                            )
                            Text("${viewModel.team.membersCount} membre${if (viewModel.team.membersCount != 1L) "s" else ""}")
                        }
                        member?.let {
                            if (it && viewModel.team.score != null) {
                                Text("${viewModel.team.score} pts")
                            } else {
                                Text(
                                    text = "REJOINDRE",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colors.primary,
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .clickable {
                                            viewModel.join(mainViewModel.getToken().value, user)
                                        }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = viewModel.team.description ?: ""
                    )
                }
            }
        }
        item {
            Text(
                text = "Membres",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(members ?: listOf()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = "${it.user?.firstName} ${it.user?.lastName}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.user?.description ?: "")
                    }
                    Text(
                        text = if (it.role == "parrain") "PARRAIN" else "MEMBRE",
                        style = MaterialTheme.typography.caption,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                if (it.role == "parrain") Color.Black
                                else Color(0xFF0BDA51),
                                MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
        item {
            Text(
                text = "Défis réalisés",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(executions ?: listOf()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = it.challenge?.name ?: "",
                            fontWeight = FontWeight.Bold
                        )
                        Text("${it.user?.firstName} ${it.user?.lastName}")
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (it.validated) "VALIDÉ" else "EN ATTENTE",
                            style = MaterialTheme.typography.caption,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    if (it.validated) Color(0xFF0BDA51)
                                    else Color(0xFFFF9503),
                                    MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                        Text("${it.challenge?.reward} pts")
                    }
                }
            }
        }
    }

}