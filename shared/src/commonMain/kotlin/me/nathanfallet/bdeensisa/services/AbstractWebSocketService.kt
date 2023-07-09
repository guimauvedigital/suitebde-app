package me.nathanfallet.bdeensisa.services

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class AbstractWebSocketService {

    private var isConnecting = false
    private var session: DefaultClientWebSocketSession? = null

    var onWebSocketMessage: ((Any) -> Unit)? = null
    var onWebSocketMessageConversation: ((Any) -> Unit)? = null

    abstract val token: String?
    abstract val apiService: APIService
    abstract val isNetworkAvailable: Boolean

    fun createWebSocket() {
        val token = this.token ?: return
        if (session != null || isConnecting) return
        isConnecting = true
        CoroutineScope(Job()).launch {
            try {
                apiService.webSocketChat(
                    token,
                    this@AbstractWebSocketService::onConnected,
                    this@AbstractWebSocketService::onDisconnected,
                    this@AbstractWebSocketService::onMessage
                )
            } catch (ignore: Exception) {
                isConnecting = false
            }
        }
    }

    fun disconnectWebSocket() {
        session?.let {
            CoroutineScope(Job()).launch {
                try {
                    it.close()
                } catch (ignore: Exception) {
                }
            }
            session = null
        }
    }

    private fun onConnected(session: DefaultClientWebSocketSession) {
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

    val debugDescription: String
        get() {
            return "AbstractWebSocketService(session=$session, isConnecting=$isConnecting)"
        }

}