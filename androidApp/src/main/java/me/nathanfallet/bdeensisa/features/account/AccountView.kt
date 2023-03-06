package me.nathanfallet.bdeensisa.features.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.features.MainViewModel

@Composable
fun AccountView(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    mainViewModel: MainViewModel
) {

    val user by mainViewModel.getUser().observeAsState()
    val qrCode by viewModel.getQrCode().observeAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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