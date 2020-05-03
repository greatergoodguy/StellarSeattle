package com.greatergoodguy.stellarseattle.activity

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
import com.greatergoodguy.stellarseattle.domain.VenueItem
import com.greatergoodguy.stellarseattle.util.toPx
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var items: ArrayList<VenueItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        intent.getSerializableExtra(KEY_VENUEITEMS)?.let {
            items = it as ArrayList<VenueItem>
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

            //seattle coordinates
            val seattle = LatLng(47.6062095, -122.3320708)
            map.addMarker(MarkerOptions().position(seattle).title("Seattle").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            map.moveCamera(CameraUpdateFactory.newLatLng(seattle))

            val builder = LatLngBounds.Builder()
            for(item in items) {
                val markerOption = MarkerOptions().position(LatLng(item.latitude.toDouble(), item.longitude.toDouble())).title(item.name)
                builder.include(markerOption.position)
                map.addMarker(markerOption)
            }
            val bounds = builder.build()

            val padding = 48.toPx() // offset from edges of the map in pixels
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            map.moveCamera(cameraUpdate)
        }
    }

    companion object {
        const val KEY_VENUEITEMS = "KEY_VENUEITEMS"
    }
}
