package me.nathanfallet.suitebde.ui.components.clubs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.models.clubs.UserInClub
import me.nathanfallet.suitebde.models.ensisa.User

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
fun ClubDetailsView(
    club: Club,
    users: List<UserInClub>,
    user: User?,
    join: () -> Unit,
    leave: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = club.name) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.app_back)
                        )
                    }
                },
                actions = {
                    if (users.any { it.userId == user?.id }) {
                        IconButton(onClick = leave) {
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
            ClubCard(
                club = club,
                badgeText = if (user?.cotisant != null && users.none { it.userId == user.id }) "REJOINDRE" else null,
                badgeColor = MaterialTheme.colorScheme.primary,
                detailsEnabled = false
            )
        }
        item {
            Text(
                text = "Membres",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(users) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp)
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
                            text = "${it.user?.firstName} ${it.user?.lastName}"
                        )
                    }
                    Text(
                        text = if (it.role.admin) "ADMIN" else "MEMBRE",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                if (it.role.admin) Color.Black
                                else Color(0xFF0BDA51),
                                MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

}
