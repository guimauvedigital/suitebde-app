package me.nathanfallet.suitebde.features.scans

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.extensions.renderedDateTime
import me.nathanfallet.suitebde.viewmodels.scans.ScanHistoryViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
fun ScanHistoryView(
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<ScanHistoryViewModel>()

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    val scans by viewModel.scans.collectAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = {
                    Text(text = "Historique de scan")
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(scans ?: emptyList()) { day ->
            val title = day.date.renderedDate
            val count = day.scans.map { it.userId }.distinct().count()
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "$title - $count personne(s)",
                style = MaterialTheme.typography.titleSmall
            )
            day.scans.forEach { entry ->
                // TODO: Use user card
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .clickable {
                                entry.user?.let {
                                    navigate("feed/users/${entry.associationId}/${entry.userId}")
                                }
                            }
                    ) {
                        Text("${entry.user?.firstName} ${entry.user?.lastName}")
                        Text(
                            text = "Scanné par ${entry.scanner?.firstName} ${entry.scanner?.lastName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = entry.scannedAt.renderedDateTime,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}
