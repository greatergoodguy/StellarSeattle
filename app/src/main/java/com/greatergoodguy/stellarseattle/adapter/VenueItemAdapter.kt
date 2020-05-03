package com.greatergoodguy.stellarseattle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.domain.VenueItem
import com.greatergoodguy.stellarseattle.util.SEATTLE_LATITUDE
import com.greatergoodguy.stellarseattle.util.SEATTLE_LONGITUDE
import com.greatergoodguy.stellarseattle.util.distance
import com.squareup.picasso.Picasso
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

class VenueItemAdapter(private val context: Context, private val myDataset: List<VenueItem>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<VenueItemAdapter.ViewHolder>() {

    class ViewHolder(
        container: ViewGroup,
        val tvName: TextView,
        val tvCategories: TextView,
        val tvFormattedAddress: TextView,
        val tvLatLong: TextView,
        val ivIcon: ImageView
    ) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val container = LayoutInflater.from(parent.context).inflate(R.layout.view_venueitem, parent, false) as RelativeLayout
        val tvName = container.findViewById<TextView>(R.id.name)
        val tvCategories  = container.findViewById<TextView>(R.id.categories)
        val tvFormattedAddress = container.findViewById<TextView>(R.id.formattedAddress)
        val tvLatLong = container.findViewById<TextView>(R.id.latLong)
        val ivIcon = container.findViewById<ImageView>(R.id.categoryIcon)
        return ViewHolder(container, tvName, tvCategories, tvFormattedAddress, tvLatLong, ivIcon)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val venueItem = myDataset[position]

        holder.tvName.text = venueItem.name
        holder.tvCategories.text = venueItem.categories.joinToString(separator = ",")
        holder.tvFormattedAddress.text = if(venueItem.formattedAddress.isNotEmpty()) venueItem.formattedAddress[0] else ""

        val distance = distance(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venueItem.latitude, venueItem.longitude)
        holder.tvLatLong.text = "%.2f".format(distance) + " km"

        Picasso.with(context).load(venueItem.iconUrl).into(holder.ivIcon)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(venueItem)
        }
    }

    override fun getItemCount() = myDataset.size

    interface OnItemClickListener {
        fun onItemClick(item: VenueItem)
    }
}
