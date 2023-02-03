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
import me.nathanfallet.bdeensisa.models.Event
import me.nathanfallet.bdeensisa.models.User
import me.nathanfallet.bdeensisa.models.UserToken

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
    suspend fun getEvents(): List<Event> {
        return createRequest(HttpMethod.Get, "/api/events").body()
    }

    @Throws(Exception::class)
    suspend fun getUsers(token: String): List<UserToken> {
        return createRequest(HttpMethod.Get, "/api/users", token).body()
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
            setBody(mapOf(
                "expiration" to expiration
            ))
        }.body()
    }

}