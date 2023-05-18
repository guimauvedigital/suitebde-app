package me.nathanfallet.bdeensisa.features.shop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.models.TicketConfiguration
import me.nathanfallet.bdeensisa.views.Picker

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShopItemView(
    modifier: Modifier = Modifier,
    viewModel: ShopItemViewModel,
    mainViewModel: MainViewModel
) {

    val user by mainViewModel.getUser().observeAsState()

    val payNow by viewModel.getPayNow().observeAsState()
    val loading by viewModel.getLoading().observeAsState()
    val error by viewModel.getError().observeAsState()
    val success by viewModel.getSuccess().observeAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = viewModel.item.title ?: "Boutique") }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (success != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 4.dp),
                    elevation = 4.dp,
                    backgroundColor = Color(0xFF0BDA51)
                ) {
                    Text(
                        text = success ?: "",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        if (viewModel.item is TicketConfiguration && viewModel.item.userLeft != null && viewModel.item.userLeft!! < 1) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 4.dp),
                    elevation = 4.dp,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Text(
                        text = "Cet élément n'est plus disponible.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        item {
            ShopCard(
                item = viewModel.item,
                detailsEnabled = false,
                cotisant = mainViewModel.getUser().value?.cotisant != null,
            )
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Acheter",
                        fontWeight = FontWeight.Bold
                    )
                    ShopItemPrice(viewModel.item, user?.cotisant != null)
                    if (viewModel.item is TicketConfiguration && viewModel.item.userLeft != null) {
                        Text(
                            text = "${viewModel.item.userLeft} place(s) restante(s)",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Picker(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        placeholder = "Méthode de paiement...",
                        items = mapOf(
                            true to "Lydia",
                            false to "A un membre BDE"
                        ),
                        selected = payNow ?: true,
                        onSelected = viewModel::setPayNow,
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = loading != true,
                        onClick = {
                            viewModel.launchBuy(mainViewModel.getToken().value)
                        }
                    ) {
                        Text(text = "Acheter")
                    }
                    if (error == true) {
                        AlertDialog(
                            onDismissRequest = viewModel::dismissError,
                            title = { Text("Une erreur est survenue !") },
                            text = { Text("Vérifiez que vous êtes bien connecté à internet, que cet élément est encore disponible et que vous ne l'avez pas déjà acheté.") },
                            confirmButton = {
                                Button(onClick = viewModel::dismissError) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}