package com.mylocations.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.mylocations.BaseActivity
import com.mylocations.R
import com.mylocations.databinding.ActivityLocationListBinding
import com.mylocations.utils.Config
import com.mylocations.list.adapters.LocationListAdapter
import com.mylocations.repository.Repository
import com.mylocations.repository.local.LocalSingleton
import com.mylocations.repository.remote.RemoteSingleton

/**
 * Activity to display the list of locations. The locations are sorted in the increasing order of
 * distance from the current location.
 */
class LocationListActivity : BaseActivity(),   LocationListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityLocationListBinding
    private val adapter = LocationListAdapter(arrayListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding with the layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_list)

        //Location extra passed from the MapsActivity
        val location = intent.getParcelableExtra<Location>(Config.EXTRA_ADDRESS)

        //View Model setup and observers
        val viewModel = ViewModelProviders.of(this).get(LocationListViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()
        viewModel.location = location
        viewModel.loadLocations()!!.observe(this, Observer {
            it?.let {
                viewModel.createLocationList(it)
            }
        })
        viewModel.locationList.observe(this, Observer {
            it?.let {
                //Replace the new data in the adapter and update the view
                adapter.replaceData(it)
            }
        })

        //Recycler view setup
        binding.listView.layoutManager = LinearLayoutManager(this)
        binding.listView.adapter = adapter


        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.app_name))
    }

    /**
     * Callback available on clicking an item in the list.
     * This takes the user to the location's detail screen
     */
    override fun onItemClick(id: Int) {
        startLocationDetailActivity(id)
    }
}