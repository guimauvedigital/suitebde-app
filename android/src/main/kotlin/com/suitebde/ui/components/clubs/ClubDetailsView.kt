package com.suitebde.ui.components.clubs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.suitebde.R
import com.suitebde.models.clubs.Club
import com.suitebde.models.clubs.UserInClub
import com.suitebde.ui.components.navigation.DefaultNavigationBar
import com.suitebde.ui.components.users.UserCard
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
fun ClubDetailsView(
    club: Club,
    users: List<UserInClub>,
    loadMore: (UUID) -> Unit,
    onJoinLeaveClicked: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        stickyHeader {
            DefaultNavigationBar(
                title = club.name,
                navigateUp = navigateUp,
                image = {
                    AsyncImage(
                        model = club.logo ?: "",
                        placeholder = painterResource(R.drawable.default_event_image),
                        error = painterResource(R.drawable.default_event_image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = it
                    )
                }
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.clubs_information),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(if (club.usersCount != 1L) R.string.clubs_members else R.string.clubs_member)
                        .format(club.usersCount),
                    color = Color.Gray
                )
            }
        }
        item {
            Text(
                text = club.description,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            if (club.isMember == true) OutlinedButton(
                onClick = onJoinLeaveClicked,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                content = { Text(stringResource(R.string.clubs_button_leave)) }
            ) else Button(
                onClick = onJoinLeaveClicked,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                content = { Text(stringResource(R.string.clubs_button_join)) }
            )
        }
        item {
            Text(
                text = stringResource(R.string.clubs_information_members),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        items(users) {
            it.user?.let { user ->
                UserCard(
                    user = user,
                    customDescription = it.role.name,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                loadMore(it.id)
            }
        }

        item {
            Spacer(modifier = Modifier)
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun ClubDetailsViewPreview() {
    ClubDetailsView(
        club = Club(
            id = UUID(),
            associationId = UUID(),
            name = "Club running",
            description = "A cool club",
            logo = "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 12,
            isMember = true
        ),
        users = listOf(),
        loadMore = {},
        onJoinLeaveClicked = {},
        navigateUp = {}
    )
}
