package com.mohamed.findnearbyapp.Views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.revisionviewmodel.GPSTracker
import com.example.revisionviewmodel.LocationGPS
import com.google.android.gms.location.*
import com.mohamed.findnearbyapp.Adapters.PlaceAdapter
import com.mohamed.findnearbyapp.AppPref
import com.mohamed.findnearbyapp.Constant
import com.mohamed.findnearbyapp.Models.Item
import com.mohamed.findnearbyapp.Models.PhotoItem
import com.mohamed.findnearbyapp.R
import com.mohamed.findnearbyapp.ViewModels.MainViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: PlaceAdapter
    var photos: List<PhotoItem>? = null
    private val permissionRequestCode: Int = 1
    lateinit var fusedLocation: FusedLocationProviderClient

    var isFirstTime = true
    var lastLocation: Location? = null
    lateinit var pref : AppPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        pref = AppPref(this)
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    private fun getLocation() {

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocation.lastLocation.addOnCompleteListener(this) { task ->
                    var location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        viewModel.getAllPlaces(location.latitude, location.longitude)?.observe(this,
                            Observer<List<Item>> { items ->
                                pb_loading.visibility = View.GONE
                                if (items != null) {
                                    adapter = PlaceAdapter(items)
                                    rv_places.adapter = adapter
                                    rv_places.layoutManager = LinearLayoutManager(this)
                                } else {
                                    lay_error.visibility = View.VISIBLE
                                }
                            })
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2000
        mLocationRequest.fastestInterval = 1000

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        fusedLocation!!.requestLocationUpdates(
            mLocationRequest, getLocationCallback,
            Looper.myLooper()
        )
    }

    private val getLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (isFirstTime) {
                lastLocation = locationResult.lastLocation
                isFirstTime = false
                displayAllPlaces(lastLocation!!)
            } else {
                if (lastLocation!!.distanceTo(locationResult.lastLocation).toInt() == 500) {
                    lastLocation = locationResult.lastLocation
                    displayAllPlaces(lastLocation!!)
                }
            }
        }
    }

    fun displayAllPlaces(lastLocation: Location) {
        viewModel.getAllPlaces(lastLocation.latitude, lastLocation.longitude)
            ?.observe(this@MainActivity,
                Observer<List<Item>> { items ->
                    pb_loading.visibility = View.GONE
                    if (items != null) {
                        adapter = PlaceAdapter(items)
                        rv_places.adapter = adapter
                        rv_places.layoutManager = LinearLayoutManager(this@MainActivity)
                    } else {
                        lay_error.visibility = View.VISIBLE
                    }
                })
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionRequestCode
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == permissionRequestCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.getItem(0).title = pref.getMode()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_mode -> {
                if (pref.getMode() == Constant.RealTime){
                    pref.setMode(Constant.Single)
                    item.title = Constant.Single
                    fusedLocation.removeLocationUpdates(getLocationCallback)
                    Toast.makeText(this@MainActivity,"No location updates is available",Toast.LENGTH_SHORT).show()
                }else{
                    pref.setMode(Constant.RealTime)
                    item.title = Constant.RealTime
                    requestNewLocationData()
                    Toast.makeText(this@MainActivity,"Location will be updated after 500m",Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}