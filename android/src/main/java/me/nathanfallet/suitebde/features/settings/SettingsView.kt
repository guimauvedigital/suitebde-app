package me.nathanfallet.suitebde.features.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.jamal.composeprefs3.ui.GroupHeader
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.TextPref
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.dataStore
import me.nathanfallet.suitebde.viewmodels.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current
    val viewModel = koinViewModel<SettingsViewModel>()

    val developedWith = arrayOf("❤️", "Kotlin", "Swift", "Nathan Fallet", "Toast.cie")

    Column(modifier) {
        TopAppBar(
            title = { Text(stringResource(R.string.settings_title)) }
        )
        PrefsScreen(
            dataStore = context.dataStore
        ) {
            prefsGroup({
                GroupHeader(stringResource(R.string.settings_logout))
            }) {
                prefsItem {
                    TextPref(
                        title = stringResource(R.string.settings_logout),
                        onClick = {
                            viewModel.viewModelScope.coroutineScope.launch {
                                viewModel.logout()
                            }
                        },
                        enabled = true
                    )
                }
            }

            prefsGroup({
                GroupHeader(stringResource(R.string.settings_about))
            }) {
                prefsItem {
                    TextPref(
                        title = stringResource(R.string.settings_developed_with_love).format(*developedWith),
                        onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://suitebde.com")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                context,
                                browserIntent,
                                null
                            )
                        },
                        enabled = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = stringResource(R.string.settings_contact_us),
                        onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse("mailto:hey@suitebde.com")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                context,
                                browserIntent,
                                null
                            )
                        },
                        enabled = true
                    )
                }
            }

            //myApps() // TODO: Upgrade to material 3
        }
    }

}
