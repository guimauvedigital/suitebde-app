package me.nathanfallet.suitebde.features.scanner

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.extensions.renderedDateTime
import me.nathanfallet.suitebde.extensions.scanIcon
import me.nathanfallet.suitebde.features.root.RootViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScanHistoryView(
    modifier: Modifier = Modifier,
    viewModel: ScanHistoryViewModel,
    rootViewModel: RootViewModel,
) {

    val grouped by viewModel.getGrouped().observeAsState()

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
        items(grouped?.keys?.toList() ?: listOf()) { key ->
            val elements = grouped?.get(key) ?: listOf()
            val title = elements.firstOrNull()?.event?.title
                ?: elements.firstOrNull()?.scannedAt?.renderedDate
            val count = elements.map { it.userId }.distinct().count()
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "$title - $count personne(s)",
                style = MaterialTheme.typography.titleSmall
            )
            elements.forEach { entry ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .height(44.dp),
                        painter = painterResource(id = entry.type.scanIcon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                    Column(
                        modifier = Modifier
                            .clickable {
                                entry.user?.let { rootViewModel.setSelectedUser(it) }
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
