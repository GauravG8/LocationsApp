package com.mylocations.addlocation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.location.Address
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.mylocations.R
import com.mylocations.databinding.ActivityAddLocationBinding
import com.mylocations.utils.Config
import com.mylocations.BaseActivity
import com.mylocations.repository.Repository
import com.mylocations.repository.local.LocalSingleton
import com.mylocations.repository.remote.RemoteSingleton

/**
 * Activity to add a new location.
 */
class AddLocationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding with the layout
        val binding = DataBindingUtil.setContentView<ActivityAddLocationBinding>(this, R.layout.activity_add_location)

        //View model setup and observers
        val viewModel = ViewModelProviders.of(this).get(AddLocationViewModel::class.java)
        if (intent != null) {
            val address = intent.getParcelableExtra<Address>(Config.EXTRA_ADDRESS)
            viewModel.setAddress(address)
        }
        viewModel.isLocationAdded().observe(this, Observer {
            locationAdded -> if (locationAdded == true) {
            Toast.makeText(this, getString(R.string.location_added), Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        viewModel.nameError.observe(this, Observer { error -> binding.nameTextLayout.setError(error) })
        viewModel.notesError.observe(this, Observer { error -> binding.notesTextLayout.setError(error) })
        binding.viewModel = viewModel

        //Toolbar setup
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.add_location_title))
    }
}