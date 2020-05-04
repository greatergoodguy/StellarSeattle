package com.greatergoodguy.stellarseattle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.storage.isFavoriteVenue
import com.greatergoodguy.stellarseattle.util.SEATTLE_LATITUDE
import com.greatergoodguy.stellarseattle.util.SEATTLE_LONGITUDE
import com.greatergoodguy.stellarseattle.util.distance
import com.squareup.picasso.Picasso

class VenueAdapter(private val context: Context, private val myDataset: List<Venue>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<VenueAdapter.ViewHolder>() {

    class ViewHolder(
        container: ViewGroup,
        val tvName: TextView,
        val tvCategories: TextView,
        val tvFormattedAddress: TextView,
        val tvLatLong: TextView,
        val ivCategoryIcon: ImageView,
        val ivFavoriteIcon: ImageView
    ) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val container = LayoutInflater.from(parent.context).inflate(R.layout.view_venueitem, parent, false) as RelativeLayout
        val tvName = container.findViewById<TextView>(R.id.tvName)
        val tvCategories  = container.findViewById<TextView>(R.id.tvCategories)
        val tvFormattedAddress = container.findViewById<TextView>(R.id.tvFormattedAddress)
        val tvLatLong = container.findViewById<TextView>(R.id.tvLatLong)
        val ivCategoryIcon = container.findViewById<ImageView>(R.id.categoryIcon)
        val ivFavoriteIcon = container.findViewById<ImageView>(R.id.favoriteIcon)
        return ViewHolder(container, tvName, tvCategories, tvFormattedAddress, tvLatLong, ivCategoryIcon, ivFavoriteIcon)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val venue = myDataset[position]

        holder.tvName.text = venue.name
        holder.tvCategories.text = venue.categories.joinToString(separator = ",")
        holder.tvFormattedAddress.text = if(venue.formattedAddress.isNotEmpty()) venue.formattedAddress[0] else ""

        val distance = distance(SEATTLE_LATITUDE, SEATTLE_LONGITUDE, venue.latitude, venue.longitude)
        holder.tvLatLong.text = "%.2f".format(distance) + " km"

        Picasso.with(context).load(venue.iconUrl).into(holder.ivCategoryIcon)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(venue)
        }

        if(isFavoriteVenue(context, venue.id)) {
            holder.ivFavoriteIcon.visibility = View.VISIBLE
        } else {
            holder.ivFavoriteIcon.visibility = View.GONE
        }
    }

    override fun getItemCount() = myDataset.size

    interface OnItemClickListener {
        fun onItemClick(item: Venue)
    }
}
