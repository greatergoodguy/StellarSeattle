package com.greatergoodguy.stellarseattle.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greatergoodguy.stellarseattle.BuildConfig
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.adapter.SearchSuggestionsAdapter
import com.greatergoodguy.stellarseattle.adapter.VenueAdapter
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

        fab.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            val venues = viewModel.venues.value ?: listOf()
            intent.putExtra(MapActivity.KEY_VENUES, ArrayList(venues))
            startActivity(intent)
        }

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
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
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
    }

    override fun onResume() {
        super.onResume()
        // This will refresh the list to show the favorite icon, in case this field was
        // updated in the VenueDetailsActivity
        viewAdapter.notifyDataSetChanged()
    }
}
