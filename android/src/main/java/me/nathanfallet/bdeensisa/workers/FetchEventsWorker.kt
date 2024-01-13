package me.nathanfallet.bdeensisa.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.services.StorageService

class FetchEventsWorker(
    val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            SharedCacheService.getInstance(DatabaseDriverFactory(context)).getEvents(
                StorageService.getInstance(context).sharedPreferences.getString("token", null),
                true
            )
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

}