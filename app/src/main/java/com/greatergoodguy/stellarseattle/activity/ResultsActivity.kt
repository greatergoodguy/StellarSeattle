package com.greatergoodguy.stellarseattle.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.adapter.VenueAdapter
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.api.APIClient
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.activity_results.fab
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class ResultsActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val venues = mutableListOf<Venue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        fab.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(MapActivity.KEY_VENUES, ArrayList(venues))
            startActivity(intent)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = VenueAdapter(baseContext, venues, object: VenueAdapter.OnItemClickListener {
            override fun onItemClick(item: Venue) {
                val intent = Intent(this@ResultsActivity, VenueDetailsActivity::class.java)
                intent.putExtra(VenueDetailsActivity.KEY_VENUE, item)
                startActivity(intent)
            }
        })

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            ))
        }

        var searchQuery: String = intent.getStringExtra(KEY_SEARCHQUERY).orEmpty()
        getVenueResponses(searchQuery)
    }

    override fun onResume() {
        super.onResume()
        // This will refresh the list to show the favorite icon, in case this field was
        // updated in the VenueDetailsActivity
        viewAdapter.notifyDataSetChanged()
    }

    private fun getVenueResponses(searchQuery: String) {
        fab.visibility = View.GONE
        spinner.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        GlobalScope.launch {
            try {
                val fourSquareAPI = APIClient.client?.create(FourSquareAPI::class.java)
                val clientId = "VM1IINUCXSHQJRSBJPIQWBJKCVAV4YUQQ31VWHYQRITLPY0D"
                val clientSecret = "GUJPBGJMVTQWEPNRU5V0WISFH11LCU1WDSVS2JBN3W5SE1GJ"
                val version = "20180401"
                val venuesResponse = fourSquareAPI?.getVenues(clientId, clientSecret, "Seattle,+WA", searchQuery, version, 20)
                val venueItems = venuesResponse?.response?.venues?.map { it.toVenueItem() } ?: listOf()
                runOnUiThread {
                    updateList(venueItems)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    showError()
                }
            }
        }
    }

    private fun updateList(venues: List<Venue>) {
        fab.visibility = View.VISIBLE
        spinner.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        this.venues.clear()
        this.venues.addAll(venues)
        viewAdapter.notifyDataSetChanged()
    }

    private fun showError() {
        fab.visibility = View.GONE
        spinner.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    companion object {
        const val KEY_SEARCHQUERY = "KEY_SEARCHQUERY"
    }
}
