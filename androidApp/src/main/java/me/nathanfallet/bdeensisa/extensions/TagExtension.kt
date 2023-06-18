package me.nathanfallet.bdeensisa.extensions

import android.nfc.Tag

val Tag.formattedIdentifier: String
    get() = id.toList().joinToString(":") {
        it.toUByte().toString(16).padStart(2, '0')
    }