package me.nathanfallet.suitebde.features.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.models.ensisa.ShopItem

@Composable
fun ShopCard(
    item: ShopItem,
    detailsEnabled: Boolean,
    cotisant: Boolean,
    showDetails: ((ShopItem) -> Unit)? = null,
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
            }
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
