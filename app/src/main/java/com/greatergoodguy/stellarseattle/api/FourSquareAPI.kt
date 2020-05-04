package com.greatergoodguy.stellarseattle.api

import com.greatergoodguy.stellarseattle.data.SearchSuggestionsResponse
import com.greatergoodguy.stellarseattle.data.VenueDetailsResponse
import com.greatergoodguy.stellarseattle.data.VenuesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface FourSquareAPI {
    @GET("/v2/venues/search")
    suspend fun getVenues(
        @Query("client_id") clientID: String?,
        @Query("client_secret") clientSecret: String?,
        @Query("near") near: String?,
        @Query("query") query: String?,
        @Query("v") version: String?,
        @Query("limit") limit: Int?
    ): VenuesResponse

    @GET("/v2/venues/{venueId}")
    suspend fun getVenueDetails(
        @Path("venueId") venueId: String,
        @Query("client_id") clientID: String?,
        @Query("client_secret") clientSecret: String?,
        @Query("v") version: String?
    ): VenueDetailsResponse

    @GET("/v2/venues/suggestcompletion")
    suspend fun getSearchSuggestions(
        @Query("client_id") clientID: String?,
        @Query("client_secret") clientSecret: String?,
        @Query("near") near: String?,
        @Query("query") query: String?,
        @Query("v") version: String?,
        @Query("limit") limit: Int?
    ): SearchSuggestionsResponse

}
