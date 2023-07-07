package me.nathanfallet.bdeensisa.extensions

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

fun String.generateQRCode(): Bitmap? {
    return try {
        val encoder = BarcodeEncoder()
        val content = encoder.encode(this, BarcodeFormat.QR_CODE, 512, 512)
        return createBitmap(content, -1, 0)
    } catch (error: Exception) {
        null
    }
}

@Composable
fun String.ChatLogo(backup: String? = null, size: Int = 44, corner: Int = 4) {
    val str = this
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size.dp)
            .background(Color.LightGray, RoundedCornerShape(corner.dp))
    ) {
        Text(
            text = backup?.uppercase() ?: str
                .split(" ")
                .map { (it.firstOrNull() ?: ' ').uppercase() }
                .filter { it in "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" }
                .joinToString(""),
            maxLines = 1,
            fontSize = (size / 3f).sp,
            textAlign = TextAlign.Center
        )
    }
}
