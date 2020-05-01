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
import com.greatergoodguy.stellarseattle.api.APIClient
import com.greatergoodguy.stellarseattle.api.FourSquareAPI
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var typeAheadAdapter: ArrayAdapter<String>
    private lateinit var autoComplete: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        findViewById<MaterialButton>(R.id.layoutButton).setOnClickListener {
            val intent = Intent(this, ResultsActivity::class.java)
            startActivity(intent)
        }

        autoComplete = findViewById<AutoCompleteTextView>(R.id.searchQuery)
        autoComplete.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("SearchActivity", "onTextChanged: " + s.toString())
                getSearchSuggestions(s.toString())

            }
        })

        typeAheadAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, mutableListOf())
        autoComplete.threshold = 2
        autoComplete.setAdapter(typeAheadAdapter)
    }

    var typeAheadWords = mutableListOf(
        "Paries, France",
        "PA, United States",
        "Parana, Brazil",
        "Padua, Italy",
        "Pasadena, CA, United States"
    )


    private fun getSearchSuggestions(query: String) {
        if(query.length < 2) {
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
            Log.d("SearchActivity", newTypeAheadWords.toString())
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
