package me.nathanfallet.bdeensisa.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.nathanfallet.bdeensisa.database.DatabaseDriverFactory
import me.nathanfallet.bdeensisa.extensions.SharedCacheService
import me.nathanfallet.bdeensisa.utils.SingletonHolder

class WebSocketService(
    private val context: Context
) : ConnectivityManager.NetworkCallback() {

    companion object : SingletonHolder<WebSocketService, Context>(::WebSocketService)

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val isNetworkAvailable: Boolean
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

    private var isConnecting = false
    private var session: Any? = null

    var onWebSocketMessage: ((Any) -> Unit)? = null
    var onWebSocketMessageConversation: ((Any) -> Unit)? = null

    init {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build(),
            this
        )
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        createWebSocket()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        disconnectWebSocket()
    }

    fun createWebSocket() {
        val token = StorageService
            .getInstance(context)
            .sharedPreferences
            .getString("token", null) ?: return
        if (session != null || isConnecting) return
        isConnecting = true
        CoroutineScope(Job()).launch {
            try {
                SharedCacheService.getInstance(DatabaseDriverFactory(context)).apiService()
                    .webSocketChat(
                        token,
                        this@WebSocketService::onConnected,
                        this@WebSocketService::onDisconnected,
                        this@WebSocketService::onMessage
                    )
            } catch (e: Exception) {
                isConnecting = false
                e.printStackTrace()
            }
        }
    }

    fun disconnectWebSocket() {
        session?.let {
            CoroutineScope(Job()).launch {
                try {
                    SharedCacheService.getInstance(DatabaseDriverFactory(context)).apiService()
                        .closeWebSocketChat(it)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            session = null
        }
    }

    private fun onConnected(session: Any) {
        this.session = session
        this.isConnecting = false
    }

    private fun onDisconnected() {
        this.session = null

        if (isNetworkAvailable) {
            createWebSocket()
        }
    }

    private fun onMessage(message: Any) {
        onWebSocketMessage?.invoke(message)
        onWebSocketMessageConversation?.invoke(message)
    }

}