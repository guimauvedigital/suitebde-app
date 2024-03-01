package me.nathanfallet.suitebde.ui.components.subscriptions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.localizedPrice
import me.nathanfallet.suitebde.models.associations.SubscriptionInAssociation
import me.nathanfallet.suitebde.ui.components.navigation.DefaultNavigationBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionName")
fun SubscriptionDetailsView(
    subscription: SubscriptionInAssociation,
    buy: () -> Unit,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        stickyHeader {
            DefaultNavigationBar(
                title = stringResource(R.string.subscriptions_title),
                navigateUp = navigateUp,
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = subscription.name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = subscription.price.localizedPrice,
                    color = Color.Gray
                )
            }
        }
        item {
            Text(
                text = subscription.description,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Button(
                onClick = buy,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(stringResource(R.string.subscriptions_buy))
            }
        }

        item {
            Spacer(modifier = Modifier)
        }
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun SubscriptionDetailsViewPreview() {
    SubscriptionDetailsView(
        subscription = SubscriptionInAssociation(
            "id",
            "associationId",
            "Cotisation pour la scolarité",
            "Cool",
            85.0,
            "1y",
            false
        ),
        buy = {},
        navigateUp = {}
    )
}
