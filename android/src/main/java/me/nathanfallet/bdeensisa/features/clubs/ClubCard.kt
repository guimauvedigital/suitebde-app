package me.nathanfallet.bdeensisa.features.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.nathanfallet.bdeensisa.models.Club

@Composable
fun ClubCard(
    club: Club,
    badgeText: String?,
    badgeColor: Color,
    action: (() -> Unit)?,
    detailsEnabled: Boolean,
    showDetails: ((Club) -> Unit)? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 4.dp)
            .clickable {
                if (detailsEnabled) {
                    showDetails?.invoke(club)
                }
            },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    club.logo?.let { logo ->
                        AsyncImage(
                            model = "https://bdensisa.org/clubs/${club.id}/uploads/$logo",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = club.name
                        )
                        Text(
                            text = "${club.membersCount} membre${if (club.membersCount != 1L) "s" else ""}",
                            color = Color.Gray
                        )
                    }
                }
                badgeText?.let {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.caption,
                        color = Color.White,
                        modifier = Modifier
                            .background(badgeColor, MaterialTheme.shapes.small)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .clickable {
                                action?.invoke()
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = club.description ?: "",
                maxLines = if (detailsEnabled) 5 else Int.MAX_VALUE
            )
        }
    }

}
