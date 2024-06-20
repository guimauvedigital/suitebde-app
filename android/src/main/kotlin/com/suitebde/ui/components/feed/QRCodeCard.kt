package com.suitebde.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

val qrcode = "https://suitebde.com".generateQRCode()

@Composable
@Suppress("FunctionName")
fun QRCodeCard(
    modifier: Modifier = Modifier,
) {

    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                bitmap = qrcode!!.asImageBitmap(),
                contentDescription = "QR Code",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
            )

            Text(
                text = stringResource(R.string.qrcode_view),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun QRCodeCardPreview() {
    QRCodeCard()
}
