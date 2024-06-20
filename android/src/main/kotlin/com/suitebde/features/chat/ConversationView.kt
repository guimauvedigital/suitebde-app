package com.suitebde.features.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.suitebde.R
import com.suitebde.features.root.OldRootViewModel
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationView(
    modifier: Modifier = Modifier,
    viewModel: ConversationViewModel,
    oldRootViewModel: OldRootViewModel,
    navigate: (String) -> Unit,
    navigateUp: () -> Unit,
) {

    val messages by viewModel.getMessages().observeAsState()
    val sendingMessages by viewModel.getSendingMessages().observeAsState()
    val typingMessage by viewModel.getTypingMessage().observeAsState()

    Column(modifier) {
        TopAppBar(
            title = {
                Text(text = viewModel.conversation.name)
            },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.app_back)
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    navigate("chat/conversation/settings")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                        contentDescription = "Paramètres"
                    )
                }
            }
        )
        LazyColumn(
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            items(sendingMessages?.reversed() ?: listOf()) { message ->
                MessageView(
                    message = message,
                    isHeaderShown = false,
                    viewedBy = oldRootViewModel.getUser().value,
                    sending = true
                )
            }
            items(messages ?: listOf()) { message ->
                val previousMessage = messages?.firstOrNull {
                    (it.createdAt ?: Clock.System.now()) < (message.createdAt ?: Clock.System.now())
                }
                MessageView(
                    message = message,
                    isHeaderShown = previousMessage?.type == "system" || message.userId != previousMessage?.userId,
                    viewedBy = oldRootViewModel.getUser().value
                )
                viewModel.loadMore(
                    oldRootViewModel.getToken().value,
                    message.id
                )
                if (
                    (message.createdAt ?: Clock.System.now()) >
                    (viewModel.conversation.membership?.lastRead ?: Clock.System.now())
                ) {
                    viewModel.markAsRead(oldRootViewModel.getToken().value)
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                placeholder = {
                    Text("Ecrivez un message...")
                },
                value = typingMessage ?: "",
                onValueChange = viewModel::setTypingMessage,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions {
                    viewModel.sendMessage(
                        oldRootViewModel.getToken().value,
                        oldRootViewModel.getUser().value
                    )
                },
            )
            IconButton(
                onClick = {
                    viewModel.sendMessage(
                        oldRootViewModel.getToken().value,
                        oldRootViewModel.getUser().value
                    )
                },
                enabled = typingMessage?.isNotBlank() == true
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_send_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    contentDescription = "Envoyer"
                )
            }
        }
    }

}
