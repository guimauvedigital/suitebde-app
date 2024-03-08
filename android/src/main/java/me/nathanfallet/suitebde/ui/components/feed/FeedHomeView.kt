package me.nathanfallet.suitebde.ui.components.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.events.Event

@Suppress("FunctionName")
fun LazyListScope.FeedHomeView(
    subscriptions: List<SubscriptionInAssociation>,
    events: List<Event>,
    navigate: (String) -> Unit,
) {

    item {
        Text(
            text = stringResource(R.string.qrcode_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        QRCodeCard(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    navigate("feed/qrcode") // TODO
                }
        )
    }

    if (subscriptions.isNotEmpty()) {
        item {
            Text(
                text = stringResource(R.string.feed_subscriptions),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(subscriptions) { subscription ->
                    SubscriptionCard(
                        subscription = subscription,
                        onCardClicked = {
                            navigate("feed/subscriptions/${subscription.id}")
                        }
                    )
                }
            }
        }
    }

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

}
