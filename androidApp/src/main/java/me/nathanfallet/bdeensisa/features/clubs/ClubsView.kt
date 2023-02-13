package me.nathanfallet.bdeensisa.features.clubs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.features.MainViewModel

@Composable
fun ClubsView(
    modifier: Modifier = Modifier,
    viewModel: ClubsViewModel,
    mainViewModel: MainViewModel
) {

    val mine by viewModel.getMine().observeAsState()
    val clubs by viewModel.getClubs().observeAsState()

    LazyColumn(
        modifier
    ) {
        item {
            TopAppBar(
                title = {
                    Text(text = "Clubs")
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (mine?.isNotEmpty() == true) {
            item {
                Text(
                    text = "Mes clubs",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
            items(mine ?: listOf()) {

            }
            item {
                Text(
                    text = "Autres clubs",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
        }
    }

}