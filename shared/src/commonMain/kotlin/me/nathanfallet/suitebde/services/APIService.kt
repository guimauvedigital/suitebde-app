package me.nathanfallet.suitebde.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.nathanfallet.suitebde.models.ensisa.*

class APIService {

    // Constants

    private companion object {
        const val baseUrl = "https://bdensisa.org"
    }

    // Client

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val httpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
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

    val authenticationUrl: String
        get() = "$baseUrl/account/authorize"

    @Throws(Exception::class)
    suspend fun authenticate(code: String): UserToken {
        // Extract data from code URL
        val inputParameters = code.removeRange(0, code.indexOf("?") + 1)
            .split("&")
            .map {
                val values = it.split("=")
                values[0] to values[1]
            }
        val userCode = inputParameters.find { it.first == "code" }?.second ?: ""

        // Call API
        return createRequest(HttpMethod.Post, "/api/v1/auth") {
            contentType(ContentType.Application.Json)
            setBody(UserAuthorize(userCode))
        }.body()
    }

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
    suspend fun sendNotificationToken(token: String, notificationToken: String) {
        createRequest(HttpMethod.Post, "/api/v1/notifications/tokens", token) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "token" to notificationToken
                )
            )
        }
    }

    @Throws(Exception::class)
    suspend fun getEvents(offset: Long = 0, limit: Long = 10): List<Event> {
        return createRequest(HttpMethod.Get, "/api/v1/events") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getEvent(id: String): Event {
        return createRequest(HttpMethod.Get, "/api/v1/events/$id").body()
    }

    @Throws(Exception::class)
    suspend fun updateEvent(
        token: String,
        id: String,
        upload: EventUpload,
    ): Event {
        return createRequest(HttpMethod.Put, "/api/v1/events/$id", token) {
            contentType(ContentType.Application.Json)
            setBody(upload)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun suggestEvent(
        token: String,
        upload: EventUpload,
    ): Event {
        return createRequest(HttpMethod.Post, "/api/v1/events", token) {
            contentType(ContentType.Application.Json)
            setBody(upload)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getTopics(offset: Long = 0, limit: Long = 10): List<Topic> {
        return createRequest(HttpMethod.Get, "/api/v1/topics") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getUserCourses(
        token: String,
        offset: Long = 0,
        limit: Long = 10,
    ): List<UserCourse> {
        return createRequest(HttpMethod.Get, "/api/v1/courses", token) {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getUsers(
        token: String,
        offset: Long = 0,
        limit: Long = 25,
        search: String? = null,
    ): List<User> {
        return createRequest(HttpMethod.Get, "/api/v1/users", token) {
            parameter("offset", offset)
            parameter("limit", limit)
            search?.let { search ->
                parameter("search", search)
            }
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getUser(token: String, id: String): User {
        return createRequest(HttpMethod.Get, "/api/v1/users/$id", token).body()
    }

    @Throws(Exception::class)
    suspend fun getUserByNFC(token: String, id: String): User {
        return createRequest(HttpMethod.Get, "/api/v1/nfc/$id", token).body()
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
    suspend fun postNFC(
        token: String,
        id: String,
    ): User {
        return createRequest(HttpMethod.Post, "/api/v1/nfc/$id", token).body()
    }

    @Throws(Exception::class)
    suspend fun deleteNFC(
        token: String,
        id: String,
    ): User {
        return createRequest(HttpMethod.Delete, "/api/v1/nfc/$id", token).body()
    }

    @Throws(Exception::class)
    suspend fun getScanHistory(token: String): List<ScanHistoryEntry> {
        return createRequest(HttpMethod.Get, "/api/v1/scans", token).body()
    }

    @Throws(Exception::class)
    suspend fun getClubs(
        offset: Long = 0,
        limit: Long = 25,
    ): List<Club> {
        return createRequest(HttpMethod.Get, "/api/v1/clubs") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getClubsMe(token: String?): List<ClubMembership> {
        return createRequest(HttpMethod.Get, "/api/v1/clubs/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun getClub(id: String): Club {
        return createRequest(HttpMethod.Get, "/api/v1/clubs/$id").body()
    }

    @Throws(Exception::class)
    suspend fun getClubMembers(id: String): List<ClubMembership> {
        return createRequest(HttpMethod.Get, "/api/v1/clubs/$id/members").body()
    }

    @Throws(Exception::class)
    suspend fun joinClub(token: String, id: String): ClubMembership {
        return createRequest(HttpMethod.Post, "/api/v1/clubs/$id/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun leaveClub(token: String, id: String) {
        return createRequest(HttpMethod.Delete, "/api/v1/clubs/$id/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun updateClubMembership(
        token: String,
        id: String,
        userId: String,
        role: String,
    ): ClubMembership {
        return createRequest(HttpMethod.Put, "/api/v1/clubs/$id/members", token) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "clubId" to id,
                    "userId" to userId,
                    "role" to role
                )
            )
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getCotisantConfigurations(): List<CotisantConfiguration> {
        return createRequest(HttpMethod.Get, "/api/v1/cotisants").body()
    }

    @Throws(Exception::class)
    suspend fun getTicketConfigurations(): List<TicketConfiguration> {
        return createRequest(HttpMethod.Get, "/api/v1/tickets").body()
    }

    @Throws(Exception::class)
    suspend fun createShopItem(
        token: String,
        type: String,
        id: String,
    ): PaymentResponse {
        return createRequest(HttpMethod.Post, "/api/v1/shop/$type/$id", token).body()
    }

    @Throws(Exception::class)
    suspend fun getShopItemPayment(
        token: String,
        type: String,
        id: String,
        itemId: String,
    ): PaymentResponse {
        return createRequest(HttpMethod.Get, "/api/v1/shop/$type/$id/$itemId/payment", token).body()
    }

    @Throws(Exception::class)
    suspend fun sendNotification(token: String, payload: NotificationPayload) {
        return createRequest(HttpMethod.Post, "/api/v1/notifications", token) {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getIntegrationConfiguration(token: String): IntegrationConfiguration {
        return createRequest(HttpMethod.Get, "/api/v1/integration/configuration", token).body()
    }

    @Throws(Exception::class)
    suspend fun getIntegrationChallenges(token: String): List<IntegrationChallenge> {
        return createRequest(HttpMethod.Get, "/api/v1/integration/challenges", token).body()
    }

    @Throws(Exception::class)
    suspend fun getIntegrationTeams(token: String): List<IntegrationTeam> {
        return createRequest(HttpMethod.Get, "/api/v1/integration/teams", token).body()
    }

    @Throws(Exception::class)
    suspend fun postIntegrationTeams(token: String, name: String, description: String): IntegrationTeam {
        return createRequest(HttpMethod.Post, "/api/v1/integration/teams", token) {
            contentType(ContentType.Application.Json)
            setBody(IntegrationTeamUpload(name, description))
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getIntegrationTeamMembers(token: String, id: String): List<IntegrationMembership> {
        return createRequest(HttpMethod.Get, "/api/v1/integration/teams/$id/members", token).body()
    }

    @Throws(Exception::class)
    suspend fun joinIntegrationTeam(token: String, id: String): IntegrationMembership {
        return createRequest(HttpMethod.Post, "/api/v1/integration/teams/$id/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun leaveIntegrationTeam(token: String, id: String) {
        return createRequest(HttpMethod.Delete, "/api/v1/integration/teams/$id/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun getIntegrationTeamExecutions(token: String, id: String): List<IntegrationExecution> {
        return createRequest(HttpMethod.Get, "/api/v1/integration/teams/$id/executions", token).body()
    }

    @Throws(Exception::class)
    suspend fun postIntegrationTeamExecution(
        token: String,
        id: String,
        challengeId: String,
        proof: ByteArray,
        filename: String,
    ): IntegrationExecution {
        return createRequest(HttpMethod.Post, "/api/v1/integration/teams/$id/executions", token) {
            setBody(MultiPartFormDataContent(formData {
                append("challenge", challengeId)
                append("file", proof, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=$filename")
                })
            }))
        }.body()
    }

    @Throws(Exception::class)
    suspend fun postIntegrationTeamExecution(
        token: String,
        id: String,
        executionId: String,
        status: String,
    ): IntegrationExecution {
        return createRequest(
            HttpMethod.Post,
            "/api/v1/integration/teams/$id/executions/$executionId/$status",
            token
        ).body()
    }

    @Throws(Exception::class)
    suspend fun getChat(token: String): List<ChatConversation> {
        return createRequest(HttpMethod.Get, "/api/v1/chat", token).body()
    }

    @Throws(Exception::class)
    suspend fun getChat(
        token: String,
        type: String,
        id: String,
    ): ChatConversation {
        return createRequest(HttpMethod.Get, "/api/v1/chat/$type/$id", token).body()
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
                        onMessage(json.decodeFromString<ChatMessage>(text.substring("ChatMessage:".length)))
                    } else if (text.startsWith("ChatMembership:")) {
                        onMessage(json.decodeFromString<ChatMembership>(text.substring("ChatMembership:".length)))
                    }
                }
            } finally {
                onDisconnected()
                it.close()
            }
        }
    }

}
