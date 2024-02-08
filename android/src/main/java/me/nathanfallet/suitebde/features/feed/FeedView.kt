package me.nathanfallet.suitebde.features.feed

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.nathanfallet.suitebde.features.root.OldRootViewModel
import me.nathanfallet.suitebde.features.shop.ShopCard
import me.nathanfallet.suitebde.ui.components.feed.FeedRootView
import me.nathanfallet.suitebde.viewmodels.feed.FeedViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
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

    val events by viewModel.events.collectAsState()
    val cotisantConfigurations by oldViewModel.getCotisantConfigurations().observeAsState()
    val ticketConfigurations by oldViewModel.getTicketConfigurations().observeAsState()

    FeedRootView(
        events = events ?: emptyList(),
        sendNotificationVisible = user?.hasPermission("admin.notifications") == true,
        navigate = navigate,
        oldBeforeView = {
            user?.let {
                if (user?.cotisant == null && cotisantConfigurations?.isNotEmpty() == true) {
                    item {
                        Text(
                            text = "Cotisation",
                            style = MaterialTheme.typography.titleMedium,
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
                            style = MaterialTheme.typography.titleMedium,
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
        },
        modifier = modifier,
    )

}
