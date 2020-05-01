package com.greatergoodguy.stellarseattle.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.adapter.VenueItemAdapter
import com.greatergoodguy.stellarseattle.domain.VenueItem
import com.greatergoodguy.stellarseattle.api.APIClient
import com.greatergoodguy.stellarseattle.api.SearchAPI
import com.greatergoodguy.stellarseattle.data.VenueResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ResultsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val items = mutableListOf(
        VenueItem("52d456c811d24128cdd7bc8b", "Storyville Coffee Company", true, listOf("Coffee Shop"), "1001 1st Ave"),
        VenueItem("49d3e558f964a520225c1fe3", "Cherry Street Coffee House", true, listOf("Coffee Shop"), "103 Cherry St (at 1st Ave)"),
        VenueItem("57e95a82498e0a3995a43e90", "Anchorhead Coffee Co", true, listOf("Coffee Shop"), "1600 7th Ave Ste 105 (Olive Way)"),
        VenueItem("52251f9511d2b2f238901956", "Storyville Coffee Company", true, listOf("Coffee Shop"), "94 Pike St #34 (1st Ave (Pike Place Market))"),
        VenueItem("545803de498e7e758ac5605e", "Elm Coffee Roasters", true, listOf("Coffee Shop"), "240 2nd Avenue Ext S Ste 103"),
        VenueItem("55fc6351498e081c6ae9a9c1", "Slate Coffee", false, listOf("Coffee Shop"), "602 2nd Ave (James St)"),
        VenueItem("569d5c6c498e4ff52aa8b724", "Olympia Coffee Roasters", false, listOf("Coffee Shop"), "2021 7th Ave"),
        VenueItem("4a609aa8f964a520ebc01fe3", "Pegasus Coffee", true, listOf("Coffee Shop"), "711 3rd Ave (at Cherry St)"),
        VenueItem("4a53845cf964a5205eb21fe3", "Tully's Coffee", true, listOf("Coffee Shop"), "821 2nd Ave Ste 402 (btw Marion & Columbia)"),
        VenueItem("4a848900f964a520e3fc1fe3", "Cherry Street Coffee House", true, listOf("Coffee Shop"), "808 3rd Ave (btw Marion & Columbia)")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)


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
        GlobalScope.launch {
            val searchAPI = APIClient.client?.create(SearchAPI::class.java)
            searchAPI?.getVenues("VM1IINUCXSHQJRSBJPIQWBJKCVAV4YUQQ31VWHYQRITLPY0D", "GUJPBGJMVTQWEPNRU5V0WISFH11LCU1WDSVS2JBN3W5SE1GJ", "Seattle,+WA", "coffee", "20180401", 20)
        }
    }
}
