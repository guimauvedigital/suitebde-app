package com.suitebde.ui.components.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.suitebde.extensions.initials
import com.suitebde.models.users.User
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock
import software.guimauve.ui.components.DefaultCard

@Composable
@Suppress("FunctionName")
fun UserCard(
    user: User,
    modifier: Modifier = Modifier,
    customDescription: String? = null,
) {

    val fullName = "${user.firstName} ${user.lastName}"

    DefaultCard(
        image = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = it.background(Color.Gray)
            ) {
                Text(
                    text = fullName.initials,
                    fontSize = MaterialTheme.typography.displaySmall.fontSize
                )
                AsyncImage(
                    model = "",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = it
                )
            }
        },
        title = fullName,
        description = customDescription ?: "",
        modifier = modifier
    )

}

@Preview
@Composable
@Suppress("FunctionName")
fun UserCardPreview() {
    UserCard(
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
        customDescription = "Some custom text"
    )
}
