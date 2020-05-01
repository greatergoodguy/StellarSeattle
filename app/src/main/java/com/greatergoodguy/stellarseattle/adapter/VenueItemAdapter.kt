package com.greatergoodguy.stellarseattle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.domain.VenueItem

class VenueItemAdapter(private val myDataset: List<VenueItem>) : RecyclerView.Adapter<VenueItemAdapter.ViewHolder>() {

    class ViewHolder(
        container: ViewGroup,
        val tvName: TextView,
        val tvCategories: TextView,
        val tvFormattedAddress: TextView
    ) : RecyclerView.ViewHolder(container)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val container = LayoutInflater.from(parent.context).inflate(R.layout.view_venueitem, parent, false) as RelativeLayout
        val tvName = container.findViewById<TextView>(R.id.name)
        val tvCategories  = container.findViewById<TextView>(R.id.categories)
        val tvFormattedAddress = container.findViewById<TextView>(R.id.formattedAddress)
        return ViewHolder(container, tvName, tvCategories, tvFormattedAddress)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val venueItem = myDataset[position]

        holder.tvName.text = venueItem.name
        holder.tvCategories.text = venueItem.categories[0]
        holder.tvFormattedAddress.text = venueItem.formattedAddress
    }

    override fun getItemCount() = myDataset.size
}
