package me.nathanfallet.bdeensisa.features.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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
                text = item.title ?: "",
                fontWeight = FontWeight.Bold
            )
            if (detailsEnabled) {
                if (cotisant) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${item.price ?: 0}€",
                            style = TextStyle(textDecoration = TextDecoration.LineThrough)
                        )
                        Text(
                            text = "${item.priceReduced ?: 0}€",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text("${item.price ?: 0}€")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.content ?: "",
                maxLines = if (detailsEnabled) 5 else Int.MAX_VALUE
            )
        }
    }

}