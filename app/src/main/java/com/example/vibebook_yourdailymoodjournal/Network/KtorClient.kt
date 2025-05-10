package com.example.vibebook_yourdailymoodjournal.Network

import android.annotation.SuppressLint
import com.example.vibebook_yourdailymoodjournal.Network.KtorClient.okHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient{

    val okHttpClient : HttpClient by lazy{
        HttpClient(OkHttp){

            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }


            install(Logging) {
                level = LogLevel.BODY
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 15_000
            }
        }
    }

}
    suspend fun GetQuotes(): QuotesResponse {
        return okHttpClient.get("https://zenquotes.io/api/random").body()
    }
