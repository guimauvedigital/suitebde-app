package me.nathanfallet.suitebde.features.shop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.features.root.OldRootViewModel
import me.nathanfallet.suitebde.models.ensisa.TicketConfiguration
import me.nathanfallet.suitebde.ui.components.Picker

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShopItemView(
    modifier: Modifier = Modifier,
    viewModel: ShopItemViewModel,
    oldRootViewModel: OldRootViewModel,
    navigateUp: () -> Unit,
) {

    val user by oldRootViewModel.getUser().observeAsState()

    val payNow by viewModel.getPayNow().observeAsState()
    val loading by viewModel.getLoading().observeAsState()
    val error by viewModel.getError().observeAsState()
    val success by viewModel.getSuccess().observeAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = viewModel.item.title ?: "Boutique") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.app_back)
                        )
                    }
                }
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
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0BDA51)
                    )
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
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
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
                cotisant = oldRootViewModel.getUser().value?.cotisant != null,
            )
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp)
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
                    if (viewModel.item.canPayLater) {
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
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = loading != true,
                        onClick = {
                            viewModel.launchBuy(oldRootViewModel.getToken().value)
                        }
                    ) {
                        Text(text = "Acheter")
                    }
                    Text(
                        "Astuce : ajoutez votre adresse mail uha à votre compte Lydia pour simplifier le processus de paiement."
                    )
                    if (error != null) {
                        AlertDialog(
                            onDismissRequest = viewModel::dismissError,
                            title = { Text("Une erreur est survenue !") },
                            text = { Text(error ?: "Erreur inconnue") },
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
