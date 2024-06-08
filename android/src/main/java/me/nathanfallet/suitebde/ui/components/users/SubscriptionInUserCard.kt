package me.nathanfallet.suitebde.ui.components.users

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
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.models.users.SubscriptionInUser

@Composable
@Suppress("FunctionName")
fun SubscriptionInUserCard(
    subscription: SubscriptionInUser,
    modifier: Modifier = Modifier,
) {

    val isSubscriptionValid = subscription.endsAt >= Clock.System.now()

    Card(
        elevation = CardDefaults.elevatedCardElevation(),
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
            id = "subscriptionId",
            userId = "userId",
            subscriptionId = "subscriptionId",
            startsAt = Clock.System.now(),
            endsAt = Clock.System.now(),
            subscription = SubscriptionInAssociation(
                id = "subscriptionId",
                associationId = "associationId",
                name = "Subscription name",
                description = "",
                price = 10.0,
                duration = "1y",
                autoRenewable = false
            )
        )
    )
}
