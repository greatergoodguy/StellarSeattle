package com.greatergoodguy.stellarseattle.presentation

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView.OnEditorActionListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.adapter.SearchSuggestionsAdapter
import com.greatergoodguy.stellarseattle.adapter.VenueAdapter
import com.greatergoodguy.stellarseattle.databinding.ActivityMainBinding
import com.greatergoodguy.stellarseattle.di.ViewModelFactory
import com.greatergoodguy.stellarseattle.domain.Venue
import com.greatergoodguy.stellarseattle.storage.SharedPrefsStorage
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var storage: SharedPrefsStorage

    private lateinit var binding: ActivityMainBinding

    private lateinit var typeAheadAdapter: ArrayAdapter<String>

    private lateinit var viewAdapter: VenueAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set up UI components
        viewManager = LinearLayoutManager(this)
        viewAdapter = VenueAdapter(baseContext, storage, object: VenueAdapter.OnItemClickListener {
            override fun onItemClick(item: Venue) {
                val intent = Intent(this@MainActivity, VenueDetailsActivity::class.java)
                intent.putExtra(VenueDetailsActivity.KEY_VENUE, item)
                startActivity(intent)
            }
        })

        recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL)
            )
        }

        typeAheadAdapter = SearchSuggestionsAdapter(this, android.R.layout.select_dialog_item, mutableListOf())
        inputField.threshold = 2
        inputField.setAdapter(typeAheadAdapter)

        inputField.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                inputField.dismissDropDown()
                viewModel.getVenues(viewModel.searchQuery.value.orEmpty())
                true
            }
            false
        }

        // Set up viewModel observable fields
        viewModel.venues.observe(this, Observer {
            viewAdapter.setData(it)
            viewAdapter.notifyDataSetChanged()
            inputField.dismissDropDown()
        })

        viewModel.searchQuery.observe(this, Observer {
            viewModel.getSearchSuggestions(it)
        })

        viewModel.typeAheadWords.observe(this, Observer {
            typeAheadAdapter.clear()
            typeAheadAdapter.addAll(it)
            typeAheadAdapter.notifyDataSetChanged()
        })

        // Set up click listeners
        fab.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            val venues = viewModel.venues.value ?: listOf()
            intent.putExtra(MapActivity.KEY_VENUES, ArrayList(venues))
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        // This will refresh the list to show the favorite icon, in case this field was
        // updated in the VenueDetailsActivity
        viewAdapter.notifyDataSetChanged()
    }
}
