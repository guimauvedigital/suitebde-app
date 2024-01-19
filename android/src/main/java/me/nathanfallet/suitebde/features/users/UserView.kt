package me.nathanfallet.suitebde.features.users

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.fiveYears
import me.nathanfallet.suitebde.extensions.oneDay
import me.nathanfallet.suitebde.extensions.oneYear
import me.nathanfallet.suitebde.extensions.renderedDate
import me.nathanfallet.suitebde.features.root.RootViewModel
import me.nathanfallet.suitebde.ui.components.AlertCase
import me.nathanfallet.suitebde.ui.components.AlertCaseDialog
import me.nathanfallet.suitebde.ui.components.DatePicker
import me.nathanfallet.suitebde.ui.components.Picker

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserView(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    rootViewModel: RootViewModel,
    navigateUp: () -> Unit,
) {

    val context = LocalContext.current

    val user by viewModel.getUser().observeAsState()
    val editing by viewModel.isEditing().observeAsState()
    val alert by viewModel.getAlert().observeAsState()

    val image by viewModel.getImage().observeAsState()

    val firstName by viewModel.getFirstName().observeAsState()
    val lastName by viewModel.getLastName().observeAsState()
    val option by viewModel.getOption().observeAsState()
    val year by viewModel.getYear().observeAsState()
    val expiration by viewModel.getExpiration().observeAsState()

    val tickets by viewModel.getTickets().observeAsState()
    val paid by viewModel.getPaid().observeAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.updateImage(rootViewModel.getToken().value, uri, context)
        }
    )

    LazyColumn(modifier) {
        stickyHeader {
            TopAppBar(
                title = { Text(text = "Utilisateur") },
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
                    if (viewModel.editable) {
                        Text(
                            text = if (editing == true) "Terminé" else "Modifier",
                            modifier = Modifier
                                .clickable(onClick = viewModel::toggleEdit)
                                .padding(16.dp)
                        )
                    }
                }
            )
            AlertCaseDialog(
                alertCase = alert,
                onDismissRequest = { viewModel.setAlert(null) },
                discardEdit = viewModel::discardEdit,
                deleteAccount = rootViewModel::deleteAccount
            )
        }
        if (editing == true) {
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = "Photo d'identité",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    image?.let {
                        Image(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape),
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Photo d'identité",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Button(
                        onClick = {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Text(text = if (image != null) "Modifier la photo" else "Ajouter une photo")
                    }
                }
            }
        }
        item {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Informations",
                style = MaterialTheme.typography.titleSmall
            )
        }
        if (editing == true) {
            item {
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
            }
            item {
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
            }
            item {
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
                        "other" to "4A et plus",
                        "CPB" to "CPB"
                    ),
                    selected = year ?: "",
                    onSelected = viewModel::setYear,
                )
            }
            item {
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
            }
            item {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.updateInfo(rootViewModel.getToken().value) {
                            rootViewModel.setUser(it)
                        }
                    }
                ) {
                    Text(text = "Enregistrer")
                }
            }
        } else {
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    image?.let {
                        Image(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape),
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Photo d'identité",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "${user?.firstName} ${user?.lastName}"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = user?.description ?: "",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        if (viewModel.isMyAccount) {
            item {
                Button(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    colors = ButtonDefaults.outlinedButtonColors(),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.setAlert(AlertCase.DELETING)
                    }
                ) {
                    Text(text = "Supprimer mon compte")
                }
            }
        }
        if (editing != true || rootViewModel.getUser().value?.hasPermission("admin.users.edit") == true) {
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = "Cotisation",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            item {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = if (user?.cotisant != null) "Cotisant" else "Non cotisant",
                    color = if (user?.cotisant != null) Color.Green else Color.Red
                )
            }
            if (user?.cotisant != null && editing != true) {
                item {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        text = "Expire : ${user?.cotisant?.expiration?.renderedDate}"
                    )
                }
            }
            if (editing == true) {
                item {
                    DatePicker(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        placeholder = "Expire",
                        selected = expiration,
                        onSelected = viewModel::setExpiration,
                    )
                }
                item {
                    Button(
                        onClick = {
                            viewModel.setExpiration(oneDay)
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(text = "1 jour")
                    }
                }
                item {
                    Button(
                        onClick = {
                            viewModel.setExpiration(oneYear)
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(text = "1 an")
                    }
                }
                item {
                    Button(
                        onClick = {
                            viewModel.setExpiration(fiveYears)
                        },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(text = "Scolarité")
                    }
                }
                item {
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            viewModel.updateExpiration(rootViewModel.getToken().value)
                        }
                    ) {
                        Text(text = "Enregistrer")
                    }
                }
            }
        }
        if (tickets?.isNotEmpty() == true && (editing != true || rootViewModel.getUser().value?.hasPermission(
                "admin.tickets.edit"
            ) == true)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = "Tickets",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            items(tickets ?: listOf()) { ticket ->
                Card(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 4.dp)
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
                                    text = ticket.event?.title ?: "",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (editing != true) {
                                Text(
                                    text = if (ticket.paid != null) "PAYÉ" else "NON PAYÉ",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            if (ticket.paid != null) Color(0xFF0BDA51)
                                            else MaterialTheme.colorScheme.primary,
                                            MaterialTheme.shapes.small
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                        // We put this under the title (and not next to it)
                        // because else UI is broken (Picker takes all the space)
                        // It's not as easy as it is on iOS
                        if (editing == true) {
                            Picker(
                                modifier = Modifier
                                    .padding(top = 8.dp),
                                items = mapOf(
                                    true to "Payé",
                                    false to "Non payé"
                                ),
                                selected = paid?.get(ticket.id) ?: false,
                                onSelected = {
                                    viewModel.getPaid().value?.set(ticket.id, it)
                                    viewModel.updateTicket(
                                        rootViewModel.getToken().value,
                                        ticket.id
                                    )
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = ticket.event?.content ?: "",
                            maxLines = 5
                        )
                    }
                }
            }
        }
    }

}
