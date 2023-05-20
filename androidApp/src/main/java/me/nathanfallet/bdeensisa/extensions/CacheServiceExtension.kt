package me.nathanfallet.bdeensisa.extensions

import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.services.CacheService
import me.nathanfallet.bdeensisa.utils.SingletonHolder

object SharedCacheService : SingletonHolder<CacheService, DatabaseDriverFactory>(::CacheService)
