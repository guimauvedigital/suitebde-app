package me.nathanfallet.suitebde.features.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.nathanfallet.suitebde.features.root.OldRootViewModel
import me.nathanfallet.suitebde.ui.components.Picker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendNotificationView(
    modifier: Modifier,
    oldRootViewModel: OldRootViewModel,
) {

    val viewModel: SendNotificationViewModel = viewModel()

    val topic by viewModel.getTopic().observeAsState()
    val title by viewModel.getTitle().observeAsState()
    val body by viewModel.getBody().observeAsState()
    val sent by viewModel.getSent().observeAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopAppBar(
            title = {
                Text("Envoi de notification")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Picker(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = "Sujet",
            items = mapOf(
                "broadcast" to "Général",
                "cotisants" to "Cotisants",
                "events" to "Evènements"
            ),
            selected = topic ?: "",
            onSelected = viewModel::setTopic,
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = title ?: "",
            onValueChange = viewModel::setTitle,
            placeholder = {
                Text(
                    text = "Titre",
                    color = Color.LightGray
                )
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = body ?: "",
            onValueChange = viewModel::setBody,
            placeholder = {
                Text(
                    text = "Contenu",
                    color = Color.LightGray
                )
            }
        )
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            enabled = title?.isNotEmpty() ?: false && body?.isNotEmpty() ?: false,
            onClick = {
                viewModel.send(oldRootViewModel.getToken().value)
            }
        ) {
            Text(text = "Envoyer")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (sent == true) {
            AlertDialog(
                onDismissRequest = viewModel::dismissSent,
                title = { Text("Notification envoyée") },
                confirmButton = {
                    Button(onClick = viewModel::dismissSent) {
                        Text("OK")
                    }
                }
            )
        }
    }

}
