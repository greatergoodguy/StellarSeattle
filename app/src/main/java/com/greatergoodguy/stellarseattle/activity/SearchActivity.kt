package com.greatergoodguy.stellarseattle.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.greatergoodguy.stellarseattle.R
import com.greatergoodguy.stellarseattle.adapter.SearchSuggestionsAdapter
import com.greatergoodguy.stellarseattle.api.APIClient
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var typeAheadAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        layoutButton.setOnClickListener {
//            val intent = Intent(this, ResultsActivity::class.java)
//            intent.putExtra(ResultsActivity.KEY_SEARCHQUERY, inputField.text.toString())
//            startActivity(intent)
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
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
    }

    private fun getSearchSuggestions(query: String) {
        if(query.trim().length < 2) {
            updateTypeAheadWords(listOf())
            return
        }

        GlobalScope.launch {
            val searchAPI = APIClient.client?.create(FourSquareAPI::class.java)
            val getSearchSuggestionsResponse = searchAPI?.getSearchSuggestions(
                "VM1IINUCXSHQJRSBJPIQWBJKCVAV4YUQQ31VWHYQRITLPY0D",
                "GUJPBGJMVTQWEPNRU5V0WISFH11LCU1WDSVS2JBN3W5SE1GJ",
                "Seattle,+WA",
                query,
                "20180401",
                5
            )

            val newTypeAheadWords = getSearchSuggestionsResponse?.response?.minivenues?.map { it.name } ?: listOf()
            runOnUiThread {
                updateTypeAheadWords(newTypeAheadWords)
            }
        }
    }

    private fun updateTypeAheadWords(newTypeAheadWords: List<String>) {
        typeAheadAdapter.clear()
        typeAheadAdapter.addAll(newTypeAheadWords)
        typeAheadAdapter.notifyDataSetChanged()
    }
}
