package me.nathanfallet.bdeensisa.features.manage

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.zxing.client.android.Intents
import com.jamal.composeprefs.ui.GroupHeader
import com.jamal.composeprefs.ui.PrefsScreen
import com.jamal.composeprefs.ui.prefs.TextPref
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import me.nathanfallet.bdeensisa.extensions.dataStore
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.features.scanner.ScannerActivity

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    mainViewModel: MainViewModel
) {

    val viewModel: ManageViewModel = viewModel()

    val user by mainViewModel.getUser().observeAsState()

    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        result.contents?.let { contents ->
            Uri.parse(contents)?.also {
                if (it.scheme == "bdeensisa") {
                    mainViewModel.onOpenURL(it)
                } else {
                    Toast.makeText(context, "QR Code invalide !", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(context, "QR Code invalide !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier) {
        TopAppBar(
            title = { Text(text = "Gestion") }
        )
        PrefsScreen(
            dataStore = LocalContext.current.dataStore
        ) {
            prefsGroup({
                GroupHeader(
                    title = "Scan"
                )
            }) {
                prefsItem {
                    TextPref(
                        title = "Scanner un QR Code",
                        onClick = {
                            val options = ScanOptions()
                            options.captureActivity = ScannerActivity::class.java
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            options.setOrientationLocked(false)
                            options.setBeepEnabled(false)
                            options.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN)
                            barcodeLauncher.launch(options)
                        },
                        enabled = true
                    )
                }
            }
            if (user?.hasPermission("admin.users.view") == true) {
                prefsGroup({
                    GroupHeader(
                        title = "Utilisateurs"
                    )
                }) {
                    prefsItem {
                        TextPref(
                            title = "Liste des utilisateurs",
                            onClick = {
                                navigate("manage/users")
                            },
                            enabled = true
                        )
                    }
                }
            }
            if (user?.hasPermission("admin.notifications") == true) {
                prefsGroup({
                    GroupHeader(
                        title = "Notifications"
                    )
                }) {
                    prefsItem {
                        TextPref(
                            title = "Envoyer une notification",
                            onClick = {
                                navigate("manage/send_notification")
                            },
                            enabled = true
                        )
                    }
                }
            }
        }
    }

}
