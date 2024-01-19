package me.nathanfallet.suitebde.features.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.ui.components.AlertCaseDialog
import me.nathanfallet.suitebde.ui.components.DateTimePicker
import me.nathanfallet.suitebde.viewmodels.events.EventViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventView(
    id: String?,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<EventViewModel>(
        parameters = { parametersOf(id) }
    )

    LaunchedEffect(id) {
        viewModel.onAppear()
    }

    val event by viewModel.event.collectAsState()

    val name by viewModel.name.collectAsState()
    val description by viewModel.description.collectAsState()
    val startsAt by viewModel.startsAt.collectAsState()
    val endsAt by viewModel.endsAt.collectAsState()
    val renderedDateRange by viewModel.renderedDateRange.collectAsState()
    val validated by viewModel.validated.collectAsState()

    val isEditing by viewModel.isEditing.collectAsState()
    val alert by viewModel.alert.collectAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = "Evènement") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    if (viewModel.isEditable) Text(
                        text = if (isEditing) "Terminé" else "Modifier",
                        modifier = Modifier
                            .clickable(onClick = viewModel::toggleEditing)
                            .padding(16.dp)
                    )
                }
            )
            AlertCaseDialog(
                alertCase = alert,
                onDismissRequest = { viewModel.setAlert(null) },
                discardEdit = viewModel::discardEditingFromAlert
            )
        }
        item {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Informations",
                style = MaterialTheme.typography.titleSmall
            )
        }
        if (isEditing) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    value = name,
                    onValueChange = viewModel::updateName,
                    placeholder = {
                        Text(
                            text = "Name",
                            color = Color.LightGray
                        )
                    }
                )
            }
            item {
                DateTimePicker(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    placeholder = "Date de début",
                    selected = startsAt.toLocalDateTime(TimeZone.currentSystemDefault()),
                    onSelected = { viewModel.updateStartsAt(it.toInstant(TimeZone.currentSystemDefault())) }
                )
            }
            item {
                DateTimePicker(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    placeholder = "Date de fin",
                    selected = endsAt.toLocalDateTime(TimeZone.currentSystemDefault()),
                    onSelected = { viewModel.updateEndsAt(it.toInstant(TimeZone.currentSystemDefault())) }
                )
            }
            if (viewModel.isEditable) item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Evènement validé"
                    )
                    Switch(
                        checked = validated,
                        onCheckedChange = viewModel::updateValidated,
                    )
                }
            }
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = "Contenu de l'évènement",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .height(120.dp)
                        .fillMaxWidth(),
                    value = description,
                    onValueChange = viewModel::updateDescription,
                    placeholder = {
                        Text(
                            text = "Contenu de l'évènement",
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
                        viewModel.viewModelScope.coroutineScope.launch {
                            viewModel.saveChanges()
                        }
                    }
                ) {
                    Text(text = "Enregistrer")
                }
            }
        } else {
            item {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        text = event?.name ?: "Evènement"
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        text = renderedDateRange ?: "Date",
                        color = Color.Gray
                    )
                }
            }
            event?.description?.let {
                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        text = it
                    )
                }
            }
        }
    }

}
