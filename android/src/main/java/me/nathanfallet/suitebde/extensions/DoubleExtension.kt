package me.nathanfallet.suitebde.extensions

import java.text.NumberFormat
import java.util.*

val Double.localizedPrice: String
    get() {
        val formatter = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        return formatter.format(this)
    }
