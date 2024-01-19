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
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import me.nathanfallet.suitebde.features.root.RootView
import me.nathanfallet.suitebde.services.WebSocketService
import me.nathanfallet.suitebde.ui.theme.SuiteBDETheme
import me.nathanfallet.suitebde.workers.FetchEventsWorker
import java.util.concurrent.TimeUnit

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
        scheduleAppRefresh()

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

    private fun scheduleAppRefresh() {
        PeriodicWorkRequest
            .Builder(
                FetchEventsWorker::class.java,
                1, TimeUnit.HOURS, // repeatInterval (the period cycle)
                15, TimeUnit.MINUTES // flexInterval (the tolerance for when to run)
            )
            .build()
            .also { workRequest ->
                WorkManager.getInstance(this).enqueue(workRequest)
            }
    }

}
