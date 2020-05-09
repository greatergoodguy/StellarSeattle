package com.greatergoodguy.stellarseattle.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greatergoodguy.stellarseattle.BuildConfig
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.domain.Venue
import kotlinx.coroutines.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val fourSquareAPI: FourSquareAPI
): ViewModel() {

    val searchQuery = MutableLiveData<String>().apply  { value = "" }
    val venues= MutableLiveData<List<Venue>>().apply { value = listOf() }

    val typeAheadWords = MutableLiveData<List<String>>().apply { value = listOf() }

    val isSearchApiRunning = MutableLiveData<Boolean>().apply { value = false }
    val isSearchApiSuccessful = MutableLiveData<Boolean>().apply { value = false }
    val showApiError = MutableLiveData<Boolean>().apply { value = false }

    private var searchSuggestionJob: Job? = null

    fun getVenues(searchQuery: String) {
        viewModelScope.launch {
            searchSuggestionJob?.cancel()
            isSearchApiRunning.postValue(true)
            showApiError.postValue(false)
            try {
                val venuesResponse = fourSquareAPI.getVenues(
                    BuildConfig.FoursquareClientId,
                    BuildConfig.FoursquareClientSecret,
                    "Seattle,+WA",
                    searchQuery,
                    BuildConfig.FoursquareVersion,
                    20
                )
                val result = venuesResponse.response.venues.map { it.toVenueItem() }
                venues.postValue(result)
                isSearchApiSuccessful.postValue(true)
            } catch(e: Exception) {
                isSearchApiSuccessful.postValue(false)
                showApiError.postValue(true)
            }
            isSearchApiRunning.postValue(false)
        }
    }


    fun getSearchSuggestions(query: String) {
        if(query.trim().length < 2) {
            typeAheadWords.postValue(listOf())
            return
        }
        searchSuggestionJob?.cancel()
        searchSuggestionJob = viewModelScope.launch {
            try {
                val getSearchSuggestionsResponse = fourSquareAPI.getSearchSuggestions(
                    BuildConfig.FoursquareClientId,
                    BuildConfig.FoursquareClientSecret,
                    "Seattle,+WA",
                    query,
                    BuildConfig.FoursquareVersion,
                    5
                )

                val result = getSearchSuggestionsResponse.response.minivenues.map { it.name }
                typeAheadWords.postValue(result)
            } catch(e: Exception) {}
        }
    }
}
