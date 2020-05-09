package com.greatergoodguy.stellarseattle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.data.VenueDetailsResponse
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.presentation.VenueDetailsViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class VenueDetailsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var fourSquareAPI: FourSquareAPI

    private lateinit var viewModel: VenueDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Dispatchers.setMain(Dispatchers.Unconfined)

        fourSquareAPI = mock()
        viewModel = VenueDetailsViewModel(
            fourSquareAPI
        )
    }

    @Test
    fun `Given venue is set, then all fields based on venue are correct`() {
        viewModel.venue.postValue(
            Venue(
                id = "4d5435bab3d9a35d2513cb7b",
                name = "PitchBook",
                categories = listOf("Office", "Business"),
                formattedAddress = listOf("901 5th Ave Suite 1200", "Seattle, WA 98164", "United States"),
                latitude = 47.60571F,
                longitude = -122.33211F,
                iconUrl = "https://ss3.4sqi.net/img/categories_v2/building/default_bg_64.png"
            )
        )

        viewModel.name.observeForever(mock())
        viewModel.categories.observeForever(mock())
        viewModel.formattedAddress.observeForever(mock())
        viewModel.distance.observeForever(mock())

        val expectedName        = "PitchBook"
        val expectedCategories  = "Office,Business"
        val expectedAddress     = "901 5th Ave Suite 1200\nSeattle, WA 98164\nUnited States"
        val expectedDistance    = "0.06 km"

        Assert.assertEquals(expectedName,       viewModel.name.value)
        Assert.assertEquals(expectedCategories, viewModel.categories.value)
        Assert.assertEquals(expectedAddress,    viewModel.formattedAddress.value)
        Assert.assertEquals(expectedDistance,   viewModel.distance.value)
    }

    @Test
    fun `Given venueDetails API is called, then all fields based on venueDetails are correct`() = runBlocking {
        val mockVenueId = ""
        val clientId = BuildConfig.FoursquareClientId
        val clientSecret = BuildConfig.FoursquareClientSecret
        val version = BuildConfig.FoursquareVersion

        val fileName = "venue_details_success_sample.json"
        val jsonString = VenueDetailsViewModelTest::class.java.classLoader?.getResourceAsStream(fileName)?.bufferedReader().use {
            it?.readText().orEmpty()
        }

        val venueDetailsResponse = Gson().fromJson(jsonString, VenueDetailsResponse::class.java)
        whenever(fourSquareAPI.getVenueDetails(mockVenueId, clientId, clientSecret, version)).thenReturn(
            venueDetailsResponse
        )

        val venueDetailsLatch = CountDownLatch(1)
        viewModel.venueDetails.observeForever { venueDetailsLatch.countDown() }
        viewModel.description.observeForever(mock())
        viewModel.websiteUrl.observeForever(mock())

        viewModel.getVenueDetails(mockVenueId)
        venueDetailsLatch.await(2, TimeUnit.SECONDS)

        val expectedDescription = "Individual artisan-style pizzas and salads made on demand - superfast! Are you MOD"
        val expectedWebsite = "http://modpizza.com/locations/downtown-seattle/"
        Assert.assertEquals(expectedDescription, viewModel.description.value)
        Assert.assertEquals(expectedWebsite, viewModel.websiteUrl.value)
    }
}
