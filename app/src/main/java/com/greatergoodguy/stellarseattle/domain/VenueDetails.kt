package com.greatergoodguy.stellarseattle.domain

import java.io.Serializable

class VenueDetails (
    val id: String,
    val name: String,
    val description: String,
    val url: String?
) : Serializable
