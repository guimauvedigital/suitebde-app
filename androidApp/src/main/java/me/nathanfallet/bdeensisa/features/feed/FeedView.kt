package me.nathanfallet.bdeensisa.features.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.extensions.formatted

@Composable
fun FeedView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit
) {

    val viewModel: FeedViewModel = viewModel()

    val events by viewModel.getEvents().observeAsState()
    val topics by viewModel.getTopics().observeAsState()

    LazyColumn(
        modifier
    ) {
        item {
            TopAppBar(
                title = {
                    Text(text = "Actualité")
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                text = "Evènements à venir",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(events ?: listOf()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    contentDescription = "Evènement",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(48.dp)
                )
                Column {
                    Text(
                        text = it.title ?: "Evènement",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Du ${it.start?.formatted ?: "?"}")
                    Text(text = "Au ${it.end?.formatted ?: "?"}")
                }
            }
        }
        item {
            Text(
                text = "Affaires en cours",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(topics ?: listOf()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_business_24),
                    contentDescription = "Affaire",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(48.dp)
                )
                Column {
                    Text(
                        text = it.title ?: "Affaire",
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Ajoutée le ${it.createdAt?.formatted ?: "?"}")
                }
            }
        }
    }

}