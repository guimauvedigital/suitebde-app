package me.nathanfallet.bdeensisa.features.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.features.MainViewModel

@Composable
fun EventView(
    modifier: Modifier = Modifier,
    viewModel: EventViewModel,
    mainViewModel: MainViewModel
) {

    val event by viewModel.getEvent().observeAsState()

    Column(modifier) {
        TopAppBar(
            title = { Text(text = "Evènement") }
        )
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = "Informations",
            style = MaterialTheme.typography.h6
        )
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