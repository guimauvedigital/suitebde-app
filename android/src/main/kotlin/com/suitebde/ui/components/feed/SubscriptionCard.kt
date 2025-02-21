package com.suitebde.ui.components.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suitebde.R
import com.suitebde.extensions.localizedPrice
import com.suitebde.models.associations.SubscriptionInAssociation
import dev.kaccelero.models.UUID

@Composable
@Suppress("FunctionName")
fun SubscriptionCard(
    subscription: SubscriptionInAssociation,
    onCardClicked: () -> Unit = {},
) {

    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .clickable(onClick = onCardClicked)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(256.dp)
                .height(128.dp)
                .padding(12.dp)
        ) {
            Text(
                text = subscription.name,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.3,
                color = Color.White
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = subscription.price.localizedPrice,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.5,
                    color = Color.White
                )
                Button(
                    onClick = onCardClicked,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    contentPadding = PaddingValues(12.dp),
                ) {
                    Text(stringResource(R.string.feed_subscriptions_more))
                }
            }
        }
    }

}

@Composable
@Suppress("FunctionName")
@Preview
fun SubscriptionCardPreview() {
    SubscriptionCard(
        SubscriptionInAssociation(
            UUID(),
            UUID(),
            "Cotisation pour la scolarité",
            "Cool",
            85.0,
            "1y",
            false
        )
    )
}
