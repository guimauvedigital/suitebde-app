package com.suitebde.ui.components.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.suitebde.R
import com.suitebde.extensions.initials
import com.suitebde.models.clubs.Club
import com.suitebde.ui.components.DefaultCard
import dev.kaccelero.models.UUID
import kotlinx.datetime.Clock

@Composable
@Suppress("FunctionName")
fun ClubCard(
    club: Club,
    modifier: Modifier = Modifier,
) {

    DefaultCard(
        image = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = it.background(Color.Gray)
            ) {
                Text(
                    text = club.name.initials,
                    fontSize = MaterialTheme.typography.displaySmall.fontSize
                )
                AsyncImage(
                    model = club.logo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = it
                )
            }
        },
        title = club.name,
        description = stringResource(if (club.usersCount != 1L) R.string.clubs_members else R.string.clubs_member)
            .format(club.usersCount),
        modifier = modifier
    )

}

@Preview
@Composable
@Suppress("FunctionName")
fun ClubCardPreview() {
    ClubCard(
        club = Club(
            id = UUID(),
            associationId = UUID(),
            name = "Club running",
            description = "",
            logo = "https://bdensisa.org/clubs/rev4fkzzd79u7glwk0l1agdoovm3s7yo/uploads/logo%20club%20run.jpeg",
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 12,
            isMember = true
        )
    )
}

@Preview
@Composable
@Suppress("FunctionName")
fun ClubCardPreviewNoIcon() {
    ClubCard(
        club = Club(
            id = UUID(),
            associationId = UUID(),
            name = "Club sans logo",
            description = "",
            logo = null,
            createdAt = Clock.System.now(),
            validated = true,
            usersCount = 12,
            isMember = true
        )
    )
}
