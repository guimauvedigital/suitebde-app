package me.nathanfallet.bdeensisa.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.utils.SingletonHolder

class WebSocketService(
    private val context: Context
) : AbstractWebSocketService() {

    companion object : SingletonHolder<WebSocketService, Context>(::WebSocketService)

    override val token: String?
        get() {
            return StorageService
                .getInstance(context)
                .sharedPreferences
                .getString("token", null)
        }

    override val apiService: APIService
        get() {
            return SharedCacheService.getInstance(DatabaseDriverFactory(context)).apiService()
        }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val isNetworkAvailable: Boolean
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                } ?: false
            } else true
        }

    init {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    createWebSocket()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    disconnectWebSocket()
                }
            }
        )
    }

}