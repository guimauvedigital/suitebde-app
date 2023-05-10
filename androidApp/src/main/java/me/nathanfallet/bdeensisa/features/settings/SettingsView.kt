package me.nathanfallet.bdeensisa.features.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jamal.composeprefs.ui.GroupHeader
import com.jamal.composeprefs.ui.PrefsScreen
import com.jamal.composeprefs.ui.prefs.CheckBoxPref
import com.jamal.composeprefs.ui.prefs.TextPref
import me.nathanfallet.bdeensisa.extensions.dataStore
import me.nathanfallet.myappsandroid.compose.myApps

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsView(
    modifier: Modifier = Modifier
) {

    val viewModel: SettingsViewModel = viewModel()

    val eventsNotifications = viewModel.getEventsNotifications().value ?: true

    Column(modifier) {
        TopAppBar(
            title = { Text("Paramètres") }
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
                        key = "notifications_events",
                        title = "Événements",
                        defaultChecked = eventsNotifications,
                        onCheckedChange = viewModel::setEventsNotifications
                    )
                }
            }

            prefsGroup({
                GroupHeader(
                    title = "A propos"
                )
            }) {
                prefsItem {
                    TextPref(
                        title = "Développée avec ❤️ en Kotlin par Nathan Fallet",
                        onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://nathanfallet.me")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                viewModel.getApplication(),
                                browserIntent,
                                null
                            )
                        },
                        enabled = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = "Site du BDE",
                        onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://bdensisa.org")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                viewModel.getApplication(),
                                browserIntent,
                                null
                            )
                        },
                        enabled = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = "Contacter le BDE",
                        onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse("mailto:bde@bdensisa.org")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                viewModel.getApplication(),
                                browserIntent,
                                null
                            )
                        },
                        enabled = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = "Contacter le développeur",
                        onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse("mailto:dev@bdensisa.org")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                viewModel.getApplication(),
                                browserIntent,
                                null
                            )
                        },
                        enabled = true
                    )
                }
                prefsItem {
                    TextPref(
                        title = "GitHub",
                        onClick = {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/NathanFallet/BdeEnsisaMobile")
                            )
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(
                                viewModel.getApplication(),
                                browserIntent,
                                null
                            )
                        },
                        enabled = true
                    )
                }
            }

            myApps()
        }
    }

}
