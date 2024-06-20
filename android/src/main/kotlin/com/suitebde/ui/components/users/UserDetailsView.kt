package com.suitebde.ui.components.users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
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
import com.suitebde.R
import com.suitebde.models.users.SubscriptionInUser
import com.suitebde.models.users.User
import com.suitebde.ui.components.feed.QRCodeCard
import com.suitebde.ui.components.navigation.DefaultNavigationBar
import com.suitebde.ui.components.navigation.DefaultNavigationBarButton
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("FunctionName")
fun UserDetailsView(
    user: User,
    isCurrentUser: Boolean,
    subscriptions: List<SubscriptionInUser>,
    toggleEditing: () -> Unit,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    navigateUp: (() -> Unit)? = null,
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        stickyHeader {
            DefaultNavigationBar(
                title = user.firstName + " " + user.lastName,
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
        if (isCurrentUser) {
            item {
                Text(
                    text = stringResource(R.string.qrcode_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                QRCodeCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable { navigate("account/qrcode") }
                )
            }
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
            id = UUID(),
            associationId = UUID(),
            email = "",
            password = null,
            firstName = "John",
            lastName = "Doe",
            superuser = false,
            lastLoginAt = Clock.System.now()
        ),
        isCurrentUser = true,
        subscriptions = emptyList(),
        navigateUp = {},
        toggleEditing = {},
        navigate = {},
        modifier = Modifier
    )
}
