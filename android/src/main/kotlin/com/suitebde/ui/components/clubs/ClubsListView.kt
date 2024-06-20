package com.suitebde.ui.components.clubs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suitebde.R
import com.suitebde.models.clubs.Club
import com.suitebde.ui.components.navigation.DefaultNavigationBar
import dev.kaccelero.models.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionName")
fun ClubsListView(
    myClubs: List<Club>,
    moreClubs: List<Club>,
    loadMoreIfNeeded: (UUID) -> Unit,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(modifier) {
        stickyHeader {
            DefaultNavigationBar(
                title = stringResource(R.string.clubs_title),
            )
        }
        if (myClubs.isNotEmpty()) {
            item {
                Text(
                    text = "Mes clubs",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
            items(myClubs) {
                ClubCard(
                    club = it,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                        .clickable {
                            navigate("clubs/${it.id}")
                        }
                )
                loadMoreIfNeeded(it.id)
            }
            if (moreClubs.isNotEmpty()) {
                item {
                    Text(
                        text = "Autres clubs",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
        items(moreClubs) {
            ClubCard(
                club = it,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .clickable {
                        navigate("clubs/${it.id}")
                    }
            )
            loadMoreIfNeeded(it.id)
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun ClubsListViewPreview() {
    ClubsListView(
        myClubs = listOf(),
        moreClubs = listOf(),
        loadMoreIfNeeded = {},
        navigate = {}
    )
}
