package me.nathanfallet.suitebde.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import me.nathanfallet.suitebde.models.application.SuiteBDEJson
import me.nathanfallet.suitebde.models.ensisa.*

class APIService {

    // Constants

    private companion object {
        const val baseUrl = "https://bdensisa.org"
    }

    // Client

    private val httpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(SuiteBDEJson.json)
        }
        install(WebSockets)
    }

    private suspend fun createRequest(
        method: HttpMethod,
        url: String,
        token: String? = null,
        builder: HttpRequestBuilder.() -> Unit = {},
    ): HttpResponse {
        return httpClient.request(baseUrl + url) {
            this.method = method
            token?.let { token ->
                this.header("Authorization", "Bearer $token")
            }
            builder()
        }
    }

    // API

    @Throws(Exception::class)
    suspend fun checkToken(token: String): UserToken? {
        return try {
            createRequest(HttpMethod.Get, "/api/v1/auth", token).body()
        } catch (e: Exception) {
            if (e is ClientRequestException && e.response.status == HttpStatusCode.Unauthorized) null
            else throw e
        }
    }

    @Throws(Exception::class)
    suspend fun getUser(token: String, id: String): User {
        return createRequest(HttpMethod.Get, "/api/v1/users/$id", token).body()
    }

    @Throws(Exception::class)
    suspend fun getUserPicture(token: String, id: String): ByteArray {
        return createRequest(HttpMethod.Get, "/api/v1/users/$id/picture", token).body()
    }

    @Throws(Exception::class)
    suspend fun getUserTickets(token: String, id: String): List<Ticket> {
        return createRequest(HttpMethod.Get, "/api/v1/users/$id/tickets", token).body()
    }

    @Throws(Exception::class)
    suspend fun updateUser(
        token: String,
        id: String,
        upload: UserUpload,
    ): User {
        return createRequest(HttpMethod.Put, "/api/v1/users/$id", token) {
            contentType(ContentType.Application.Json)
            setBody(upload)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun deleteMe(token: String) {
        return createRequest(HttpMethod.Delete, "/api/v1/users/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun updateUserPicture(
        token: String,
        id: String,
        picture: ByteArray,
    ) {
        return createRequest(HttpMethod.Post, "/api/v1/users/$id/picture", token) {
            contentType(ContentType.Image.JPEG)
            setBody(picture)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun updateUserTicket(
        token: String,
        id: String,
        ticketId: String,
        paid: Boolean,
    ): Ticket {
        return createRequest(HttpMethod.Put, "/api/v1/users/$id/tickets/$ticketId", token) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "paid" to paid
                )
            )
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getScanHistory(token: String): List<ScanHistoryEntry> {
        return createRequest(HttpMethod.Get, "/api/v1/scans", token).body()
    }

    @Throws(Exception::class)
    suspend fun sendNotification(token: String, payload: NotificationPayload) {
        return createRequest(HttpMethod.Post, "/api/v1/notifications", token) {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getChat(token: String): List<ChatConversation> {
        return createRequest(HttpMethod.Get, "/api/v1/chat", token).body()
    }

    @Throws(Exception::class)
    suspend fun getChatMembers(token: String, type: String, id: String): List<User> {
        return createRequest(HttpMethod.Get, "/api/v1/chat/$type/$id/members", token).body()
    }

    @Throws(Exception::class)
    suspend fun putChatMembers(
        token: String,
        type: String,
        id: String,
        upload: ChatMembershipUpload?,
    ) {
        return createRequest(HttpMethod.Put, "/api/v1/chat/$type/$id/members", token) {
            contentType(ContentType.Application.Json)
            setBody(upload)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getChatMessages(
        token: String,
        type: String,
        id: String,
        offset: Long = 0,
    ): List<ChatMessage> {
        return createRequest(HttpMethod.Get, "/api/v1/chat/$type/$id/messages", token) {
            parameter("offset", offset)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun postChatMessages(
        token: String,
        type: String,
        id: String,
        message: ChatMessageUpload,
    ): ChatMessage {
        return createRequest(HttpMethod.Post, "/api/v1/chat/$type/$id/messages", token) {
            contentType(ContentType.Application.Json)
            setBody(message)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun webSocketChat(
        token: String,
        onConnected: (DefaultClientWebSocketSession) -> Unit,
        onDisconnected: () -> Unit,
        onMessage: (Any) -> Unit,
    ) {
        httpClient.plugin(WebSockets)
        val session = httpClient.prepareRequest {
            method = HttpMethod.Get
            url("wss", "bdensisa.org", 443, "/api/v1/chat")
            header("Authorization", "Bearer $token")
        }
        session.body<DefaultClientWebSocketSession, Unit> {
            try {
                onConnected(it)
                for (frame in it.incoming) {
                    if (frame !is Frame.Text) continue
                    val text = frame.readText()
                    if (text.startsWith("ChatMessage:")) {
                        onMessage(SuiteBDEJson.json.decodeFromString<ChatMessage>(text.substring("ChatMessage:".length)))
                    } else if (text.startsWith("ChatMembership:")) {
                        onMessage(SuiteBDEJson.json.decodeFromString<ChatMembership>(text.substring("ChatMembership:".length)))
                    }
                }
            } finally {
                onDisconnected()
                it.close()
            }
        }
    }

}
