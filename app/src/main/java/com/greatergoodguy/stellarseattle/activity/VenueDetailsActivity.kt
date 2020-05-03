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
import kotlin.math.min


class VenueDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venuedetails)
    }

    companion object {
        const val KEY_VENUE = "KEY_VENUE"
    }
}
