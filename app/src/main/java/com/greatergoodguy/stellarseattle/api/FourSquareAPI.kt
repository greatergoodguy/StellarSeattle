package com.greatergoodguy.stellarseattle.api

import com.greatergoodguy.stellarseattle.data.GetSearchSuggestionsResponse
import com.greatergoodguy.stellarseattle.data.GetVenuesResponse
import retrofit2.http.GET
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
    ): GetVenuesResponse

    @GET("/v2/venues/suggestcompletion")
    suspend fun getSearchSuggestions(
        @Query("client_id") clientID: String?,
        @Query("client_secret") clientSecret: String?,
        @Query("near") near: String?,
        @Query("query") query: String?,
        @Query("v") version: String?,
        @Query("limit") limit: Int?
    ): GetSearchSuggestionsResponse

}
