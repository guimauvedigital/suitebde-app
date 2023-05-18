package me.nathanfallet.bdeensisa.features.account

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.features.scanner.ScannerActivity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit,
    viewModel: AccountViewModel,
    mainViewModel: MainViewModel
) {

    val user by mainViewModel.getUser().observeAsState()
    val qrCode by viewModel.getQrCode().observeAsState()
    val tickets by viewModel.getTickets().observeAsState()

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
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
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
                            horizontalAlignment = Alignment.CenterHorizontally,
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
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "${user?.firstName} ${user?.lastName}")
                                Text(text = user?.description ?: "")
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth()
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
        if (tickets?.isNotEmpty() == true) {
            item {
                Text(
                    text = "Tickets",
                    style = MaterialTheme.typography.h6,
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
                        .padding(vertical = 4.dp),
                    elevation = 4.dp
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
                                    text = ticket.event?.title ?: "",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = if (ticket.paid != null) "Payé" else "Non payé",
                                style = MaterialTheme.typography.caption,
                                color = Color.White,
                                modifier = Modifier
                                    .background(
                                        if (ticket.paid != null) Color(0xFF0BDA51)
                                        else MaterialTheme.colors.primary,
                                        MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
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