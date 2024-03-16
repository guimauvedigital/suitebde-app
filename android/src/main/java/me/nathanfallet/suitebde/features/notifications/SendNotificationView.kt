package me.nathanfallet.suitebde.features.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.ui.components.Picker
import me.nathanfallet.suitebde.viewmodels.notifications.SendNotificationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
fun SendNotificationView(
    modifier: Modifier,
) {

    val viewModel = koinViewModel<SendNotificationViewModel>()

    val topics by viewModel.topics.collectAsState()
    val topic by viewModel.topic.collectAsState()
    val title by viewModel.title.collectAsState()
    val body by viewModel.body.collectAsState()
    val sent by viewModel.sent.collectAsState()
    val error by viewModel.error.collectAsState()

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
            items = topics?.topics ?: emptyMap(),
            selected = topic ?: "",
            onSelected = viewModel::updateTopic,
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = title ?: "",
            onValueChange = viewModel::updateTitle,
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
            onValueChange = viewModel::updateBody,
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
            enabled = topic?.isNotEmpty() == true && title.isNotEmpty() && body.isNotEmpty(),
            onClick = {
                viewModel.viewModelScope.coroutineScope.launch {
                    viewModel.send()
                }
            }
        ) {
            Text(text = "Envoyer")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (sent) AlertDialog(
        onDismissRequest = { viewModel.dismiss() },
        title = { Text("Notification envoyée") },
        confirmButton = {
            Button(onClick = { viewModel.dismiss() }) {
                Text("OK")
            }
        }
    )

}
