package com.greatergoodguy.stellarseattle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.domain.VenueItem
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

class VenueItemAdapter(private val myDataset: List<VenueItem>) : RecyclerView.Adapter<VenueItemAdapter.ViewHolder>() {

    class ViewHolder(
        container: ViewGroup,
        val tvName: TextView,
        val tvCategories: TextView,
        val tvFormattedAddress: TextView,
        val tvLatLong: TextView
    ) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val container = LayoutInflater.from(parent.context).inflate(R.layout.view_venueitem, parent, false) as RelativeLayout
        val tvName = container.findViewById<TextView>(R.id.name)
        val tvCategories  = container.findViewById<TextView>(R.id.categories)
        val tvFormattedAddress = container.findViewById<TextView>(R.id.formattedAddress)
        val tvLatLong = container.findViewById<TextView>(R.id.latLong)
        return ViewHolder(container, tvName, tvCategories, tvFormattedAddress, tvLatLong)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val venueItem = myDataset[position]

        holder.tvName.text = venueItem.name
        holder.tvCategories.text = venueItem.categories.joinToString(separator = ",")
        holder.tvFormattedAddress.text = venueItem.formattedAddress

        var seattleLat = 47.6062F
        var seattleLong = -122.3321F
        var distance = distance(seattleLat, seattleLong, venueItem.latitude, venueItem.longitude)
        holder.tvLatLong.text = "%.2f".format(distance) + " km"
    }

    override fun getItemCount() = myDataset.size

    private fun distance(lat1: Float, long1: Float, lat2: Float, long2: Float): Float {
        var p = 0.017453292519943295;    // 2 * Math.PI / 360
        var a = 0.5 - cos((lat2 - lat1) * p) /2 +
                cos(lat1 * p) * cos(lat2 * p) *
                (1 - cos((long2 - long1) * p))/2;

        return (12742 * asin(sqrt(a))).toFloat(); // 2 * R; R = 6371 km
    }
}
