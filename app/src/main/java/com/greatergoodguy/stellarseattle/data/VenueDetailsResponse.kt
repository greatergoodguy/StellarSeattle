package com.greatergoodguy.stellarseattle.data

class VenueDetailsResponse (
    val meta: Meta,
    val response: Response
) {
    class Meta(
        val code: Int,
        val requestId: String
    )

    class Response(
        val venue: VenueDetails
    ) {
        class VenueDetails (
            val id: String,
            val name: String,
            val description: String?,
            val url: String?
        ) {
            fun toVenueDetails(): com.greatergoodguy.stellarseattle.domain.VenueDetails {
                return com.greatergoodguy.stellarseattle.domain.VenueDetails(
                    id = id,
                    name = name,
                    description = description.orEmpty(),
                    url = url
                )
            }
        }
    }
}
