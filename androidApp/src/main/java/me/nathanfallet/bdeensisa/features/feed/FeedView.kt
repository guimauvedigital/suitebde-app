package me.nathanfallet.bdeensisa.features.feed

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FeedView(
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit
) {

    Text(text = "Feed")

}