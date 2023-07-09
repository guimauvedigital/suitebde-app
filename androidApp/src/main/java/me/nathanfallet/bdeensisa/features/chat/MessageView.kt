package me.nathanfallet.bdeensisa.features.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.nathanfallet.bdeensisa.extensions.ChatLogo
import me.nathanfallet.bdeensisa.models.ChatMessage
import me.nathanfallet.bdeensisa.models.User


@Composable
fun MessageView(
    message: ChatMessage,
    isHeaderShown: Boolean,
    viewedBy: User?,
    sending: Boolean = false
) {

    Column {
        if (message.type == "system") {
            Text(
                text = message.content ?: "",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        } else {
            if (isHeaderShown && message.userId != viewedBy?.id) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                ) {
                    Box {
                        "${message.user?.firstName} ${message.user?.lastName}".ChatLogo(
                            size = 20,
                            corner = 10
                        )
                        AsyncImage(
                            model = "https://bdensisa.org/api/users/${message.userId}/picture",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                    Text(
                        text = "${message.user?.firstName} ${message.user?.lastName}",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Row(
                horizontalArrangement =
                if (message.userId == viewedBy?.id) Arrangement.End
                else Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    backgroundColor =
                    if (message.userId == viewedBy?.id) MaterialTheme.colors.primary
                    else if (MaterialTheme.colors.isLight) Color.LightGray
                    else Color.DarkGray,
                ) {
                    Text(
                        text = message.content ?: "",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                if (sending) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}