package me.nathanfallet.suitebde.ui.components.scans

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
import kotlinx.datetime.LocalDate
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.extensions.renderedDateTime
import me.nathanfallet.suitebde.models.scans.ScansForDay
import me.nathanfallet.suitebde.ui.components.navigation.DefaultNavigationBar
import me.nathanfallet.suitebde.ui.components.users.UserCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionName")
fun ScanHistoryRootView(
    scans: List<ScansForDay>,
    loadMoreIfNeeded: (LocalDate) -> Unit,
    navigate: (String) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(modifier) {
        stickyHeader {
            DefaultNavigationBar(
                title = stringResource(R.string.scan_history_title),
                navigateUp = navigateUp,
            )
        }
        items(scans) { day ->
            val title = day.date.renderedDate
            val count = day.scans.map { it.userId }.distinct().count()
            Text(
                text = "$title - $count personne(s)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
            day.scans.forEach { entry ->
                entry.user?.let { user ->
                    UserCard(
                        user = user,
                        customDescription = entry.scannedAt.renderedDateTime,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp)
                            .clickable {
                                navigate("feed/users/${entry.associationId}/${entry.userId}")
                            }
                    )
                }
            }
            loadMoreIfNeeded(day.date)
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun PreviewScanHistoryRootView() {
    ScanHistoryRootView(
        scans = listOf(),
        loadMoreIfNeeded = {},
        navigate = {},
        navigateUp = {}
    )
}
