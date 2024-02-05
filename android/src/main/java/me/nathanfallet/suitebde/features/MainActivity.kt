package me.nathanfallet.suitebde.features

import android.Manifest
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import me.nathanfallet.suitebde.features.root.RootView
import me.nathanfallet.suitebde.services.WebSocketService
import me.nathanfallet.suitebde.ui.theme.SuiteBDETheme

class MainActivity : ComponentActivity() {

    val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        setContent {
            SuiteBDETheme {
                RootView(this)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        WebSocketService.getInstance(this).disconnectWebSocket()
    }

    override fun onResume() {
        super.onResume()
        WebSocketService.getInstance(this).createWebSocket()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}
