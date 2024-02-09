package me.nathanfallet.suitebde.features.auth

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import me.nathanfallet.suitebde.ui.components.auth.AuthErrorView
import me.nathanfallet.suitebde.ui.components.auth.AuthRootView
import me.nathanfallet.suitebde.viewmodels.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthView(
    onUserLogged: () -> Unit,
    modifier: Modifier = Modifier,
    code: String? = null,
) {

    val context = LocalContext.current

    val viewModel = koinViewModel<AuthViewModel>()

    val error by viewModel.error.collectAsState()

    val loginRegisterClicked = {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(context, browserIntent, null)
    }

    LaunchedEffect(code) {
        code?.let {
            viewModel.authenticate(code, onUserLogged)
        }
    }

    error?.let {
        AuthErrorView(
            error = it,
            tryAgainClicked = {},
            modifier = modifier
        )
    } ?: run {
        AuthRootView(
            loginClicked = loginRegisterClicked,
            registerClicked = loginRegisterClicked,
            modifier = modifier
        )
    }

}
