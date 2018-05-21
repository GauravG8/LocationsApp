package com.mylocations.map

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.widget.Toast

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mylocations.R
import com.mylocations.databinding.ActivityMapsBinding
import com.mylocations.repository.Repository
import com.mylocations.repository.local.LocalSingleton
import com.mylocations.repository.models.CustomLocation
import com.mylocations.repository.remote.RemoteSingleton
import com.mylocations.utils.Config
import com.mylocations.addlocation.AddLocationActivity
import com.mylocations.BaseActivity
import com.mylocations.list.LocationListActivity
import com.mylocations.managers.NetworkManager

import java.util.*

/**
 * Maps activity to display the markers - both default and custom locations.
 * The user can add a custom location by placing a marker in the map.
 */
class MapsActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {
    private val LOCATION_REQUEST = 1
    private var mLocationGranted = false
    private lateinit var mMap: GoogleMap
    private var mLastKnownLocation: Location? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mNewMarker: Marker? = null
    private lateinit var mViewModel: MapViewModel
    private lateinit var binding: ActivityMapsBinding

    //Default location to display in map
    private lateinit var sydney : LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding with the layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

        //Basic setup for displaying map and location
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //View model setup with observers for updating UI
        mViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        mViewModel.getLocationList()!!.observe(this, Observer { locations -> addMarkersToMap(locations!!) })
        mViewModel.isLoading.observe(this, Observer { status -> if(status == true) showProgress() else closeProgress() })
        mViewModel.isSelectingLive.observe(this, Observer { status -> showOrHideMarker(status) })
        mViewModel.getItemCount()!!.observe(this, Observer { count -> mViewModel.itemCount.set(if(count != null) "$count Locations" else "No Locations")})
        mViewModel.listItemLive.observe(this, Observer { status -> if(status ==true) startListActivity() })
        mViewModel.selectLocationLive.observe(this, Observer { status -> if(status ==true) startAddLocationActivity() })
        mViewModel.appStart();

        binding.viewModel = mViewModel

        //Toolbar setup
        setSupportActionBar(binding.toolbar as Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setTitle(getString(R.string.app_name))

        sydney = LatLng(-34.0, 151.0)
    }

    /**
     * Called when the user clicks on the floating action button.
     * If the operation is Add Location, a new marker is created and placed on the map.
     * @param showMarker
     */
    private fun showOrHideMarker(showMarker: Boolean?){
        if (showMarker!!) {
            binding.selectLocation.animate().alpha(1.0f)
            binding.listItems.animate().alpha(0.0f)
            val latLng: LatLng
            if (mLastKnownLocation == null) {
                latLng = sydney;
            } else {
                latLng = LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)
            }
            if (::mMap.isInitialized) {
                mNewMarker = mMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)).alpha(0.8f).zIndex(100f).draggable(true))
                mNewMarker!!.tag = -1
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                setMarkerDetails(latLng)
            }
            showToast(getString(R.string.drop_pin))
        } else {
            binding.selectLocation.animate().alpha(0.0f)
            binding.listItems.animate().alpha(1.0f)
            if (mNewMarker != null) mNewMarker!!.remove()
        }
    }


    /**
     * Manipulates the map once available.LatLn
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerDragListener(this)
        mMap.setOnMarkerClickListener(this)

        checkLocationPermission()
        updateLocationUI()
        if (NetworkManager(applicationContext).isConnected!!.not()){
            showToast(getString(R.string.network_connection))
        }
    }

    /**
     * Check whether location permission is granted. Else, request from the user,
     */
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationGranted = true
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                mLocationGranted = false
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationGranted = true
                    //get the current location if location permission has been granted
                    getCurrentLocation()
                }
                updateLocationUI()
            }
        }
    }

    /**
     * Display my location icon in the map based on the permissions
     */
    private fun updateLocationUI() {
        try {
            if (mLocationGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
            }
        } catch (e: SecurityException) {

        }

    }

    private fun getCurrentLocation() {
        try {
            if (mLocationGranted) {
                val locationResult = mFusedLocationClient!!.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //Store the current location
                        mLastKnownLocation = task.result
                    }
                }
            }
        } catch (e: SecurityException) {

        }

    }

    /**
     * Start list activity passing the current location as an extra to calculate the distance
     */
    private fun startListActivity(){
        mViewModel.listItemLive.value = false
        if(mLastKnownLocation != null) {
            val intent = Intent(this, LocationListActivity::class.java)
            intent.putExtra(Config.EXTRA_ADDRESS, mLastKnownLocation)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }else{
            checkLocationPermission()
        }
    }

    /**
     * Start add location activity passing the Address object which contains latitude and longitude
     */
    private fun startAddLocationActivity(){
        mViewModel.selectLocationLive.value = false
        if (binding.markedLocation.tag != null) {
            val intent = Intent(this, AddLocationActivity::class.java)
            intent.putExtra(Config.EXTRA_ADDRESS, binding.markedLocation.tag as Address)
            mViewModel.address.set(null)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }else{
            showToast(getString(R.string.something_wrong))
        }
    }

    override fun onMarkerDragStart(marker: Marker) {

    }

    override fun onMarkerDrag(marker: Marker) {

    }

    /**
     * Callback available once the user drop the marker
     */
    override fun onMarkerDragEnd(marker: Marker) {
        try {
            val latLng = marker.position
            setMarkerDetails(latLng)
        } catch (e: Exception) {

        }

    }

    /**
     * Update the text and address of the draggable marker dropped by user.
     * This function calculates the address from the latitude and longitude of the marker position.
     * @param latLng
     */
    private fun setMarkerDetails(latLng : LatLng){
        try {
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val sb = StringBuffer()
                sb.append(if (address.getAddressLine(0) != null) address.getAddressLine(0) else "")
                mViewModel.markedText.set(sb.toString())
                mViewModel.address.set(address)
            } else {
                mViewModel.markedText.set("")
                mViewModel.address.set(null)
            }
        }catch (e : Exception){
            mViewModel.markedText.set("")
            mViewModel.address.set(null)
        }
    }

    /**
     * Called after the locations are retrieved (by observing for changes) from the database.
     * @param locations
     */
    private fun addMarkersToMap(locations: List<CustomLocation>) {
        mMap.clear()
        for (location in locations) {
            val marker = mMap.addMarker(MarkerOptions().position(LatLng(location.latitude, location.longitude)).title(location.locationName))
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(if (location.type == Config.LOCATION_TYPE_DEFAULT) BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_MAGENTA))
            marker.tag = location.id
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.0f))
    }

    /**
     * This callback is available once the user clicks on a marker.
     * Based on the tag, associated with the marker, its details are shown to the user.
     */
    override fun onMarkerClick(marker: Marker?): Boolean {
        val id = marker?.tag as Int
        if (id == -1) return false
        startLocationDetailActivity(id)
        return true;
    }
}
