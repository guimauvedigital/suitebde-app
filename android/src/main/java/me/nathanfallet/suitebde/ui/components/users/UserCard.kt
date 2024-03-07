package me.nathanfallet.suitebde.ui.components.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import me.nathanfallet.suitebde.extensions.initials
import me.nathanfallet.suitebde.models.users.User
import me.nathanfallet.suitebde.ui.components.DefaultCard

@Composable
@Suppress("FunctionName")
fun UserCard(
    user: User,
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
    )

}

@Preview
@Composable
@Suppress("FunctionName")
fun UserCardPreview() {
    UserCard(
        user = User(
            id = "1",
            associationId = "associationId",
            email = "",
            password = null,
            firstName = "John",
            lastName = "Doe",
            superuser = false,
        ),
        customDescription = "Some custom text"
    )
}
