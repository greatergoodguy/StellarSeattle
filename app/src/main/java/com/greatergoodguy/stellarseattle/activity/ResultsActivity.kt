package com.greatergoodguy.stellarseattle.activity

import android.os.Bundle
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class ResultsActivity : AppCompatActivity() {

    private lateinit var spinner: View

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val items = mutableListOf<VenueItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        spinner = findViewById(R.id.spinner)

        viewManager = LinearLayoutManager(this)
        viewAdapter = VenueItemAdapter(items)

        recyclerView = findViewById<RecyclerView>(R.id.resultsList).apply {
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

        getVenueResponses()
    }

    private fun getVenueResponses() {
        spinner.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        GlobalScope.launch {
            try {
                val searchAPI = APIClient.client?.create(FourSquareAPI::class.java)
                val getVenuesResponse = searchAPI?.getVenues("VM1IINUCXSHQJRSBJPIQWBJKCVAV4YUQQ31VWHYQRITLPY0D", "GUJPBGJMVTQWEPNRU5V0WISFH11LCU1WDSVS2JBN3W5SE1GJ", "Seattle,+WA", "coffee", "20180401", 20)
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
        spinner.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        items.clear()
        items.addAll(venueItems)
        viewAdapter.notifyDataSetChanged()
    }

    private fun showError() {
        spinner.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }
}
