package me.nathanfallet.bdeensisa.features.integration

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntegrationCreateView(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit
) {

    val viewModel: IntegrationCreateViewModel = viewModel()

    val name by viewModel.getName().observeAsState()
    val description by viewModel.getDescription().observeAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = "Créer une équipe") },
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
        }
        item {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Informations",
                style = MaterialTheme.typography.h6
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                value = name ?: "",
                onValueChange = viewModel::setName,
                placeholder = {
                    Text(
                        text = "Nom de l'équipe",
                        color = Color.LightGray
                    )
                }
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .height(120.dp)
                    .fillMaxWidth(),
                value = description ?: "",
                onValueChange = viewModel::setDescription,
                placeholder = {
                    Text(
                        text = "Description de l'équipe",
                        color = Color.LightGray
                    )
                }
            )
        }
        item {
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.createTeam(
                        mainViewModel.getToken().value,
                        navigateUp
                    )
                }
            ) {
                Text(text = "Créer")
            }
        }
    }

}
