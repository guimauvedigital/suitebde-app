package me.nathanfallet.suitebde.features.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.ui.components.notifications.SendNotificationRootView
import me.nathanfallet.suitebde.viewmodels.notifications.SendNotificationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun SendNotificationView(
    navigateUp: () -> Unit,
    modifier: Modifier,
) {

    val viewModel = koinViewModel<SendNotificationViewModel>()

    val topics by viewModel.topics.collectAsState()
    val topic by viewModel.topic.collectAsState()
    val title by viewModel.title.collectAsState()
    val body by viewModel.body.collectAsState()
    val sent by viewModel.sent.collectAsState()
    val error by viewModel.error.collectAsState()

    SendNotificationRootView(
        navigateUp = navigateUp,
        topics = topics?.topics ?: emptyMap(),
        topic = topic ?: "",
        updateTopic = viewModel::updateTopic,
        title = title,
        updateTitle = viewModel::updateTitle,
        body = body,
        updateBody = viewModel::updateBody,
        sent = sent,
        isEnabled = topic?.isNotEmpty() == true && title.isNotEmpty() && body.isNotEmpty(),
        send = {
            viewModel.viewModelScope.coroutineScope.launch {
                viewModel.send()
            }
        },
        dismiss = viewModel::dismiss,
        modifier = modifier
    )

}
