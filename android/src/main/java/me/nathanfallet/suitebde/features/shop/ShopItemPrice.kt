package me.nathanfallet.suitebde.features.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import me.nathanfallet.suitebde.extensions.localizedPrice
import me.nathanfallet.suitebde.models.ShopItem

@Composable
fun ShopItemPrice(
    item: ShopItem,
    cotisant: Boolean,
) {
    if (cotisant && item.priceReduced != item.price) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.price?.localizedPrice ?: "?",
                style = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
            Text(
                text = item.priceReduced?.localizedPrice ?: "?",
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        Text(item.price?.localizedPrice ?: "?")
    }
}
