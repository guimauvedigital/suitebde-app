package me.nathanfallet.bdeensisa.views

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

enum class AlertCase {

    SAVED, CANCELLING, DELETING

}

@Composable
fun AlertCaseDialog(
    alertCase: AlertCase?,
    onDismissRequest: () -> Unit,
    discardEdit: () -> Unit,
    deleteAccount: () -> Unit = {}
) {
    when (alertCase) {
        AlertCase.CANCELLING -> {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = { Text("Les modifications n'ont pas été enregistrées") },
                text = { Text("Etes vous sûr de vouloir quitter ?") },
                confirmButton = {
                    Button(onClick = {
                        discardEdit()
                        onDismissRequest()
                    }) {
                        Text("Quitter sans enregistrer")
                    }
                },
                dismissButton = {
                    Button(onClick = onDismissRequest) {
                        Text("Revenir sur l'édition")
                    }
                }
            )
        }

        AlertCase.DELETING -> {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = { Text("Es tu sûr de vouloir supprimer ton compte ?") },
                text = { Text("Cette action est irréversible et tu perdras toutes tes données.") },
                confirmButton = {
                    Button(onClick = {
                        deleteAccount()
                        onDismissRequest()
                    }) {
                        Text("Supprimer mon compte")
                    }
                },
                dismissButton = {
                    Button(onClick = onDismissRequest) {
                        Text("Annuler")
                    }
                }
            )
        }

        AlertCase.SAVED -> {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = { Text("Les modifications ont bien été enregistrées") },
                confirmButton = {
                    Button(onClick = onDismissRequest) {
                        Text("OK")
                    }
                }
            )
        }
        else -> {} // Nothing in case it's null
    }
}
