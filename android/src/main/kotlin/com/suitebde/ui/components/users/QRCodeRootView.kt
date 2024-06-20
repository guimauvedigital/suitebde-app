package com.suitebde.ui.components.users

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suitebde.R
import com.suitebde.extensions.generateQRCode
import com.suitebde.models.users.User
import com.suitebde.ui.components.navigation.DefaultNavigationBar
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock

@Composable
@Suppress("FunctionName")
fun QRCodeRootView(
    user: User,
    qrCodeUrl: String,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var image by remember { mutableStateOf(null as Bitmap?) }

    LaunchedEffect(Unit) {
        image = qrCodeUrl.generateQRCode()
    }

    Column {
        DefaultNavigationBar(
            title = stringResource(R.string.qrcode_title),
            navigateUp = navigateUp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize().padding(16.dp)
        ) {
            Card(
                elevation = CardDefaults.elevatedCardElevation(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Suite BDE",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "BDE X",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Name",
                                color = Color.Gray
                            )
                            Text("${user.firstName} ${user.lastName}")
                        }
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Valid until",
                                color = Color.Gray
                            )
                            Text("???")
                        }
                    }
                    image?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier
                                .aspectRatio(1f)
                                .fillMaxSize()
                        )
                    } ?: run {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun QRCodeRootViewPreview() {
    QRCodeRootView(
        user = User(
            id = UUID(),
            associationId = UUID(),
            email = "",
            password = null,
            firstName = "John",
            lastName = "Doe",
            superuser = false,
            lastLoginAt = Clock.System.now()
        ),
        qrCodeUrl = "",
        navigateUp = {}
    )
}
