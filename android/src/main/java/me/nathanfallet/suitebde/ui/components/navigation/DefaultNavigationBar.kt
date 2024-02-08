package me.nathanfallet.suitebde.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.R

@Composable
@Suppress("FunctionName")
fun DefaultNavigationBar(
    title: String,
    navigateUp: (() -> Unit)? = null,
    toolbar: @Composable RowScope.() -> Unit = {},
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        navigateUp?.let {
            IconButton(
                onClick = navigateUp,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.app_back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center).padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            content = toolbar
        )
    }

}

@Preview
@Composable
@Suppress("FunctionName")
fun DefaultNavigationBarPreview() {
    DefaultNavigationBar(
        title = "Title",
        navigateUp = {},
        toolbar = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                    contentDescription = stringResource(R.string.settings_title),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                    contentDescription = stringResource(R.string.settings_title),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}
