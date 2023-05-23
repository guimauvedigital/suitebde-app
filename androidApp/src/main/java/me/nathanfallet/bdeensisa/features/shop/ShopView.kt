package me.nathanfallet.bdeensisa.features.shop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShopView(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {

    val viewModel: ShopViewModel = viewModel()

    val user by mainViewModel.getUser().observeAsState()

    val cotisantConfigurations by viewModel.getCotisantConfigurations().observeAsState()
    val ticketConfigurations by viewModel.getTicketConfigurations().observeAsState()

    LazyColumn(
        modifier
    ) {
        stickyHeader {
            TopAppBar(
                title = {
                    Text(text = "Boutique")
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (user?.cotisant == null) {
            item {
                Text(
                    text = "Cotisation",
                    style = MaterialTheme.typography.h6,
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
        item {
            Text(
                text = "Tickets",
                style = MaterialTheme.typography.h6,
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