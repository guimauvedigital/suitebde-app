package me.nathanfallet.bdeensisa.features.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.models.ShopItem

@Composable
fun ShopCard(
    item: ShopItem,
    detailsEnabled: Boolean,
    cotisant: Boolean,
    showDetails: ((ShopItem) -> Unit)? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 4.dp)
            .clickable {
                if (detailsEnabled) {
                    showDetails?.invoke(item)
                }
            },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.title ?: ""
            )
            if (detailsEnabled) {
                ShopItemPrice(item, cotisant)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.content ?: "",
                maxLines = if (detailsEnabled) 5 else Int.MAX_VALUE
            )
        }
    }

}