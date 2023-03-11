package me.nathanfallet.bdeensisa.features.account

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.features.scanner.ScannerActivity

@Composable
fun AccountView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    viewModel: AccountViewModel,
    mainViewModel: MainViewModel
) {

    val user by mainViewModel.getUser().observeAsState()
    val qrCode by viewModel.getQrCode().observeAsState()

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

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Mon compte") },
            actions = {
                if (user != null) {
                    if (user?.hasPermission("admin.users.view") == true) {
                        IconButton(onClick = {
                            val options = ScanOptions()
                            options.captureActivity = ScannerActivity::class.java
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            options.setOrientationLocked(false)
                            options.setBeepEnabled(false)
                            options.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN)
                            barcodeLauncher.launch(options)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_qr_code_scanner_24),
                                contentDescription = "Scanner un QR code"
                            )
                        }
                        IconButton(onClick = {
                            navigate("account/users")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_people_24),
                                contentDescription = "Liste des utilisateurs"
                            )
                        }
                    }
                    IconButton(onClick = {
                        navigate("account/edit")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_create_24),
                            contentDescription = "Modifier"
                        )
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (user != null) {
            if (qrCode != null) {
                Card(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 4.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Image(
                            bitmap = qrCode!!.asImageBitmap(),
                            contentDescription = "QR Code",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                            modifier = Modifier
                                .aspectRatio(1f)
                                .fillMaxSize()
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "${user?.firstName} ${user?.lastName}")
                            Text(text = user?.description ?: "")
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (user?.cotisant != null) "Cotisant" else "Non cotisant",
                                color = if (user?.cotisant != null) Color.Green else Color.Red
                            )
                            if (user?.cotisant != null) {
                                Text(text = "Expire : ${user?.cotisant?.expiration?.renderedDate}")
                            }
                        }
                    }
                }
            } else {
                viewModel.generateQrCode(user!!)
                Text("Chargement...")
            }
        } else {
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                onClick = viewModel::launchLogin
            ) {
                Text(text = "Connexion")
            }
        }
    }

}