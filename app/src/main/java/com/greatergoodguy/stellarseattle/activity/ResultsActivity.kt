package com.greatergoodguy.stellarseattle.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.adapter.VenueItemAdapter
import com.greatergoodguy.stellarseattle.domain.VenueItem
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

    private val items = mutableListOf<VenueItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        fab.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(MapActivity.KEY_VENUEITEMS, ArrayList(items))
            startActivity(intent)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = VenueItemAdapter(baseContext, items, object: VenueItemAdapter.OnItemClickListener {
            override fun onItemClick(item: VenueItem) {
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

    private fun getVenueResponses(searchQuery: String) {
        fab.visibility = View.GONE
        spinner.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        GlobalScope.launch {
            try {
                val searchAPI = APIClient.client?.create(FourSquareAPI::class.java)
                val getVenuesResponse = searchAPI?.getVenues("VM1IINUCXSHQJRSBJPIQWBJKCVAV4YUQQ31VWHYQRITLPY0D", "GUJPBGJMVTQWEPNRU5V0WISFH11LCU1WDSVS2JBN3W5SE1GJ", "Seattle,+WA", searchQuery, "20180401", 20)
                val venueItems = getVenuesResponse?.response?.venues?.map { it.toVenueItem() } ?: listOf()
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

    private fun updateList(venueItems: List<VenueItem>) {
        fab.visibility = View.VISIBLE
        spinner.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        items.clear()
        items.addAll(venueItems)
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
