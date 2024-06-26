package com.suitebde.ui.components.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suitebde.R
import software.guimauve.ui.components.navigation.DefaultNavigationBar
import software.guimauve.ui.components.pickers.Picker

@Composable
@Suppress("FunctionName")
fun SendNotificationRootView(
    navigateUp: () -> Unit,
    topics: Map<String, String>,
    topic: String,
    updateTopic: (String) -> Unit,
    title: String,
    updateTitle: (String) -> Unit,
    body: String,
    updateBody: (String) -> Unit,
    sent: Boolean,
    isEnabled: Boolean,
    send: () -> Unit,
    dismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultNavigationBar(
            title = stringResource(R.string.send_notification_title),
            navigateUp = navigateUp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Picker(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = stringResource(R.string.send_notification_field_topic),
            items = topics,
            selected = topic,
            onSelected = updateTopic,
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = title,
            onValueChange = updateTitle,
            placeholder = {
                Text(
                    text = stringResource(R.string.send_notification_field_title),
                    color = Color.LightGray
                )
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = body,
            onValueChange = updateBody,
            placeholder = {
                Text(
                    text = stringResource(R.string.send_notification_field_content),
                    color = Color.LightGray
                )
            }
        )
        Button(
            onClick = send,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            enabled = isEnabled
        ) {
            Text(text = stringResource(R.string.send_notification_button))
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (sent) AlertDialog(
        onDismissRequest = dismiss,
        title = { Text(stringResource(R.string.send_notification_confirm_dialog)) },
        confirmButton = {
            Button(onClick = dismiss) {
                Text("OK")
            }
        }
    )

}

@Preview
@Composable
@Suppress("FunctionName")
fun SendNotificationRootViewPreview() {
    SendNotificationRootView(
        navigateUp = {},
        topics = mapOf("topic" to "Topic"),
        topic = "topic",
        updateTopic = {},
        title = "Title",
        updateTitle = {},
        body = "Body",
        updateBody = {},
        sent = false,
        isEnabled = true,
        send = {},
        dismiss = {}
    )
}
