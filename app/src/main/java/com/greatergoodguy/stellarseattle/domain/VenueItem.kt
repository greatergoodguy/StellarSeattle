package com.greatergoodguy.stellarseattle.domain

import java.io.Serializable

class VenueItem (
    val id: String,
    val name: String,
    val categories: List<String>,
    val formattedAddress: List<String>,
    val latitude: Float,
    val longitude: Float,
    val iconUrl: String
) : Serializable
