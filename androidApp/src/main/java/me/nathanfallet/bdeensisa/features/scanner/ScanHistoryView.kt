package me.nathanfallet.bdeensisa.features.scanner

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.extensions.renderedDateTime
import me.nathanfallet.bdeensisa.extensions.scanIcon
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScanHistoryView(
    modifier: Modifier = Modifier,
    viewModel: ScanHistoryViewModel,
    mainViewModel: MainViewModel
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
                style = MaterialTheme.typography.h6
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                    )
                    Column(
                        modifier = Modifier
                            .clickable {
                                entry.user?.let { mainViewModel.setSelectedUser(it) }
                            }
                    ) {
                        Text("${entry.user?.firstName} ${entry.user?.lastName}")
                        Text(
                            text = "Scanné par ${entry.scanner?.firstName} ${entry.scanner?.lastName}",
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            text = entry.scannedAt.renderedDateTime,
                            style = MaterialTheme.typography.body2
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