package com.greatergoodguy.stellarseattle.domain

import java.io.Serializable

class VenueItem (
    val id: String,
    val name: String,
    val categories: List<String>,
    val formattedAddress: String,
    val latitude: Float,
    val longitude: Float,
    val iconUrl: String
) : Serializable
