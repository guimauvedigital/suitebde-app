package me.nathanfallet.suitebde.ui.components.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.extensions.renderedDateTime
import me.nathanfallet.suitebde.extensions.renderedTime
import me.nathanfallet.suitebde.models.events.Event

@Composable
fun EventCard(
    event: Event,
    onCardClicked: () -> Unit = {},
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onCardClicked)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                contentDescription = "Evènement",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(48.dp)
            )
            Column {
                Text(
                    text = event.name
                )
                Text(
                    text = renderDate(event),
                    color = Color.Gray
                )
            }
        }
    }

}

fun renderDate(event: Event): String {
    val start = event.startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
    val end = event.endsAt.toLocalDateTime(TimeZone.currentSystemDefault())

    // TODO: i18n
    // Check if same day
    return if (start.dayOfYear == end.dayOfYear && start.year == end.year) {
        "${start.renderedDate} de ${start.renderedTime} à ${end.renderedTime}"
    } else {
        "Du ${start.renderedDateTime} au ${end.renderedDateTime}"
    }
}
