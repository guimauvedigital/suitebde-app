package me.nathanfallet.bdeensisa.features.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
                Column(
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = club.name,
                        fontWeight = FontWeight.Bold
                    )
                    Text("${club.membersCount} membre${if (club.membersCount != 1L) "s" else ""}")
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
            Text(club.description ?: "")
        }
    }

}
