package me.nathanfallet.suitebde.ui.components.clubs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.ui.components.navigation.DefaultNavigationBar

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
fun ClubsListView(
    myClubs: List<Club>,
    moreClubs: List<Club>,
    loadMoreIfNeeded: (String) -> Unit,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        modifier
    ) {
        stickyHeader {
            DefaultNavigationBar(
                title = stringResource(R.string.clubs_title),
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
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
                    badgeText = if (!it.validated) "EN ATTENTE"
                    else "MEMBRE",
                    badgeColor = if (!it.validated) Color(0xFFFFA500)
                    else Color(0xFF0BDA51),
                    detailsEnabled = true,
                    showDetails = {
                        navigate("clubs/${it.id}")
                    }
                )
                loadMoreIfNeeded(it.id)
            }
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
        items(moreClubs) {
            ClubCard(
                club = it,
                badgeText = null,
                badgeColor = MaterialTheme.colorScheme.primary,
                detailsEnabled = true,
                showDetails = {
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
