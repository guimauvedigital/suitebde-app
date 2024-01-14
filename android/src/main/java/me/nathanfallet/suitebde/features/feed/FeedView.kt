package me.nathanfallet.suitebde.features.feed

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.isGalaShown
import me.nathanfallet.suitebde.extensions.renderedDateTime
import me.nathanfallet.suitebde.features.MainViewModel
import me.nathanfallet.suitebde.features.shop.ShopCard

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    mainViewModel: MainViewModel,
) {

    val context = LocalContext.current

    val viewModel: FeedViewModel = viewModel()

    val user by mainViewModel.getUser().observeAsState()
    val integrationConfiguration by mainViewModel.getIntegrationConfiguration().observeAsState()

    val isNewMenuShown by viewModel.getIsNewMenuShown().observeAsState()
    val events by viewModel.getEvents().observeAsState()
    val topics by viewModel.getTopics().observeAsState()
    val cotisantConfigurations by viewModel.getCotisantConfigurations().observeAsState()
    val ticketConfigurations by viewModel.getTicketConfigurations().observeAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = "Actualité") },
                actions = {
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
                            }, text = {
                                Text("Suggérer un évènement")
                            })
                            if (user?.hasPermission("admin.notifications") == true) {
                                DropdownMenuItem(onClick = {
                                    navigate("feed/send_notification")
                                    viewModel.setIsNewMenuShown(false)
                                }, text = {
                                    Text("Envoyer une notification")
                                })
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
        user?.let {
            if (user?.cotisant == null && cotisantConfigurations?.isNotEmpty() == true) {
                item {
                    Text(
                        text = "Cotisation",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp)
                    )
                }
                items(cotisantConfigurations ?: listOf()) {
                    ShopCard(
                        item = it,
                        detailsEnabled = true,
                        cotisant = false,
                        showDetails = mainViewModel::setSelectedShopItem
                    )
                }
            }
            if (ticketConfigurations?.isNotEmpty() == true) {
                item {
                    Text(
                        text = "Tickets",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp)
                    )
                }
                items(ticketConfigurations ?: listOf()) {
                    ShopCard(
                        item = it,
                        detailsEnabled = true,
                        cotisant = user?.cotisant != null,
                        showDetails = mainViewModel::setSelectedShopItem
                    )
                }
            }
        }
        if (Clock.System.now().isGalaShown) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 4.dp)
                        .clickable {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://bdensisa.org/pages/gala")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                viewModel.getApplication(),
                                browserIntent,
                                null
                            )
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Réservez votre place pour le Gala !"
                        )
                    }
                }
            }
        }
        if (events?.isNotEmpty() == true) {
            item {
                Text(
                    text = "Evènements à venir",
                    style = MaterialTheme.typography.titleSmall,
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
                        }
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(48.dp)
                        )
                        Column {
                            Text(
                                text = it.title ?: "Evènement"
                            )
                            Text(
                                text = it.renderedDate,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
        if (topics?.isNotEmpty() == true) {
            item {
                Text(
                    text = "Affaires en cours",
                    style = MaterialTheme.typography.titleSmall,
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
                        .padding(vertical = 4.dp)
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
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(48.dp)
                        )
                        Column {
                            Text(
                                text = it.title ?: "Affaire"
                            )
                            Text(
                                text = "Ajoutée le ${it.createdAt?.renderedDateTime ?: "?"}",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}
