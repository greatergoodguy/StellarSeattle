package com.greatergoodguy.stellarseattle.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.domain.VenueItem
import com.greatergoodguy.stellarseattle.util.SEATTLE_LATITUDE
import com.greatergoodguy.stellarseattle.util.SEATTLE_LONGITUDE
import com.greatergoodguy.stellarseattle.util.distance
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_venuedetails.*

class VenueDetailsActivity : AppCompatActivity() {

    private lateinit var venueItem: VenueItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venuedetails)

        venueItem = intent.getSerializableExtra(KEY_VENUE) as VenueItem

        val apiKey = "AIzaSyD-CR-KPPIfRnyU-XQmqkmPGleycM_CydE"
        val staticMapsUrlBase = "https://maps.googleapis.com/maps/api/staticmap?center=47.6062,-122.3321&size=600x400&maptype=roadmap&markers=color:blue|47.6062,-122.3321&markers=color:red|%.6f,%.6f&key=%s"
        val staticMapsUrl = staticMapsUrlBase.format(venueItem.latitude, venueItem.longitude, apiKey)
        Picasso.with(this).load(staticMapsUrl).placeholder(R.drawable.mapsstatic_placeholder).into(staticMap)

        name.text = venueItem.name
        categories.text = venueItem.categories.joinToString(separator = ",")
        formattedAddress.text = venueItem.formattedAddress

        val distance = distance(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venueItem.latitude, venueItem.longitude)
        latLong.text = "%.2f".format(distance) + " km"
    }

    companion object {
        const val KEY_VENUE = "KEY_VENUE"
    }
}
