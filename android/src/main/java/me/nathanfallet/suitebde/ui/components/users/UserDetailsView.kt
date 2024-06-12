package me.nathanfallet.suitebde.ui.components.users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.datetime.Clock
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.models.users.SubscriptionInUser
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.ui.components.navigation.DefaultNavigationBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionName")
fun UserDetailsView(
    user: User,
    subscriptions: List<SubscriptionInUser>,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        stickyHeader {
            DefaultNavigationBar(
                title = user.firstName + " " + user.lastName,
                navigateUp = navigateUp,
                image = {
                    AsyncImage(
                        model = "",
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
                text = stringResource(R.string.users_information),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        // TODO
        item {
            Text(
                text = stringResource(R.string.users_subscriptions),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        items(subscriptions) {
            SubscriptionInUserCard(
                subscription = it,
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
fun UserDetailsViewPreview() {
    UserDetailsView(
        user = User(
            id = "1",
            associationId = "associationId",
            email = "",
            password = null,
            firstName = "John",
            lastName = "Doe",
            superuser = false,
            lastLoginAt = Clock.System.now()
        ),
        subscriptions = emptyList(),
        navigateUp = {},
        modifier = Modifier
    )
}
