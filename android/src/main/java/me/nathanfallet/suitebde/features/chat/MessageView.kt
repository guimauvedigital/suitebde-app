package me.nathanfallet.suitebde.features.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.nathanfallet.suitebde.extensions.ChatLogo
import me.nathanfallet.suitebde.models.ensisa.ChatMessage
import me.nathanfallet.suitebde.models.ensisa.User


@Composable
fun MessageView(
    message: ChatMessage,
    isHeaderShown: Boolean,
    viewedBy: User?,
    sending: Boolean = false,
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
                        style = MaterialTheme.typography.bodyMedium
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
                    colors = CardDefaults.cardColors(
                        containerColor = if (message.userId == viewedBy?.id) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiaryContainer,
                    )
                ) {
                    Text(
                        text = message.content ?: "",
                        style = MaterialTheme.typography.bodyMedium,
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
