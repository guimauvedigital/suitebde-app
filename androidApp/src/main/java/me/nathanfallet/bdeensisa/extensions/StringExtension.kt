package me.nathanfallet.bdeensisa.extensions

import android.graphics.Bitmap
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
