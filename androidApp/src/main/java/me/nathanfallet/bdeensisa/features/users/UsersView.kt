package me.nathanfallet.bdeensisa.features.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.extensions.formatted
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.utils.debounce

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UsersView(
    modifier: Modifier = Modifier,
    viewModel: UsersViewModel,
    mainViewModel: MainViewModel,
    owner: LifecycleOwner
) {

    val search by viewModel.getSearch().observeAsState()
    val users by viewModel.getUsers().observeAsState()
    val searchUsers by viewModel.getSearchUsers().observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    viewModel.getSearch().observe(owner, debounce(500L, viewModel.viewModelScope) {
        viewModel.search(mainViewModel.getToken().value, true)
    })

    LazyColumn(modifier) {
        item {
            TopAppBar(
                title = {
                    search?.let { search ->
                        TextField(
                            value = search,
                            onValueChange = viewModel::setSearch,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent
                            ),
                            placeholder = {
                                Text(
                                    text = "Rechercher",
                                    color = Color.LightGray
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                }
                            )
                        )
                    } ?: run {
                        Text(text = "Utilisateurs")
                    }
                },
                actions = {
                    if (search != null) {
                        IconButton(
                            onClick = { viewModel.setSearch(null) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                                contentDescription = "Annuler la recherche"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { viewModel.setSearch("") }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_search_24),
                                contentDescription = "Rechercher"
                            )
                        }
                    }
                }
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
            }
            viewModel.loadMore(mainViewModel.getToken().value, user.id)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}
