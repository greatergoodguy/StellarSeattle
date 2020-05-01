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
    val categories: List<Category>,
    val location: Location
) {
    fun toVenueItem(): VenueItem {
        return VenueItem(
            id = id,
            name = name,
            verified = verified,
            categories = categories.map { it.name },
            formattedAddress = if(location.formattedAddress.isNotEmpty()) location.formattedAddress[0] else ""
        )
    }

    class Category (
        val id: String,
        val name: String,
        val pluralName: String,
        val shortName: String
    )

    class Location (
        val address: String,
        val crossStreet: String,
        val postalCode: String,
        val formattedAddress: List<String>
    )
}
