package me.nathanfallet.suitebde.ui.components.clubs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.models.clubs.Club
import me.nathanfallet.suitebde.ui.components.DefaultCard

@Composable
@Suppress("FunctionName")
fun ClubCard(
    club: Club,
    modifier: Modifier = Modifier,
) {

    DefaultCard(
        image = {
            AsyncImage(
                model = club.logo,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = it
            )
        },
        title = club.name,
        description = stringResource(if (club.usersCount != 1L) R.string.clubs_members else R.string.clubs_member)
            .format(club.usersCount),
        modifier = modifier
    )

}
