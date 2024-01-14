package me.nathanfallet.suitebde.features.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.features.MainViewModel
import me.nathanfallet.suitebde.views.AlertCaseDialog
import me.nathanfallet.suitebde.views.DateTimePicker

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventView(
    modifier: Modifier = Modifier,
    viewModel: EventViewModel,
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit,
) {

    val event by viewModel.getEvent().observeAsState()
    val editing by viewModel.isEditing().observeAsState()
    val alert by viewModel.getAlert().observeAsState()

    val title by viewModel.getTitle().observeAsState()
    val start by viewModel.getStart().observeAsState()
    val end by viewModel.getEnd().observeAsState()
    val content by viewModel.getContent().observeAsState()
    val validated by viewModel.isValidated().observeAsState()

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
                    if (viewModel.editable) {
                        Text(
                            text = if (editing == true) "Terminé" else "Modifier",
                            modifier = Modifier
                                .clickable(onClick = viewModel::toggleEdit)
                                .padding(16.dp)
                        )
                    }
                }
            )
            AlertCaseDialog(
                alertCase = alert,
                onDismissRequest = { viewModel.setAlert(null) },
                discardEdit = viewModel::discardEdit
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
        if (editing == true) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    value = title ?: "",
                    onValueChange = viewModel::setTitle,
                    placeholder = {
                        Text(
                            text = "Titre",
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
                    selected = start?.toLocalDateTime(TimeZone.currentSystemDefault()),
                    onSelected = { viewModel.setStart(it.toInstant(TimeZone.currentSystemDefault())) }
                )
            }
            item {
                DateTimePicker(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    placeholder = "Date de fin",
                    selected = end?.toLocalDateTime(TimeZone.currentSystemDefault()),
                    onSelected = { viewModel.setEnd(it.toInstant(TimeZone.currentSystemDefault())) }
                )
            }
            if (viewModel.editable) {
                item {
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
                            checked = validated ?: false,
                            onCheckedChange = viewModel::setValidated,
                        )
                    }
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
                    value = content ?: "",
                    onValueChange = viewModel::setContent,
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
                        viewModel.updateInfo(mainViewModel.getToken().value)
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
                        text = event?.title ?: "Evènement"
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        text = event?.renderedDate ?: "Date",
                        color = Color.Gray
                    )
                }
            }
            event?.content?.let {
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
