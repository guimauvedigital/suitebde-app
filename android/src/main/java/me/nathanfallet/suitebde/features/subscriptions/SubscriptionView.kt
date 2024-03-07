package me.nathanfallet.suitebde.features.subscriptions

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import me.nathanfallet.suitebde.ui.components.subscriptions.SubscriptionDetailsView
import me.nathanfallet.suitebde.viewmodels.subscriptions.SubscriptionViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Suppress("FunctionName")
fun SubscriptionView(
    id: String,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    val viewModel = koinViewModel<SubscriptionViewModel>(
        parameters = { parametersOf(id) }
    )

    LaunchedEffect(id) {
        viewModel.onAppear()
    }

    val subscription by viewModel.subscription.collectAsState()
    val url by viewModel.url.collectAsState()

    LaunchedEffect(url) {
        if (url == null) return@LaunchedEffect
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(context, browserIntent, null)
    }

    subscription?.let {
        SubscriptionDetailsView(
            subscription = it,
            buy = {
                viewModel.viewModelScope.coroutineScope.launch {
                    viewModel.checkoutSubscription()
                }
            },
            navigateUp = navigateUp,
            modifier = modifier
        )
    } ?: run {
        CircularProgressIndicator(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }

}
