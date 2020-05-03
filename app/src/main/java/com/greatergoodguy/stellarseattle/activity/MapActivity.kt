package com.greatergoodguy.stellarseattle.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.greatergoodguy.stellarseattle.R
import kotlinx.android.synthetic.main.activity_map.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mapView.onResume()

        map?.let {
            map.setMinZoomPreference(12F)
            map.setMaxZoomPreference(15F)
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


            map.addMarker(MarkerOptions().position(LatLng(47.60475923205166, -122.33636210125788)).title("Storyville Coffee Company"))
            map.addMarker(MarkerOptions().position(LatLng(47.61340942776967, -122.33469499761385)).title("Anchorhead Coffee Co"))
        }
    }
}
