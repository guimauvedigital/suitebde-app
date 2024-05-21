package me.nathanfallet.suitebde.features.scans

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.MixedDecoder
import me.nathanfallet.suitebde.R

class ScannerActivity : AppCompatActivity(), DecoratedBarcodeView.TorchListener {

    private lateinit var capture: CaptureManager
    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var switchFlashlightButton: ImageButton
    private lateinit var openGalleryButton: ImageButton
    private var isTorchOn: Boolean = false

    private val openGalleryRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.data?.let { uri -> handleImage(uri) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scanner)
        setTitle(R.string.qrcode_scan_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)
        barcodeScannerView.setTorchListener(this)
        barcodeScannerView.setStatusText("")

        switchFlashlightButton = findViewById(R.id.switch_flashlight)
        openGalleryButton = findViewById(R.id.open_gallery)
        switchFlashlightButton.setOnClickListener {
            switchFlashlight()
        }
        openGalleryButton.setOnClickListener {
            openGallery()
        }

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            switchFlashlightButton.visibility = View.GONE
        }
        capture = CaptureManager(this, barcodeScannerView)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.setShowMissingCameraPermissionDialog(false)
        capture.decode()

        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "scanner")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ScannerActivity")
        }
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    private fun switchFlashlight() {
        if (!isTorchOn) {
            barcodeScannerView.setTorchOn()
        } else {
            barcodeScannerView.setTorchOff()
        }
    }

    override fun onTorchOn() {
        isTorchOn = true
        switchFlashlightButton.setImageResource(R.drawable.ic_baseline_flashlight_on_24)
    }

    override fun onTorchOff() {
        isTorchOn = false
        switchFlashlightButton.setImageResource(R.drawable.ic_baseline_flashlight_off_24)
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        openGalleryRequest.launch(Intent.createChooser(intent, "Ouvrir depuis la galerie"))
    }

    private fun handleImage(uri: Uri) {
        try {
            val image = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, uri)
                ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
            }

            val intArray = IntArray(image.width * image.height)
            image.getPixels(intArray, 0, image.width, 0, 0, image.width, image.height)

            val source = RGBLuminanceSource(image.width, image.height, intArray)
            val reader = MixedDecoder(MultiFormatReader())
            var result = reader.decode(source)
            if (result == null) {
                result = reader.decode(source)
            }

            val intent = CaptureManager.resultIntent(BarcodeResult(result, null), null)
            setResult(RESULT_OK, intent)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scanner, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.scan_history_button -> {
                val url = "bdeensisa://scan_history"
                val intent = CaptureManager.resultIntent(
                    BarcodeResult(
                        Result(
                            url,
                            url.toByteArray(),
                            null,
                            BarcodeFormat.QR_CODE
                        ), null
                    ), null
                )
                setResult(RESULT_OK, intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
