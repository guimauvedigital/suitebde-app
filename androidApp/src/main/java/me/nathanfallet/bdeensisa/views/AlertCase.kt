package me.nathanfallet.bdeensisa.views

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

enum class AlertCase {

    saved, cancelling;

}

@Composable
fun AlertCaseDialog(
    alertCase: AlertCase?,
    onDismissRequest: () -> Unit,
    discardEdit: () -> Unit
) {
    when (alertCase) {
        AlertCase.cancelling -> {
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

        AlertCase.saved -> {
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
