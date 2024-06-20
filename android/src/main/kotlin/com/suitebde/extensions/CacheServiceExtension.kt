package com.suitebde.extensions

import com.suitebde.database.IDatabaseDriverFactory
import com.suitebde.services.CacheService
import com.suitebde.utils.SingletonHolder

object SharedCacheService : SingletonHolder<CacheService, IDatabaseDriverFactory>(::CacheService)
