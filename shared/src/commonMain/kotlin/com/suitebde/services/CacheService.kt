package com.suitebde.services

import com.suitebde.database.IDatabaseDriverFactory

class CacheService(databaseDriverFactory: IDatabaseDriverFactory) {

    private val api = APIService()

    fun apiService(): APIService {
        return api
    }

}
