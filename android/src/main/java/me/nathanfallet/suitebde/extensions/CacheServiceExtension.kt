package me.nathanfallet.suitebde.extensions

import me.nathanfallet.suitebde.database.IDatabaseDriverFactory
import me.nathanfallet.suitebde.services.CacheService
import me.nathanfallet.suitebde.utils.SingletonHolder

object SharedCacheService : SingletonHolder<CacheService, IDatabaseDriverFactory>(::CacheService)
