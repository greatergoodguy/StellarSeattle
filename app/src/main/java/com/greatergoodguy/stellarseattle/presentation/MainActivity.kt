package com.greatergoodguy.stellarseattle.presentation

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.BuildConfig
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import com.greatergoodguy.stellarseattle.databinding.ActivityMainBinding
import com.greatergoodguy.stellarseattle.di.ViewModelFactory
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.storage.SharedPrefsStorage
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var storage: SharedPrefsStorage

    @Inject
    lateinit var fourSquareAPI: FourSquareAPI

    private lateinit var binding: ActivityMainBinding

    private var searchSuggestionJob: Job? = null

    private lateinit var typeAheadAdapter: ArrayAdapter<String>

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val venues = mutableListOf<Venue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        fab.setOnClickListener {
//            val intent = Intent(this, MapActivity::class.java)
//            intent.putExtra(MapActivity.KEY_VENUES, ArrayList(venues))
//            startActivity(intent)
//        }
//
//        viewManager = LinearLayoutManager(this)
//        viewAdapter = VenueAdapter(baseContext, storage, venues, object: VenueAdapter.OnItemClickListener {
//            override fun onItemClick(item: Venue) {
//                val intent = Intent(this@MainActivity, VenueDetailsActivity::class.java)
//                intent.putExtra(VenueDetailsActivity.KEY_VENUE, item)
//                startActivity(intent)
//            }
//        })
//
//        recyclerView.apply {
//            layoutManager = viewManager
//            adapter = viewAdapter
//            // use this setting to improve performance if you know that changes
//            // in content do not change the layout size of the RecyclerView
//            setHasFixedSize(true)
//            addItemDecoration(DividerItemDecoration(
//                context,
//                DividerItemDecoration.VERTICAL
//            ))
//        }
//
//        inputField.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {}
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                getSearchSuggestions(s.toString())
//
//            }
//        })
//
//        typeAheadAdapter = SearchSuggestionsAdapter(this, android.R.layout.select_dialog_item, mutableListOf())
//        inputField.threshold = 2
//        inputField.setAdapter(typeAheadAdapter)
//
//        searchButton.setOnClickListener {
//            hideKeyboard()
//            searchSuggestionJob?.cancel()
//            getVenueResponses(inputField.text.toString())
//        }
    }

    override fun onResume() {
        super.onResume()
        // This will refresh the list to show the favorite icon, in case this field was
        // updated in the VenueDetailsActivity
        //viewAdapter.notifyDataSetChanged()
    }

    private fun getVenueResponses(searchQuery: String) {
        fab.visibility = View.GONE
        spinner.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        inputField.isEnabled = false
        searchButton.isEnabled = false
        GlobalScope.launch {
            try {
                val venuesResponse = fourSquareAPI.getVenues(BuildConfig.FoursquareClientId, BuildConfig.FoursquareClientSecret, "Seattle,+WA", searchQuery, BuildConfig.FoursquareVersion, 20)
                val venueItems = venuesResponse.response.venues.map { it.toVenueItem() }
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
                val getSearchSuggestionsResponse = fourSquareAPI.getSearchSuggestions(
                    BuildConfig.FoursquareClientId,
                    BuildConfig.FoursquareClientSecret,
                    "Seattle,+WA",
                    query,
                    BuildConfig.FoursquareVersion,
                    5
                )

                val newTypeAheadWords = getSearchSuggestionsResponse.response.minivenues.map { it.name }
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
}
