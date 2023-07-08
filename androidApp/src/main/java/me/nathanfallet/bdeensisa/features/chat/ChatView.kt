package me.nathanfallet.bdeensisa.features.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.extensions.ChatLogo
import me.nathanfallet.bdeensisa.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel,
    mainViewModel: MainViewModel
) {

    val conversations by viewModel.getConversations().observeAsState()

    LazyColumn(
        modifier
    ) {
        stickyHeader {
            TopAppBar(
                title = {
                    Text(text = "Conversations")
                }
            )
        }
        items(conversations ?: listOf()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { mainViewModel.setSelectedConversation(it) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    it.logo?.let { logo ->
                        AsyncImage(
                            model = logo,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    } ?: run {
                        it.name.ChatLogo(it.backupLogo)
                    }
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.name,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            if (it.membership?.notifications == false) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_baseline_notifications_off_24),
                                    contentDescription = "Notifications désactivées",
                                    colorFilter = ColorFilter.tint(Color.Gray),
                                    modifier = Modifier
                                        .size(16.dp)
                                )
                            }
                        }
                        it.lastMessage?.let { lastMessage ->
                            Text(
                                text = lastMessage.content ?: "",
                                fontWeight = if (it.isUnread) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        } ?: run {
                            Text(
                                text = "Aucun message",
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }
                    }
                }
                Divider(
                    modifier = Modifier.padding(start = 56.dp)
                )
            }
        }
    }

}