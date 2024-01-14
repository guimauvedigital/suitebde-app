package me.nathanfallet.suitebde.features.account

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.features.MainViewModel
import me.nathanfallet.suitebde.features.scanner.ScannerActivity
import me.nathanfallet.suitebde.models.NFCMode

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AccountView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    viewModel: AccountViewModel,
    mainViewModel: MainViewModel,
) {

    val user by mainViewModel.getUser().observeAsState()
    val nfcMode by mainViewModel.getNFCMode().observeAsState()

    val qrCode by viewModel.getQrCode().observeAsState()
    val tickets by viewModel.getTickets().observeAsState()

    val context = LocalContext.current
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        result.contents?.let { contents ->
            Uri.parse(contents)?.also {
                if (it.scheme != "bdeensisa") {
                    Toast.makeText(context, "QR Code invalide !", Toast.LENGTH_SHORT).show()
                    return@let
                }
                if (it.host == "scan_history") {
                    navigate("account/scan_history")
                    return@let
                }
                mainViewModel.onOpenURL(it)
            } ?: run {
                Toast.makeText(context, "QR Code invalide !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LazyColumn(
        modifier
    ) {
        stickyHeader {
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
            if (nfcMode == NFCMode.UPDATE) {
                AlertDialog(
                    onDismissRequest = { mainViewModel.setNFCMode(NFCMode.READ) },
                    title = { Text("Veuillez approcher votre carte étudiante pour l'enregistrer") },
                    confirmButton = {
                        Button(onClick = {
                            mainViewModel.setNFCMode(NFCMode.READ)
                        }) {
                            Text("Annuler")
                        }
                    }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            user?.let { user ->
                if (user.nfcIdentifier == null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 4.dp)
                            .clickable(onClick = { mainViewModel.setNFCMode(NFCMode.UPDATE) }),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Carte étudiante non enregistrée, cliquez pour l'ajouter",
                            color = Color.White,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                }
                qrCode?.let { qrCode ->
                    Card(
                        modifier = Modifier
                            .widthIn(max = 400.dp)
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Image(
                                bitmap = qrCode.asImageBitmap(),
                                contentDescription = "QR Code",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .fillMaxSize()
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "${user.firstName} ${user.lastName}")
                                Text(
                                    text = user.description,
                                    color = Color.Gray
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (user.cotisant != null) "Cotisant" else "Non cotisant",
                                    color = if (user.cotisant != null) Color.Green else Color.Red
                                )
                                if (user.cotisant != null) {
                                    Text(text = "Expire : ${user.cotisant?.expiration?.renderedDate}")
                                }
                            }
                        }
                    }
                } ?: run {
                    viewModel.generateQrCode(user)
                    Text("Chargement...")
                }
            } ?: run {
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
        if (tickets?.isNotEmpty() == true) {
            item {
                Text(
                    text = "Tickets",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
            items(tickets ?: listOf()) { ticket ->
                Card(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Text(
                                    text = ticket.event?.title ?: ""
                                )
                            }
                            Text(
                                text = if (ticket.paid != null) "PAYÉ" else "NON PAYÉ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                modifier = Modifier
                                    .background(
                                        if (ticket.paid != null) Color(0xFF0BDA51)
                                        else MaterialTheme.colorScheme.primary,
                                        MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .clickable {
                                        if (ticket.paid == null) {
                                            viewModel.launchPayment(
                                                mainViewModel.getToken().value,
                                                "tickets",
                                                ticket.configurationId,
                                                ticket.id
                                            )
                                        }
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = ticket.event?.content ?: "",
                            maxLines = 5
                        )
                    }
                }
            }
        }
    }

}
