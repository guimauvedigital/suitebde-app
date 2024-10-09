package com.suitebde.ui.components.users

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suitebde.R
import com.suitebde.extensions.renderedDate
import com.suitebde.models.associations.SubscriptionInAssociation
import com.suitebde.models.users.SubscriptionInUser
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock
import software.guimauve.ui.theme.DefaultTheme

@Composable
@Suppress("FunctionName")
fun SubscriptionInUserCard(
    subscription: SubscriptionInUser,
    modifier: Modifier = Modifier,
) {

    val isSubscriptionValid = subscription.endsAt >= Clock.System.now()

    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        colors = DefaultTheme.defaultCardColors(),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(16.dp)
            ) {
                Text(
                    text = subscription.subscription?.name ?: "",
                )
                Text(
                    text = stringResource(R.string.users_subscriptions_valid_until)
                        .format(subscription.endsAt.renderedDate),
                    color = Color.Gray
                )
            }

            Icon(
                painter = painterResource(
                    if (isSubscriptionValid) R.drawable.ic_baseline_check_circle_24
                    else R.drawable.ic_baseline_cancel_24
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun SubscriptionInUserCardPreview() {
    SubscriptionInUserCard(
        SubscriptionInUser(
            id = UUID(),
            userId = UUID(),
            subscriptionId = UUID(),
            startsAt = Clock.System.now(),
            endsAt = Clock.System.now(),
            subscription = SubscriptionInAssociation(
                id = UUID(),
                associationId = UUID(),
                name = "Subscription name",
                description = "",
                price = 10.0,
                duration = "1y",
                autoRenewable = false
            )
        )
    )
}
