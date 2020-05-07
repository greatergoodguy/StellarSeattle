package com.greatergoodguy.stellarseattle.presentation

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.greatergoodguy.stellarseattle.BuildConfig
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.databinding.ActivityVenuedetailsBinding
import com.greatergoodguy.stellarseattle.di.ViewModelFactory
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.storage.SharedPrefsStorage
import com.greatergoodguy.stellarseattle.util.SEATTLE_LATITUDE
import com.greatergoodguy.stellarseattle.util.SEATTLE_LONGITUDE
import com.greatergoodguy.stellarseattle.util.showExternalBrowser
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_venuedetails.*
import javax.inject.Inject

class VenueDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: VenueDetailsViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var storage: SharedPrefsStorage

    @Inject
    lateinit var fourSquareAPI: FourSquareAPI

    private lateinit var binding: ActivityVenuedetailsBinding

    private lateinit var venue: Venue

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_venuedetails)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        venue = intent.getSerializableExtra(KEY_VENUE) as Venue
        viewModel.setVenue(venue)
        viewModel.getVenueDetails(venue.id)

        val staticMapsUrlBase = "https://maps.googleapis.com/maps/api/staticmap?size=600x400&maptype=roadmap&markers=color:blue|%.6f,%.6f&markers=color:red|%.6f,%.6f&key=%s"
        val staticMapsUrl = staticMapsUrlBase.format(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venue.latitude, venue.longitude, BuildConfig.GoogleApiKey)
        Picasso.with(this).load(staticMapsUrl).placeholder(R.drawable.mapsstatic_placeholder).into(staticMap)

        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = 500
        scaleAnimation.interpolator = BounceInterpolator()

        favoriteButton.isChecked = storage.isFavoriteVenue(this, venue.id)
        favoriteButton.setOnCheckedChangeListener(object:View.OnClickListener, CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
                view?.startAnimation(scaleAnimation)
                if(isChecked) {
                    storage.addFavoriteVenue(this@VenueDetailsActivity, venue.id)
                } else {
                    storage.removeFavoriteVenue(this@VenueDetailsActivity, venue.id)
                }
            }

            override fun onClick(p0: View?) {}
        })

        websiteUrlContainer.setOnClickListener {
            showExternalBrowser(this@VenueDetailsActivity, viewModel.websiteUrl.value.orEmpty())
        }
    }

    companion object {
        const val KEY_VENUE = "KEY_VENUE"
    }
}
