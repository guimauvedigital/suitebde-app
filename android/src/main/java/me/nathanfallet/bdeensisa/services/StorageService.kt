package me.nathanfallet.bdeensisa.services

import android.content.Context
import me.nathanfallet.bdeensisa.utils.SingletonHolder

class StorageService private constructor(val context: Context) {

    // Shared instance

    companion object : SingletonHolder<StorageService, Context>(::StorageService)

    // Storage

    val sharedPreferences = context.getSharedPreferences("bdeensisa", Context.MODE_PRIVATE)

}