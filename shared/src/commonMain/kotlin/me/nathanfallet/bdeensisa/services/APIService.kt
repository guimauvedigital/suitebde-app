package me.nathanfallet.bdeensisa.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.nathanfallet.bdeensisa.models.UserToken

class APIService {

    // Constants

    private companion object{
        const val baseUrl = "https://bdensisa.org"
    }

    // Client

    @OptIn(ExperimentalSerializationApi::class)
    private val httpClient = HttpClient {
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

    suspend fun checkToken(token: String): UserToken {
        return createRequest(HttpMethod.Get, "/api/auth", token).body()
    }

}