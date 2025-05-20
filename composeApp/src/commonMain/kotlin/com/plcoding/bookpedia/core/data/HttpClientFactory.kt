package com.plcoding.bookpedia.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger

object HttpClientFactory {

    fun create(engine: HttpClientEngine): HttpClient {

        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 20000L
                requestTimeoutMillis = 20000L
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }

                }

                level = LogLevel.ALL
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

        }

    }
}