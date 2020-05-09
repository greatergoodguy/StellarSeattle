package com.greatergoodguy.stellarseattle.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.greatergoodguy.stellarseattle.BuildConfig
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.domain.VenueDetails
import com.greatergoodguy.stellarseattle.util.SEATTLE_LATITUDE
import com.greatergoodguy.stellarseattle.util.SEATTLE_LONGITUDE
import com.greatergoodguy.stellarseattle.util.distance
import kotlinx.coroutines.launch
import javax.inject.Inject

class VenueDetailsViewModel @Inject constructor(
    private val fourSquareAPI: FourSquareAPI
): ViewModel() {

    val venue = MutableLiveData<Venue>()
    val name = venue.map { it.name }
    val categories = venue.map { it.categories.joinToString(separator = ",") }
    val formattedAddress = venue.map { it.formattedAddress.joinToString(separator = "\n") }
    val distance = venue.map {
        val distance = distance(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, it.latitude, it.longitude)
        "%.2f".format(distance) + " km"
    }

    val venueDetails = MutableLiveData<VenueDetails>()
    val description = venueDetails.map { it.description }
    val websiteUrl = venueDetails.map { it.url.orEmpty() }
    val showWebsite = websiteUrl.map { !it.isNullOrEmpty() }

    val isVenueDetailsAPIRunning = MutableLiveData<Boolean>().apply { value = false }


    fun setVenue(_venue: Venue) {
        venue.postValue(_venue)
    }

    fun getVenueDetails(venueId: String) {
        viewModelScope.launch {
            isVenueDetailsAPIRunning.postValue(true)
            try {
                val venueDetailsResponse = fourSquareAPI.getVenueDetails(venueId, BuildConfig.FoursquareClientId, BuildConfig.FoursquareClientSecret, BuildConfig.FoursquareVersion)
                venueDetails.postValue(venueDetailsResponse.response.venue.toVenueDetails())
            } catch (e: Exception) {}
            isVenueDetailsAPIRunning.postValue(false)
        }
    }
}
