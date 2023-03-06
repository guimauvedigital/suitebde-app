package me.nathanfallet.bdeensisa.features.users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.extensions.fiveYears
import me.nathanfallet.bdeensisa.extensions.oneYear
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.views.DatePicker
import me.nathanfallet.bdeensisa.views.Picker

@Composable
fun UserView(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    mainViewModel: MainViewModel
) {

    val user by viewModel.getUser().observeAsState()
    val editing by viewModel.isEditing().observeAsState(false)

    val firstName by viewModel.getFirstName().observeAsState()
    val lastName by viewModel.getLastName().observeAsState()
    val option by viewModel.getOption().observeAsState()
    val year by viewModel.getYear().observeAsState()
    val expiration by viewModel.getExpiration().observeAsState()

    Column(modifier) {
        TopAppBar(
            title = { Text(text = "Utilisateur") },
            actions = {
                Text(
                    text = if (editing) "Terminé" else "Modifier",
                    modifier = Modifier
                        .clickable(
                            enabled = viewModel.editable,
                            onClick = viewModel::toggleEdit
                        )
                        .padding(16.dp)
                )
            }
        )
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = "Informations",
            style = MaterialTheme.typography.h6
        )
        if (editing) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                value = firstName ?: "",
                onValueChange = viewModel::setFirstName,
                placeholder = {
                    Text(
                        text = "Prénom",
                        color = Color.LightGray
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                value = lastName ?: "",
                onValueChange = viewModel::setLastName,
                placeholder = {
                    Text(
                        text = "Nom",
                        color = Color.LightGray
                    )
                }
            )
            Picker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                placeholder = "Année",
                items = mapOf(
                    "1A" to "1A",
                    "2A" to "2A",
                    "3A" to "3A",
                    "other" to "4A et plus"
                ),
                selected = year ?: "",
                onSelected = viewModel::setYear,
            )
            Picker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                placeholder = "Option",
                items = mapOf(
                    "ir" to "Informatique et Réseaux",
                    "ase" to "Automatique et Systèmes embarqués",
                    "meca" to "Mécanique",
                    "tf" to "Textile et Fibres",
                    "gi" to "Génie Industriel"
                ),
                selected = option ?: "",
                onSelected = viewModel::setOption,
            )
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.updateInfo(mainViewModel.getToken().value)
                }
            ) {
                Text(text = "Enregistrer")
            }
        } else {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = "${user?.firstName} ${user?.lastName}"
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = user?.description ?: ""
            )
        }
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = "Cotisation",
            style = MaterialTheme.typography.h6
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            text = if (user?.cotisant != null) "Cotisant" else "Non cotisant",
            color = if (user?.cotisant != null) Color.Green else Color.Red
        )
        if (user?.cotisant != null && !editing) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = "Expire : ${user?.cotisant?.expiration?.renderedDate}"
            )
        }
        if (editing) {
            DatePicker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                placeholder = "Expire",
                selected = expiration,
                onSelected = viewModel::setExpiration,
            )
            Button(
                onClick = {
                    viewModel.setExpiration(oneYear)
                },
                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                colors = ButtonDefaults.outlinedButtonColors(),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "1 an")
            }
            Button(
                onClick = {
                    viewModel.setExpiration(fiveYears)
                },
                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                colors = ButtonDefaults.outlinedButtonColors(),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(text = "Scolarité")
            }
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.updateExpiration(mainViewModel.getToken().value)
                }
            ) {
                Text(text = "Enregistrer")
            }
        }
    }

}
