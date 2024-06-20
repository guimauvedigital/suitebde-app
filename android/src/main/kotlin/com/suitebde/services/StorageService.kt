package com.suitebde.services

import android.content.Context
import com.suitebde.utils.SingletonHolder

class StorageService private constructor(val context: Context) {

    // Shared instance

    companion object : SingletonHolder<StorageService, Context>(::StorageService)

    // Storage

    val sharedPreferences = context.getSharedPreferences("bdeensisa", Context.MODE_PRIVATE)

}
