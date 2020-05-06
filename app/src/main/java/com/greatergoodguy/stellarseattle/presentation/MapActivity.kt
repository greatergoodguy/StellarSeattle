package com.greatergoodguy.stellarseattle.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.util.SEATTLE_LATITUDE
import com.greatergoodguy.stellarseattle.util.SEATTLE_LONGITUDE
import com.greatergoodguy.stellarseattle.util.toPx
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var venues: ArrayList<Venue>
    private var markerVenueMap: HashMap<String, Venue> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        intent.getSerializableExtra(KEY_VENUES)?.let {
            venues = it as ArrayList<Venue>
        }

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mapView.onResume()

        map?.let {
            map.isIndoorEnabled = true

            val uiSettings: UiSettings = map.uiSettings
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isZoomControlsEnabled = true

            //Seattle coordinates
            val seattle = LatLng(SEATTLE_LATITUDE.toDouble(), SEATTLE_LONGITUDE.toDouble())
            val seattleMarker = map.addMarker(MarkerOptions().position(seattle).title("Seattle").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

            markerVenueMap.clear()
            val builder = LatLngBounds.Builder()
            builder.include(seattleMarker.position)
            // In each iteration of the loop, we create the venue Marker, add the coordinates
            // to the LatLngBounds Builder (for calculating the size of the map), and add an entry
            // to the Marker - Venue hashmap so we can access the Venue in the InfoWindowClickListener
            for(venue in venues) {
                val markerOption = MarkerOptions().position(LatLng(venue.latitude.toDouble(), venue.longitude.toDouble())).title(venue.name)
                builder.include(markerOption.position)
                val marker = map.addMarker(markerOption)
                markerVenueMap[marker.id] = venue
            }
            val bounds = builder.build()

            val padding = 48.toPx() // offset from edges of the map in pixels
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            map.moveCamera(cameraUpdate)

            map.setOnInfoWindowClickListener {
                val venueItem = markerVenueMap[it.id]
                venueItem?.let {
                    val intent = Intent(this@MapActivity, VenueDetailsActivity::class.java)
                    intent.putExtra(VenueDetailsActivity.KEY_VENUE, venueItem)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        const val KEY_VENUES = "KEY_VENUES"
    }
}
