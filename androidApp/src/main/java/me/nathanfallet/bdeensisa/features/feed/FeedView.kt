package me.nathanfallet.bdeensisa.features.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import me.nathanfallet.bdeensisa.extensions.renderedDateTime
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    mainViewModel: MainViewModel
) {

    val viewModel: FeedViewModel = viewModel()

    val user by mainViewModel.getUser().observeAsState()

    val isNewMenuShown by viewModel.getIsNewMenuShown().observeAsState()
    val events by viewModel.getEvents().observeAsState()
    val topics by viewModel.getTopics().observeAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = "Actualité") },
                actions = {
                    if (user?.hasPermissions == true) {
                        Box {
                            IconButton(onClick = {
                                viewModel.setIsNewMenuShown(true)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                                    contentDescription = "Nouveau"
                                )
                            }
                            DropdownMenu(
                                expanded = isNewMenuShown == true,
                                onDismissRequest = { viewModel.setIsNewMenuShown(false) }
                            ) {
                                DropdownMenuItem(onClick = {
                                    navigate("feed/suggest_event")
                                    viewModel.setIsNewMenuShown(false)
                                }) {
                                    Text("Suggérer un évènement")
                                }
                                if (user?.hasPermission("admin.notifications") == true) {
                                    DropdownMenuItem(onClick = {
                                        navigate("feed/send_notification")
                                        viewModel.setIsNewMenuShown(false)
                                    }) {
                                        Text("Envoyer une notification")
                                    }
                                }
                            }
                        }
                    }
                    IconButton(onClick = {
                        navigate("feed/settings")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                            contentDescription = "Paramètres"
                        )
                    }
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp)
                    .clickable {
                        mainViewModel.setSelectedEvent(it)
                    },
                elevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
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
                        Text(text = it.renderedDate)
                    }
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
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
                        Text(text = "Ajoutée le ${it.createdAt?.renderedDateTime ?: "?"}")
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}