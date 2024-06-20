package com.suitebde.ui.components.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.suitebde.R
import com.suitebde.extensions.renderedDate
import com.suitebde.models.events.Event
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

@Composable
@Suppress("FunctionName")
fun EventCard(
    event: Event,
    onCardClicked: () -> Unit = {},
) {

    val day: String = DateTimeFormatter.ofPattern("d").format(
        event.startsAt.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    )
    val month: String = DateTimeFormatter.ofPattern("MMM").format(
        event.startsAt.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    )

    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier
            .clickable(onClick = onCardClicked)
            .width(256.dp)
    ) {
        Column {
            Box(
                contentAlignment = Alignment.BottomStart,
            ) {
                AsyncImage(
                    model = event.image ?: "",
                    placeholder = painterResource(R.drawable.default_event_image),
                    error = painterResource(R.drawable.default_event_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(256.dp)
                        .height(128.dp)
                )
                Card(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = day
                        )
                        Text(
                            text = month,
                            color = Color.Gray
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Text(
                    text = event.name,
                    maxLines = 1
                )
                Text(
                    text = event.renderedDate,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }

}

@Composable
@Suppress("FunctionName")
@Preview
fun EventCardPreview() {
    EventCard(
        Event(
            id = UUID(),
            associationId = UUID(),
            name = "Vente de crèpes",
            description = "A cool event",
            image = "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            startsAt = Clock.System.now(),
            endsAt = Clock.System.now(),
            validated = true
        )
    )
}
