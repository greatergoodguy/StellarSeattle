package com.greatergoodguy.stellarseattle.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.domain.Venue
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val fourSquareAPI: FourSquareAPI
): ViewModel() {

    val searchQuery = MutableLiveData<String>()
    val venues = MutableLiveData<List<Venue>>()

    val showFab             = MutableLiveData<Boolean>().apply { value = true }
    val showSpinner         = MutableLiveData<Boolean>().apply { value = true }
    val showRecyclerView    = MutableLiveData<Boolean>().apply { value = false }
    val inputFieldEnabled   = MutableLiveData<Boolean>().apply { value = false }
    val searchButtonEnabled = MutableLiveData<Boolean>().apply { value = false }

//    fun getVenues(searchQuery: String) {
//        viewModelScope.launch {
//            try {
//                val venuesResponse = fourSquareAPI.getVenues(
//                    BuildConfig.FoursquareClientId,
//                    BuildConfig.FoursquareClientSecret,
//                    "Seattle,+WA",
//                    searchQuery,
//                    BuildConfig.FoursquareVersion,
//                    20
//                )
//                val venueItems = venuesResponse.response.venues.map { it.toVenueItem() }
//            } catch(e: Exception) {
//
//            }
//        }
//    }
}
