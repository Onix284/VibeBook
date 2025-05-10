package com.example.vibebook_yourdailymoodjournal.Network

import kotlinx.serialization.Serializable

@Serializable
data class QuotesResponseItem(
    val a: String,
    val h: String,
    val q: String
)