package me.nathanfallet.bdeensisa.features.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nathanfallet.bdeensisa.models.CalendarEvent

@Composable
fun CalendarEventView(
    event: CalendarEvent,
    day: Instant
) {

    val title = event.title ?: "Événement"
    val description = event.content ?: ""
    val start = event.start ?: Clock.System.now()
    val end = event.end ?: Clock.System.now()
    val startLocal = start.toLocalDateTime(TimeZone.currentSystemDefault())
    val endLocal = end.toLocalDateTime(TimeZone.currentSystemDefault())
    val dayLocal = day.toLocalDateTime(TimeZone.currentSystemDefault())
    val background =
        if (event.type == CalendarEvent.CalendarEventType.USER_COURSE) Color(0xFF0771DD)
        else Color(0xFFF5CC04)

    val height: Int
    val yOffset: Int

    if (startLocal.date == endLocal.date) {
        // Starts and ends the same day
        height = (44 * (end - start).inWholeMinutes.coerceAtLeast(60).toDouble() / 60).toInt()
        yOffset = (44 * (startLocal.hour * 60 + startLocal.minute).toDouble() / 60).toInt()
    } else {
        // Ends on a different day
        if (startLocal.date == dayLocal.date) {
            // Starts today
            yOffset = (44 * (startLocal.hour * 60 + startLocal.minute).toDouble() / 60).toInt()
            height = 44 * 24 - yOffset
        } else if (endLocal.date == dayLocal.date) {
            // Ends today
            yOffset = 0
            height = (44 * (endLocal.hour * 60 + endLocal.minute).toDouble() / 60).toInt()
        } else {
            // Is all the day
            yOffset = 0
            height = 44 * 24
        }
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .padding(start = 38.dp)
            .padding(end = 2.dp)
            .offset(y = (11 + yOffset).dp),
        backgroundColor = background.copy(alpha = 0.5f),
        elevation = 0.dp // Else, with transparent background color, it looks weird
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height((height - 18).dp) // -16 because padding is included in height
        ) {
            Text(
                text = title,
                fontSize = 13.sp
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }

}
