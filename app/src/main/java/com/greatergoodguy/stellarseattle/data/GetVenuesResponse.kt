package com.greatergoodguy.stellarseattle.data

import com.greatergoodguy.stellarseattle.domain.VenueItem

class GetVenuesResponse (
    val meta: Meta,
    val response: Response
)

class Meta(
    val code: Int,
    val requestId: String
)

class Response(
    val venues: List<Venue>
)

class Venue (
    val id: String,
    val name: String,
    val verified: Boolean,
    val url: String
) {
    fun toVenueItem(): VenueItem {
        return VenueItem(
            id = id,
            name = name,
            verified = verified,
            categories = listOf("Hello"),
            formattedAddress = ""
        )

    }
}
