package me.nathanfallet.bdeensisa.features.integration

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.views.Picker

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntegrationExecutionView(
    modifier: Modifier = Modifier,
    viewModel: IntegrationExecutionViewModel,
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {

    val context = LocalContext.current

    val challenges by viewModel.getChallenges().observeAsState()
    val challenge by viewModel.getChallenge().observeAsState()
    val filename by viewModel.getFilename().observeAsState()
    val uploading by viewModel.getUploading().observeAsState()
    val error by viewModel.getError().observeAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.imageSelected(uri, context)
        }
    )

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = "Compléter un défi") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                            contentDescription = "Retour"
                        )
                    }
                }
            )
            if (error != null) {
                AlertDialog(
                    onDismissRequest = viewModel::dismissError,
                    title = { Text("Une erreur est survenue !") },
                    text = { Text(error ?: "Erreur inconnue") },
                    confirmButton = {
                        Button(onClick = viewModel::dismissError) {
                            Text("OK")
                        }
                    }
                )
            }
        }
        item {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Défi à compléter",
                style = MaterialTheme.typography.h6
            )
        }
        item {
            Picker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                placeholder = "Sélectionnez...",
                items = challenges?.associate { it.id to it.name } ?: mapOf(),
                selected = challenge ?: "",
                onSelected = viewModel::setChallenge,
            )
        }
        if (filename?.isNotEmpty() == true) {
            item {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    text = filename ?: ""
                )
            }
        }
        item {
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                onClick = {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                    )
                }
            ) {
                Text(text = "Sélectionner une preuve")
            }
        }
        item {
            if (uploading == true) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.createExecution(
                            mainViewModel.getToken().value,
                            navigateUp
                        )
                    },
                    enabled = challenge != null && filename != null
                ) {
                    Text(text = "Proposer")
                }
            }
        }
    }

}
