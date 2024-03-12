package me.nathanfallet.suitebde.services

import me.nathanfallet.suitebde.database.IDatabaseDriverFactory

class CacheService(databaseDriverFactory: IDatabaseDriverFactory) {

    private val api = APIService()

    fun apiService(): APIService {
        return api
    }

}
