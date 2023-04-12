package me.nathanfallet.bdeensisa.models

interface ShopItem {

    val type: String
    val id: String
    val title: String?
    val content: String?
    val price: Double?
    val priceReduced: Double?
    val bail: Double?

}