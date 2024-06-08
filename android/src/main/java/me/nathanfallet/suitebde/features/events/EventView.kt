package me.nathanfallet.suitebde.features.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.ui.components.AlertCaseDialog
import me.nathanfallet.suitebde.ui.components.DateTimePicker
import me.nathanfallet.suitebde.ui.components.events.EventDetailsView
import me.nathanfallet.suitebde.viewmodels.events.EventViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
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
    val validated by viewModel.validated.collectAsState()

    val isEditing by viewModel.isEditing.collectAsState()
    val alert by viewModel.alert.collectAsState()
    val isEditable by viewModel.isEditable.collectAsState()

    if (isEditing) {
        LazyColumn(modifier) {
            stickyHeader {
                TopAppBar(
                    title = { Text(stringResource(R.string.events_title)) },
                    navigationIcon = {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.app_back)
                            )
                        }
                    },
                    actions = {
                        if (isEditable) Text(
                            text = stringResource(R.string.app_done),
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
                    text = stringResource(R.string.events_information),
                    style = MaterialTheme.typography.titleSmall
                )
            }
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
                            text = stringResource(R.string.events_name),
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
                    placeholder = stringResource(R.string.events_startsAt),
                    selected = startsAt.toLocalDateTime(TimeZone.currentSystemDefault()),
                    onSelected = { viewModel.updateStartsAt(it.toInstant(TimeZone.currentSystemDefault())) }
                )
            }
            item {
                DateTimePicker(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    placeholder = stringResource(R.string.events_endsAt),
                    selected = endsAt.toLocalDateTime(TimeZone.currentSystemDefault()),
                    onSelected = { viewModel.updateEndsAt(it.toInstant(TimeZone.currentSystemDefault())) }
                )
            }
            if (isEditable) item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.events_validated))
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
                    text = stringResource(R.string.events_description),
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
                            text = stringResource(R.string.events_description),
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
                    Text(text = stringResource(R.string.app_save))
                }
            }
        }
        return
    }

    event?.let {
        EventDetailsView(
            event = it,
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
