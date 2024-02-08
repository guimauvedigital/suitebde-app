package me.nathanfallet.suitebde.ui.components.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.models.events.Event
import me.nathanfallet.suitebde.ui.components.navigation.DefaultNavigationBar

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
fun FeedRootView(
    events: List<Event>,
    sendNotificationVisible: Boolean,
    navigate: (String) -> Unit,
    oldBeforeView: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
) {

    var isMenuShown by remember { mutableStateOf(false) }

    LazyColumn(modifier) {
        stickyHeader {
            DefaultNavigationBar(
                title = stringResource(R.string.feed_title),
                toolbar = {
                    Box {
                        IconButton(onClick = {
                            isMenuShown = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        DropdownMenu(
                            expanded = isMenuShown,
                            onDismissRequest = {
                                isMenuShown = false
                            }
                        ) {
                            DropdownMenuItem(onClick = {
                                navigate("feed/suggest_event")
                                isMenuShown = false
                            }, text = {
                                Text(stringResource(R.string.feed_events_suggest))
                            })
                            if (sendNotificationVisible) {
                                DropdownMenuItem(onClick = {
                                    navigate("feed/send_notification")
                                    isMenuShown = false
                                }, text = {
                                    Text(stringResource(R.string.feed_notifications_send))
                                })
                            }
                        }
                    }
                    IconButton(onClick = {
                        navigate("feed/settings")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                            contentDescription = stringResource(R.string.settings_title),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        oldBeforeView()

        if (events.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.feed_events),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(events) { event ->
                        EventCard(
                            event = event,
                            onCardClicked = {
                                navigate("feed/events/${event.id}")
                            }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun FeedRootViewPreview() {
    FeedRootView(
        events = listOf(
            Event(
                id = "id",
                associationId = "associationId",
                name = "Vente de crèpes",
                description = "A cool event",
                icon = "https://images.unsplash.com/photo-1637036124732-cb0fab13bb15?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                startsAt = Clock.System.now(),
                endsAt = Clock.System.now(),
                validated = true
            ),
            Event(
                id = "id2",
                associationId = "associationId",
                name = "Assemblée générale",
                description = "A cool event",
                icon = "https://images.unsplash.com/photo-1492538368677-f6e0afe31dcc?q=80&w=1740&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                startsAt = Clock.System.now(),
                endsAt = Clock.System.now(),
                validated = true
            ),
        ),
        sendNotificationVisible = true,
        navigate = {},
        oldBeforeView = {}
    )
}
