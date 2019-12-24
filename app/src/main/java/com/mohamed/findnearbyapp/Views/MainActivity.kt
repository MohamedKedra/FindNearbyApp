package com.mohamed.findnearbyapp.Views

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamed.findnearbyapp.Adapters.PlaceAdapter
import com.mohamed.findnearbyapp.Models.Item
import com.mohamed.findnearbyapp.R
import com.mohamed.findnearbyapp.ViewModels.MainViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getAllPlaces(30.073631, 31.366609)?.observe(this,
            Observer<List<Item>> { items ->
                pb_loading.visibility = View.GONE
                if (items != null){
                    adapter = PlaceAdapter(items)
                    rv_places.adapter = adapter
                    rv_places.layoutManager = LinearLayoutManager(this)
                }else{
                    lay_error.visibility = View.VISIBLE
                }
            })
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
