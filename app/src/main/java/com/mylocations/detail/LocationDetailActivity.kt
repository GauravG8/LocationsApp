package com.mylocations.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.mylocations.R
import com.mylocations.databinding.ActivityLocationDetailBinding
import com.mylocations.repository.Repository
import com.mylocations.repository.local.LocalSingleton
import com.mylocations.repository.remote.RemoteSingleton
import com.mylocations.utils.Config
import com.mylocations.BaseActivity
import com.mylocations.MyApplication

/**
 * Detailed view of the location.
 */
class LocationDetailActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding with the layout
        val binding = DataBindingUtil.setContentView<ActivityLocationDetailBinding>(this, R.layout.activity_location_detail)

        //View model setup and observers
        val viewModel = ViewModelProviders.of(this).get(LocationDetailViewModel::class.java)
        val id = intent.getIntExtra(Config.EXTRA_LOCATION_ID, 0).toString()
        viewModel.getLocation(id)!!.observe(this, Observer {
            it?.let {
                viewModel.setDetails(it)
            }
        })
        viewModel.notesUpdated.observe(this, Observer { value -> Toast.makeText(this, value, Toast.LENGTH_SHORT).show() })
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.location_detail))
    }
}