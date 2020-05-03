package com.greatergoodguy.stellarseattle.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.api.APIClient
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.util.SEATTLE_LATITUDE
import com.greatergoodguy.stellarseattle.util.SEATTLE_LONGITUDE
import com.greatergoodguy.stellarseattle.util.distance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_venuedetails.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class VenueDetailsActivity : AppCompatActivity() {

    private lateinit var venue: Venue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venuedetails)

        venue = intent.getSerializableExtra(KEY_VENUE) as Venue

        val apiKey = "AIzaSyD-CR-KPPIfRnyU-XQmqkmPGleycM_CydE"
        val staticMapsUrlBase = "https://maps.googleapis.com/maps/api/staticmap?size=600x400&maptype=roadmap&markers=color:blue|%.6f,%.6f&markers=color:red|%.6f,%.6f&key=%s"
        val staticMapsUrl = staticMapsUrlBase.format(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venue.latitude, venue.longitude, apiKey)
        Picasso.with(this).load(staticMapsUrl).placeholder(R.drawable.mapsstatic_placeholder).into(staticMap)

        tvName.text = venue.name
        tvCategories.text = venue.categories.joinToString(separator = ",")
        tvFormattedAddress.text = venue.formattedAddress.joinToString(separator = "\n")

        val distance = distance(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venue.latitude, venue.longitude)
        tvLatLong.text = "%.2f".format(distance) + " km"

        getVenueDetails(venue.id)
    }

    private fun getVenueDetails(venueId: String) {
        GlobalScope.launch {
            try {
                val fourSquareAPI = APIClient.client?.create(FourSquareAPI::class.java)
                val clientId = "VM1IINUCXSHQJRSBJPIQWBJKCVAV4YUQQ31VWHYQRITLPY0D"
                val clientSecret = "GUJPBGJMVTQWEPNRU5V0WISFH11LCU1WDSVS2JBN3W5SE1GJ"
                val version = "20180401"
                val getVenuesResponse = fourSquareAPI?.getVenueDetails(venueId, clientId, clientSecret, version)
            } catch (e: Exception) {
            }
        }
    }

    companion object {
        const val KEY_VENUE = "KEY_VENUE"
    }
}
