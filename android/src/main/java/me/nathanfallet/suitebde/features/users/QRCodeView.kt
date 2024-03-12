package me.nathanfallet.suitebde.features.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.nathanfallet.suitebde.ui.components.users.QRCodeRootView
import me.nathanfallet.suitebde.viewmodels.users.QRCodeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun QRCodeView(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<QRCodeViewModel>()

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    val user by viewModel.user.collectAsState()
    val qrCodeUrl by viewModel.qrCodeUrl.collectAsState()

    user?.let { unwrappedUser ->
        qrCodeUrl?.let { unwrappedQrCodeUrl ->
            QRCodeRootView(
                user = unwrappedUser,
                qrCodeUrl = unwrappedQrCodeUrl,
                navigateUp = navigateUp,
                modifier = modifier
            )
        }
    } ?: run {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

}
