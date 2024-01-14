package me.nathanfallet.suitebde.features.clubs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.features.MainViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ClubView(
    modifier: Modifier = Modifier,
    viewModel: ClubViewModel,
    mainViewModel: MainViewModel,
    navigateUp: () -> Unit,
) {

    val user by mainViewModel.getUser().observeAsState()

    val members by viewModel.getMembers().observeAsState()

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = viewModel.club.name) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            contentDescription = "Retour"
                        )
                    }
                },
                actions = {
                    if (members?.any { it.userId == user?.id } == true) {
                        IconButton(
                            onClick = { viewModel.leave(mainViewModel.getToken().value) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_logout_24),
                                contentDescription = "Quitter"
                            )
                        }
                    }
                    viewModel.club.email?.let { email ->
                        IconButton(
                            onClick = {
                                val browserIntent = Intent(
                                    Intent.ACTION_SENDTO,
                                    Uri.parse("mailto:$email")
                                )
                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                ContextCompat.startActivity(
                                    viewModel.getApplication(),
                                    browserIntent,
                                    null
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_mail_24),
                                contentDescription = "Quitter"
                            )
                        }
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            ClubCard(
                club = viewModel.club,
                badgeText = if (user?.cotisant != null && members?.none { it.userId == user?.id } == true) "REJOINDRE" else null,
                badgeColor = MaterialTheme.colorScheme.primary,
                action = {
                    viewModel.join(mainViewModel.getToken().value)
                },
                detailsEnabled = false
            )
        }
        item {
            Text(
                text = "Membres",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(members ?: listOf()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = "${it.user?.firstName} ${it.user?.lastName}"
                        )
                        Text(
                            text = it.user?.description ?: "",
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = if (it.role == "admin") "ADMIN" else "MEMBRE",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                if (it.role == "admin") Color.Black
                                else Color(0xFF0BDA51),
                                MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

}
