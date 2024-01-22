package me.nathanfallet.suitebde.features.auth

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.ui.theme.primaryColor
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

    LaunchedEffect(code) {
        code?.let {
            viewModel.authenticate(code, onUserLogged)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding()
            .background(primaryColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(R.string.auth_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.auth_description),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )

        // TODO: Logo

        Button(
            modifier = modifier,
            onClick = {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.url))
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(context, browserIntent, null)
            }
        ) {
            Text(text = stringResource(R.string.auth_button))
        }
    }

}
