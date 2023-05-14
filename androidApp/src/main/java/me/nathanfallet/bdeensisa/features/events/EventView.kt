package me.nathanfallet.bdeensisa.features.events

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.views.DateTimePicker

@Composable
fun EventView(
    modifier: Modifier = Modifier,
    viewModel: EventViewModel,
    mainViewModel: MainViewModel
) {

    val event by viewModel.getEvent().observeAsState()
    val editing by viewModel.isEditing().observeAsState()

    val title by viewModel.getTitle().observeAsState()
    val start by viewModel.getStart().observeAsState()
    val end by viewModel.getEnd().observeAsState()
    val content by viewModel.getContent().observeAsState()
    val validated by viewModel.isValidated().observeAsState()

    Column(modifier) {
        TopAppBar(
            title = { Text(text = "Evènement") },
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
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = "Informations",
            style = MaterialTheme.typography.h6
        )
        if (editing == true) {
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
            DateTimePicker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                placeholder = "Date de début",
                selected = start?.toLocalDateTime(TimeZone.currentSystemDefault()),
                onSelected = { viewModel.setStart(it.toInstant(TimeZone.currentSystemDefault())) }
            )
            DateTimePicker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                placeholder = "Date de fin",
                selected = end?.toLocalDateTime(TimeZone.currentSystemDefault()),
                onSelected = { viewModel.setEnd(it.toInstant(TimeZone.currentSystemDefault())) }
            )
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
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Contenu de l'évènement",
                style = MaterialTheme.typography.h6
            )
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
        } else {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = event?.title ?: "Evènement",
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = event?.renderedDate ?: "Date"
            )
            event?.content?.let {
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