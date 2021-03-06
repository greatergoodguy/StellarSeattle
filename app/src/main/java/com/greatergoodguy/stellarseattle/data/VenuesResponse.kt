package com.greatergoodguy.stellarseattle.data

import com.greatergoodguy.stellarseattle.domain.Venue

class VenuesResponse (
    val meta: Meta,
    val response: Response
) {
    class Meta(
        val code: Int,
        val requestId: String
    )

    class Response(
        val venues: List<Venue>
    ) {

        class Venue (
            val id: String,
            val name: String,
            val verified: Boolean,
            val categories: List<Category>,
            val location: Location
        ) {
            fun toVenueItem(): com.greatergoodguy.stellarseattle.domain.Venue {
                return Venue(
                    id = id,
                    name = name,
                    categories = categories.map { it.name },
                    formattedAddress = location.formattedAddress.map { it },
                    latitude = location.lat,
                    longitude = location.lng,
                    iconUrl = if(categories.isNotEmpty()) categories[0].icon.prefix + "bg_64" + categories[0].icon.suffix else ""
                )
            }

            class Category (
                val id: String,
                val name: String,
                val pluralName: String,
                val shortName: String,
                val icon: Icon
            ) {
                class Icon (
                    val prefix: String,
                    val suffix: String
                )
            }

            class Location (
                val address: String,
                val crossStreet: String,
                val postalCode: String,
                val lat: Float,
                val lng: Float,
                val formattedAddress: List<String>
            )
        }

    }
}
