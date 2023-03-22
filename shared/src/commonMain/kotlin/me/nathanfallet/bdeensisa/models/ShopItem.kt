package me.nathanfallet.bdeensisa.models

interface ShopItem {

    val id: String
    val title: String?
    val content: String?
    val price: Double?
    val priceReduced: Double?
    val bail: Double?

}