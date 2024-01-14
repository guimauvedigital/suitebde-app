package me.nathanfallet.suitebde.features.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.jamal.composeprefs.ui.GroupHeader
import com.jamal.composeprefs.ui.PrefsScreen
import com.jamal.composeprefs.ui.prefs.CheckBoxPref
import com.jamal.composeprefs.ui.prefs.TextPref
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.dataStore
import me.nathanfallet.suitebde.features.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationSettingsView(
    modifier: Modifier = Modifier,
    viewModel: ConversationSettingsViewModel,
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit,
) {

    val notifications = viewModel.getNotifications().value ?: true
    val members by viewModel.getMembers().observeAsState()

    Column(modifier) {
        TopAppBar(
            title = { Text("Paramètres de la conversation") },
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                        contentDescription = "Retour"
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
                            viewModel.updateMembership(mainViewModel.getToken().value)
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
