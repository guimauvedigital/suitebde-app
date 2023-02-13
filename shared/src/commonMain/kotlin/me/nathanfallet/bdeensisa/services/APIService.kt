package me.nathanfallet.bdeensisa.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.nathanfallet.bdeensisa.models.*

class APIService {

    // Constants

    private companion object{
        const val baseUrl = "https://bdensisa.org"
    }

    // Client

    @OptIn(ExperimentalSerializationApi::class)
    private val httpClient = HttpClient {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
    }

    private suspend fun createRequest(
        method: HttpMethod,
        url: String,
        token: String? = null,
        builder: HttpRequestBuilder.() -> Unit = {}
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
        return createRequest(HttpMethod.Post, "/api/auth") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "code" to userCode
            ))
        }.body()
    }

    @Throws(Exception::class)
    suspend fun checkToken(token: String): UserToken? {
        return try {
            createRequest(HttpMethod.Get, "/api/auth", token).body()
        } catch (e: Exception) {
            if (e is ClientRequestException && e.response.status == HttpStatusCode.Unauthorized) null
            else throw e
        }
    }

    @Throws(Exception::class)
    suspend fun sendNotificationToken(token: String, notificationToken: String) {
        createRequest(HttpMethod.Post, "/api/notifications/tokens", token) {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "token" to notificationToken
            ))
        }
    }

    @Throws(Exception::class)
    suspend fun getEvents(): List<Event> {
        return createRequest(HttpMethod.Get, "/api/events").body()
    }

    @Throws(Exception::class)
    suspend fun getTopics(): List<Topic> {
        return createRequest(HttpMethod.Get, "/api/topics").body()
    }

    @Throws(Exception::class)
    suspend fun getUsers(
        token: String,
        offset: Long = 0,
        search: String? = null
    ): List<User> {
        return createRequest(HttpMethod.Get, "/api/users", token) {
            parameter("offset", offset)
            search?.let { search ->
                parameter("search", search)
            }
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getUser(token: String, id: String): User {
        return createRequest(HttpMethod.Get, "/api/users/$id", token).body()
    }

    @Throws(Exception::class)
    suspend fun updateUser(
        token: String,
        id: String,
        firstName: String,
        lastName: String,
        year: String,
        option: String
    ): User {
        return createRequest(HttpMethod.Put, "/api/users/$id", token) {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "year" to year,
                "option" to option
            ))
        }.body()
    }

    @Throws(Exception::class)
    suspend fun updateUser(
        token: String,
        id: String,
        expiration: String
    ): User {
        return createRequest(HttpMethod.Put, "/api/users/$id", token) {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "expiration" to expiration
                )
            )
        }.body()
    }

    @Throws(Exception::class)
    suspend fun getClubs(): List<Club> {
        return createRequest(HttpMethod.Get, "/api/clubs").body()
    }

    @Throws(Exception::class)
    suspend fun getClubsMe(token: String?): List<ClubMembership> {
        return createRequest(HttpMethod.Get, "/api/clubs/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun getClub(id: String): Club {
        return createRequest(HttpMethod.Get, "/api/clubs/$id").body()
    }

    @Throws(Exception::class)
    suspend fun getClubMembers(id: String): List<ClubMembership> {
        return createRequest(HttpMethod.Get, "/api/clubs/$id/members").body()
    }

    @Throws(Exception::class)
    suspend fun joinClub(token: String, id: String): ClubMembership {
        return createRequest(HttpMethod.Post, "/api/clubs/$id/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun leaveClub(token: String, id: String): ClubMembership {
        return createRequest(HttpMethod.Delete, "/api/clubs/$id/me", token).body()
    }

    @Throws(Exception::class)
    suspend fun updateClubMembership(
        token: String,
        id: String,
        userId: String,
        role: String
    ): ClubMembership {
        return createRequest(HttpMethod.Put, "/api/clubs/$id/members", token) {
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

}