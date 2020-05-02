package com.greatergoodguy.stellarseattle.data

class SearchSuggestionsResponse (
    val meta: Meta,
    val response: Response
) {
    class Meta(
        val code: Int,
        val requestId: String
    )

    class Response(
        val minivenues: List<MiniVenue>
    ) {
        class MiniVenue (
            val id: String,
            val name: String
        )
    }
}
