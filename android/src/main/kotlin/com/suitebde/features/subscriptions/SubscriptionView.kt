package com.suitebde.features.subscriptions

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import com.suitebde.ui.components.subscriptions.SubscriptionDetailsView
import com.suitebde.viewmodels.subscriptions.SubscriptionViewModel
import dev.kaccelero.models.UUID
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Suppress("FunctionName")
fun SubscriptionView(
    id: UUID,
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

}
