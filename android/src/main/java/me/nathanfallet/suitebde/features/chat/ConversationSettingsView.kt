package me.nathanfallet.suitebde.features.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jamal.composeprefs.ui.GroupHeader
import com.jamal.composeprefs.ui.PrefsScreen
import com.jamal.composeprefs.ui.prefs.CheckBoxPref
import com.jamal.composeprefs.ui.prefs.TextPref
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.dataStore
import me.nathanfallet.suitebde.features.root.OldRootViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationSettingsView(
    modifier: Modifier = Modifier,
    viewModel: ConversationSettingsViewModel,
    oldRootViewModel: OldRootViewModel,
    navigateUp: () -> Unit,
) {

    val notifications = viewModel.getNotifications().value ?: true
    val members by viewModel.getMembers().observeAsState()

    Column(modifier) {
        TopAppBar(
            title = { Text("Paramètres de la conversation") },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.app_back)
                    )
                }
            }
        )
        PrefsScreen(
            dataStore = LocalContext.current.dataStore
        ) {
            prefsGroup({
                GroupHeader(
                    title = "Notifications"
                )
            }) {
                prefsItem {
                    CheckBoxPref(
                        key = "notifications_conversation_${viewModel.conversation.id}",
                        title = "Activer les notifications",
                        defaultChecked = notifications,
                        onCheckedChange = {
                            viewModel.setNotifications(it)
                            viewModel.updateMembership(oldRootViewModel.getToken().value)
                        }
                    )
                }
            }
            prefsGroup({
                GroupHeader(
                    title = "Dans la conversation"
                )
            }) {
                members?.forEach {
                    prefsItem {
                        TextPref(
                            title = "${it.firstName} ${it.lastName}",
                            summary = it.description
                        )
                    }
                }
            }
        }
    }

}
