package me.nathanfallet.suitebde.features.scans

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import me.nathanfallet.suitebde.ui.components.scans.ScanHistoryRootView
import me.nathanfallet.suitebde.viewmodels.scans.ScanHistoryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
@Suppress("FunctionName")
fun ScanHistoryView(
    navigate: (String) -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<ScanHistoryViewModel>()

    LaunchedEffect(Unit) {
        viewModel.onAppear()
    }

    val scans by viewModel.scans.collectAsState()

    ScanHistoryRootView(
        scans = scans ?: emptyList(),
        navigate = navigate,
        navigateUp = navigateUp,
        modifier = modifier,
    )

}
