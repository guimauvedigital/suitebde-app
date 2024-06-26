package com.suitebde.ui.components.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.suitebde.R
import com.suitebde.models.events.Event
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import software.guimauve.ui.components.navigation.DefaultNavigationBar
import software.guimauve.ui.components.navigation.DefaultNavigationBarButton
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionName")
fun EventDetailsView(
    event: Event,
    navigateUp: () -> Unit,
    toggleEditing: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val startDay: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(
        event.startsAt.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    )
    val endDay: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(
        event.endsAt.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    )
    val startTime: String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(
        event.startsAt.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    )
    val endTime: String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(
        event.endsAt.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        stickyHeader {
            DefaultNavigationBar(
                title = event.name,
                navigateUp = navigateUp,
                toolbar = {
                    DefaultNavigationBarButton(toggleEditing, true) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.app_edit),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                image = {
                    AsyncImage(
                        model = event.image ?: "",
                        placeholder = painterResource(R.drawable.default_event_image),
                        error = painterResource(R.drawable.default_event_image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = it
                    )
                }
            )
        }
        item {
            Text(
                text = stringResource(R.string.events_date),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (startDay != endDay) "$startDay - $endDay" else startDay,
                )
                Text(
                    text = if (startTime != endTime || startDay != endDay) "$startTime - $endTime" else startTime,
                    color = Color.Gray
                )
            }
        }
        item {
            Text(
                text = event.description,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier)
        }
    }
}

@Preview
@Composable
@Suppress("FunctionName")
fun EventDetailsViewPreview() {
    EventDetailsView(
        event = Event(
            id = UUID(),
            associationId = UUID(),
            name = "Vente de crèpes",
            description = "A cool event",
            image = "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            startsAt = Clock.System.now(),
            endsAt = Clock.System.now(),
            validated = true
        ),
        navigateUp = {},
        toggleEditing = {}
    )
}
