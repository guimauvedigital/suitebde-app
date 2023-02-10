package me.nathanfallet.bdeensisa.features.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.extensions.formatted
import me.nathanfallet.bdeensisa.features.MainViewModel

@Composable
fun UsersView(
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel,
    mainViewModel: MainViewModel
) {

    val users by viewModel.getUsers().observeAsState()
    val searchUsers by viewModel.getSearchUsers().observeAsState()

    LazyColumn(modifier) {
        item {
            TopAppBar(
                title = { Text(text = "Utilisateurs") },
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(searchUsers ?: users ?: listOf()) { user ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .clickable {
                        mainViewModel.setSelectedUser(user)
                    }
            ) {
                Text("${user.firstName} ${user.lastName}")
                Text(
                    text = user.description,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = if (user.cotisant != null)
                        "Cotisant jusqu'au ${user.cotisant?.expiration?.formatted}"
                    else "Non cotisant",
                    color = if (user.cotisant != null) Color.Green
                    else Color.Red
                )
                viewModel.loadMore(mainViewModel.getToken().value, user.id)
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}
