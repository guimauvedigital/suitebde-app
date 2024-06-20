package com.suitebde.ui.components.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.suitebde.R
import com.suitebde.models.clubs.Club
import com.suitebde.models.users.User
import com.suitebde.ui.components.clubs.ClubCard
import com.suitebde.ui.components.users.UserCard

@Suppress("FunctionName")
fun LazyListScope.FeedSearchView(
    users: List<User>,
    clubs: List<Club>,
    hasMoreUsers: Boolean,
    hasMoreClubs: Boolean,
    loadMoreUsers: () -> Unit,
    loadMoreClubs: () -> Unit,
    navigate: (String) -> Unit,
) {

    if (users.isNotEmpty()) {
        item {
            Text(
                text = stringResource(R.string.search_users),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )
        }
        items(users) {
            UserCard(
                user = it,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        navigate("feed/users/${it.associationId}/${it.id}")
                    }
            )
        }
        if (hasMoreUsers) {
            item {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.search_more),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { loadMoreUsers() }
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    if (clubs.isNotEmpty()) {
        item {
            Text(
                text = stringResource(R.string.search_clubs),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )
        }
        items(clubs) {
            ClubCard(
                club = it,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        navigate("clubs/${it.id}")
                    }
            )
        }
        if (hasMoreClubs) {
            item {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.search_more),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { loadMoreClubs() }
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

}
