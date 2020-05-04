package com.greatergoodguy.stellarseattle.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.adapter.SearchSuggestionsAdapter
import com.greatergoodguy.stellarseattle.adapter.VenueAdapter
import com.greatergoodguy.stellarseattle.api.APIClient
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private var searchSuggestionJob: Job? = null

    private lateinit var typeAheadAdapter: ArrayAdapter<String>

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val venues = mutableListOf<Venue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra(MapActivity.KEY_VENUES, ArrayList(venues))
            startActivity(intent)
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = VenueAdapter(baseContext, venues, object: VenueAdapter.OnItemClickListener {
            override fun onItemClick(item: Venue) {
                val intent = Intent(this@MainActivity, VenueDetailsActivity::class.java)
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

        inputField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                getSearchSuggestions(s.toString())

            }
        })

        typeAheadAdapter = SearchSuggestionsAdapter(this, android.R.layout.select_dialog_item, mutableListOf())
        inputField.threshold = 2
        inputField.setAdapter(typeAheadAdapter)

        searchButton.setOnClickListener {
            hideKeyboard()
            searchSuggestionJob?.cancel()
            getVenueResponses(inputField.text.toString())
        }
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
        inputField.isEnabled = false
        searchButton.isEnabled = false
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
        inputField.isEnabled = true
        searchButton.isEnabled = true

        this.venues.clear()
        this.venues.addAll(venues)
        viewAdapter.notifyDataSetChanged()
    }

    private fun showError() {
        fab.visibility = View.GONE
        spinner.visibility = View.GONE
        recyclerView.visibility = View.GONE
        inputField.isEnabled = true
        searchButton.isEnabled = true
    }

    private fun getSearchSuggestions(query: String) {
        if(query.trim().length < 2) {
            updateTypeAheadWords(listOf())
            return
        }

        searchSuggestionJob?.cancel()
        searchSuggestionJob = GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val searchAPI = APIClient.client?.create(FourSquareAPI::class.java)
                val getSearchSuggestionsResponse = searchAPI?.getSearchSuggestions(
                    "VM1IINUCXSHQJRSBJPIQWBJKCVAV4YUQQ31VWHYQRITLPY0D",
                    "GUJPBGJMVTQWEPNRU5V0WISFH11LCU1WDSVS2JBN3W5SE1GJ",
                    "Seattle,+WA",
                    query,
                    "20180401",
                    5
                )

                val newTypeAheadWords =
                    getSearchSuggestionsResponse?.response?.minivenues?.map { it.name } ?: listOf()
                runOnUiThread {
                    updateTypeAheadWords(newTypeAheadWords)
                }
            }
        }
    }

    private fun updateTypeAheadWords(newTypeAheadWords: List<String>) {
        typeAheadAdapter.clear()
        typeAheadAdapter.addAll(newTypeAheadWords)
        typeAheadAdapter.notifyDataSetChanged()
    }

    companion object {
        const val KEY_SEARCHQUERY = "KEY_SEARCHQUERY"
    }
}
