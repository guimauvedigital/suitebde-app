package me.nathanfallet.suitebde.features.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.features.root.OldRootViewModel
import me.nathanfallet.suitebde.features.shop.ShopCard
import me.nathanfallet.suitebde.ui.components.events.EventCard
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FeedView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    oldRootViewModel: OldRootViewModel,
) {

    val oldViewModel = viewModel<OldFeedViewModel>()
    val viewModel = koinViewModel<FeedViewModel>()

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    val user by oldRootViewModel.getUser().observeAsState()

    val isNewMenuShown by oldViewModel.getIsNewMenuShown().observeAsState()
    val events by viewModel.events.collectAsState()
    val cotisantConfigurations by oldViewModel.getCotisantConfigurations().observeAsState()
    val ticketConfigurations by oldViewModel.getTicketConfigurations().observeAsState()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        stickyHeader {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.feed_title))
                },
                actions = {
                    Box {
                        IconButton(onClick = {
                            oldViewModel.setIsNewMenuShown(true)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                                contentDescription = "Nouveau"
                            )
                        }
                        DropdownMenu(
                            expanded = isNewMenuShown == true,
                            onDismissRequest = { oldViewModel.setIsNewMenuShown(false) }
                        ) {
                            DropdownMenuItem(onClick = {
                                navigate("feed/suggest_event")
                                oldViewModel.setIsNewMenuShown(false)
                            }, text = {
                                Text("Suggérer un évènement")
                            })
                            if (user?.hasPermission("admin.notifications") == true) {
                                DropdownMenuItem(onClick = {
                                    navigate("feed/send_notification")
                                    oldViewModel.setIsNewMenuShown(false)
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
                            contentDescription = stringResource(R.string.settings_title)
                        )
                    }
                }
            )
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
                        showDetails = oldRootViewModel::setSelectedShopItem
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
                        showDetails = oldRootViewModel::setSelectedShopItem
                    )
                }
            }
        }
        if (events?.isNotEmpty() == true) {
            item {
                Text(
                    text = stringResource(R.string.feed_events),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
            items(events ?: listOf()) { event ->
                EventCard(
                    event = event,
                    onCardClicked = {
                        navigate("feed/events/${event.id}")
                    }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}
