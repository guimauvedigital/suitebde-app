package me.nathanfallet.suitebde.features.users

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R
import me.nathanfallet.suitebde.extensions.fiveYears
import me.nathanfallet.suitebde.extensions.oneDay
import me.nathanfallet.suitebde.extensions.oneYear
import me.nathanfallet.suitebde.features.root.OldRootViewModel
import me.nathanfallet.suitebde.models.application.AlertCase
import me.nathanfallet.suitebde.ui.components.AlertCaseDialog
import me.nathanfallet.suitebde.ui.components.DatePicker
import me.nathanfallet.suitebde.ui.components.Picker
import me.nathanfallet.suitebde.ui.components.users.UserDetailsView
import me.nathanfallet.suitebde.viewmodels.users.UserViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionName")
fun UserView(
    associationId: String,
    userId: String,
    modifier: Modifier = Modifier,
    oldViewModel: me.nathanfallet.suitebde.features.users.UserViewModel,
    oldRootViewModel: OldRootViewModel,
    navigateUp: () -> Unit,
) {

    val viewModel = koinViewModel<UserViewModel>(
        parameters = { parametersOf(associationId, userId) }
    )

    LaunchedEffect(associationId, userId) {
        viewModel.onAppear()
    }

    val user by viewModel.user.collectAsState()


    val isEditing by viewModel.isEditing.collectAsState()

    val context = LocalContext.current

    val oldUser by oldViewModel.getUser().observeAsState()
    val editing by oldViewModel.isEditing().observeAsState()
    val alert by oldViewModel.getAlert().observeAsState()

    val image by oldViewModel.getImage().observeAsState()

    val firstName by oldViewModel.getFirstName().observeAsState()
    val lastName by oldViewModel.getLastName().observeAsState()
    val option by oldViewModel.getOption().observeAsState()
    val year by oldViewModel.getYear().observeAsState()
    val expiration by oldViewModel.getExpiration().observeAsState()

    val tickets by oldViewModel.getTickets().observeAsState()
    val paid by oldViewModel.getPaid().observeAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            oldViewModel.updateImage(oldRootViewModel.getToken().value, uri, context)
        }
    )

    if (isEditing) {
        LazyColumn(modifier) {
            stickyHeader {
                TopAppBar(
                    title = { Text(text = "Utilisateur") },
                    navigationIcon = {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.app_back)
                            )
                        }
                    },
                    actions = {
                        if (oldViewModel.editable) {
                            Text(
                                text = if (editing == true) "Terminé" else "Modifier",
                                modifier = Modifier
                                    .clickable(onClick = oldViewModel::toggleEdit)
                                    .padding(16.dp)
                            )
                        }
                    }
                )
                AlertCaseDialog(
                    alertCase = alert,
                    onDismissRequest = { oldViewModel.setAlert(null) },
                    discardEdit = oldViewModel::discardEdit,
                    deleteAccount = oldRootViewModel::deleteAccount
                )
            }
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
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = "Informations",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    value = firstName ?: "",
                    onValueChange = oldViewModel::setFirstName,
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
                    onValueChange = oldViewModel::setLastName,
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
                    onSelected = oldViewModel::setYear,
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
                    onSelected = oldViewModel::setOption,
                )
            }
            item {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        oldViewModel.updateInfo(oldRootViewModel.getToken().value) {
                            oldRootViewModel.setUser(it)
                        }
                    }
                ) {
                    Text(text = "Enregistrer")
                }
            }
            if (oldViewModel.isMyAccount) {
                item {
                    Button(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        onClick = {
                            oldViewModel.setAlert(AlertCase.DELETING)
                        }
                    ) {
                        Text(text = "Supprimer mon compte")
                    }
                }
            }
            if (editing != true || oldRootViewModel.getUser().value?.hasPermission("admin.users.edit") == true) {
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
                        text = if (oldUser?.cotisant != null) "Cotisant" else "Non cotisant",
                        color = if (oldUser?.cotisant != null) Color.Green else Color.Red
                    )
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
                            onSelected = oldViewModel::setExpiration,
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                oldViewModel.setExpiration(oneDay)
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
                                oldViewModel.setExpiration(oneYear)
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
                                oldViewModel.setExpiration(fiveYears)
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
                                oldViewModel.updateExpiration(oldRootViewModel.getToken().value)
                            }
                        ) {
                            Text(text = "Enregistrer")
                        }
                    }
                }
            }
            if (tickets?.isNotEmpty() == true && (editing != true || oldRootViewModel.getUser().value?.hasPermission(
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
                                        oldViewModel.getPaid().value?.set(ticket.id, it)
                                        oldViewModel.updateTicket(
                                            oldRootViewModel.getToken().value,
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
        return
    }

    user?.let {
        UserDetailsView(
            user = it,
            navigateUp = navigateUp,
            modifier = modifier
        )
    } ?: run {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

}
