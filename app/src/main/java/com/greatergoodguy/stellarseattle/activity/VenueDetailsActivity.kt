package com.greatergoodguy.stellarseattle.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.greatergoodguy.stellarseattle.BuildConfig
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.api.APIClient
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.domain.VenueDetails
import com.greatergoodguy.stellarseattle.storage.addFavoriteVenue
import com.greatergoodguy.stellarseattle.storage.getFavoriteVenueIds
import com.greatergoodguy.stellarseattle.storage.isFavoriteVenue
import com.greatergoodguy.stellarseattle.storage.removeFavoriteVenue
import com.greatergoodguy.stellarseattle.util.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_venuedetails.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VenueDetailsActivity : AppCompatActivity() {

    private lateinit var venue: Venue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venuedetails)

        venue = intent.getSerializableExtra(KEY_VENUE) as Venue

        if(venue == null) {
            return
        }

        val staticMapsUrlBase = "https://maps.googleapis.com/maps/api/staticmap?size=600x400&maptype=roadmap&markers=color:blue|%.6f,%.6f&markers=color:red|%.6f,%.6f&key=%s"
        val staticMapsUrl = staticMapsUrlBase.format(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venue.latitude, venue.longitude, BuildConfig.GoogleApiKey)
        Picasso.with(this).load(staticMapsUrl).placeholder(R.drawable.mapsstatic_placeholder).into(staticMap)

        tvName.text = venue.name
        tvCategories.text = venue.categories.joinToString(separator = ",")
        tvFormattedAddress.text = venue.formattedAddress.joinToString(separator = "\n")

        val distance = distance(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venue.latitude, venue.longitude)
        tvLatLong.text = "%.2f".format(distance) + " km"

        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator

        Log.d("VenueDetailsActivity", getFavoriteVenueIds(this).toString())
        favoriteButton.isChecked = isFavoriteVenue(this, venue.id)
        favoriteButton.setOnCheckedChangeListener(object:View.OnClickListener, CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
                view?.startAnimation(scaleAnimation)
                if(isChecked) {
                    addFavoriteVenue(this@VenueDetailsActivity, venue.id)
                } else {
                    removeFavoriteVenue(this@VenueDetailsActivity, venue.id)
                }
            }

            override fun onClick(p0: View?) {
            }
        });

        getVenueDetails(venue.id)
    }

    private fun getVenueDetails(venueId: String) {
        spinner.visibility = View.VISIBLE
        detailsContainer.visibility = View.GONE
        GlobalScope.launch {
            try {
                val fourSquareAPI = APIClient.client?.create(FourSquareAPI::class.java)
                val venueDetailsResponse = fourSquareAPI?.getVenueDetails(venueId, BuildConfig.FoursquareClientId, BuildConfig.FoursquareClientSecret, BuildConfig.FoursquareVersion)
                venueDetailsResponse?.response?.venue?.let {
                    val venueDetails = it.toVenueDetails()
                    runOnUiThread {
                        updateUI(venueDetails)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    spinner.visibility = View.GONE
                }
            }
        }
    }

    private fun updateUI(venueDetails: VenueDetails) {
        spinner.visibility = View.GONE
        detailsContainer.visibility = View.VISIBLE

        tvDescription.text = venueDetails.description

        venueDetails.url?.let { websiteUrl ->
            websiteUrlContainer.visibility = View.VISIBLE
            tvWebsiteUrl.text = websiteUrl
            websiteUrlContainer.setOnClickListener {
                showExternalBrowser(this@VenueDetailsActivity, websiteUrl)
            }
        } ?: run {
            websiteUrlContainer.visibility = View.GONE
        }
    }

    companion object {
        const val KEY_VENUE = "KEY_VENUE"
    }
}
